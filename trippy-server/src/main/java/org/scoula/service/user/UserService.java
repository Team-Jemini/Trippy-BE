package org.scoula.service.user;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.scoula.common.dto.TokenPair;
import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.BadRequestException;
import org.scoula.common.exception.model.NotFoundException;
import org.scoula.common.exception.model.UnAuthorizedException;
import org.scoula.common.util.SmsUtil;
import org.scoula.common.util.VerificationCodeGenerator;
import org.scoula.config.jwt.JwtService;
import org.scoula.controller.user.dto.request.CheckPasswordDTO;
import org.scoula.controller.user.dto.request.SignUpDTO;
import org.scoula.controller.user.dto.request.TokenRequestDto;
import org.scoula.controller.user.dto.response.AllUsersTokenDTO;
import org.scoula.domain.user.Gender;
import org.scoula.domain.user.UserVO;
import org.scoula.mapper.user.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final SmsUtil smsUtil;

	/***
	 * 회원가입
	 * 1.  001125-4와 같은 형식으로 오면 LocalDate로 2000-11-25 형식으로 추출, 하고 1234로 성별 추출
	 * 2. 유저의 비밀번호는 주민번호를 이용해서 암호화해서 저장
	 * 3. 유저 정보 저장후 Token 발급
	 *
	 * @param signUpDTO
	 * (이름, 001125-4,010-2344-3333)
	 * @return TokenPair
	 */
	@Transactional
	public TokenPair signUp(SignUpDTO signUpDTO) {
		LocalDate birth = extractBirthFromResidentNumber(signUpDTO.residentNum());
		Gender gender = extractGenderFromResidentNumber(signUpDTO.residentNum());
		String encryptedPassword = passwordEncoder.encode(signUpDTO.password());

		UserVO user = UserVO.builder()
			.name(signUpDTO.name())
			.password(encryptedPassword)
			.phone(signUpDTO.phone())
			.birth(birth)
			.gender(gender)
			.build();

		userMapper.save(user);
		String userId = String.valueOf(user.getUserId());

		return jwtService.generateTokenPair(userId);
	}

	/***
	 * 비밀번호 확인 체크하는 API
	 * 1. 존재하는 유저인지 확인
	 * @param userId
	 * @param checkPasswordDTO
	 * @return
	 */
	public void checkPassword(Long userId, CheckPasswordDTO checkPasswordDTO) {
		validateUserExists(userId);
		UserVO user = userMapper.findById(userId);

		if (!passwordEncoder.matches(checkPasswordDTO.password(), user.getPassword())) {
			throw new UnAuthorizedException(ErrorCode.INVALID_PASSWORD_EXCEPTION);
		}
	}

	/***
	 * 유저 존재 여부 파악
	 * @param userId
	 * @return void
	 */
	public void validateUserExists(Long userId) {
		if (!userMapper.existsById(userId)) {
			throw new NotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION);
		}
	}

	/***
	 * 유저 이름 받아오기
	 * @param userId
	 * @return userName
	 */
	public String getUserName(Long userId) {
		validateUserExists(userId);
		return userMapper.findUserName(userId);
	}

	/***
	 * 전화번호 인증 코드 전송
	 * @param phoneNumber
	 * @throws IOException
	 */
	@Transactional
	public void sendVerificationCodeMessage(final String phoneNumber) throws IOException {
		final String verificationCode = VerificationCodeGenerator.generate();

		if (!smsUtil.sendVerificationCode(phoneNumber, verificationCode))
			throw new BadRequestException(ErrorCode.INVALID_PHONE_NUMBER_EXCEPTION);

		smsUtil.saveVerificationCode(phoneNumber, verificationCode);
	}

	/***
	 * 전화번호 인증 코드 확인
	 * @param phoneNumber
	 * @param verificationCode
	 */
	@Transactional
	public void verifyCode(final String phoneNumber, final String verificationCode) {
		if (!smsUtil.isVerificationCode(phoneNumber))
			throw new NotFoundException(ErrorCode.NOT_FOUND_VERIFICATION_CODE_EXCEPTION);

		if (!smsUtil.getVerificationCode(phoneNumber).equals(verificationCode))
			throw new BadRequestException(ErrorCode.NOT_MATCH_VERIFICATION_CODE_EXCEPTION);

		smsUtil.deleteVerificationCode(phoneNumber);
	}

	/***
	 * AccessToken 갱신
	 * [클라이언트]
	 * 1. AccessToken이 만료되면, 해당 /user/refresh로 AccessToken 재발급 요청을 한다.
	 * 2. RefreshToken도 만료되면,  로그인 페이지로 다시 이동 시켜서 재로그인 해야함
	 * 3. 따라서 클라이언트에 AccessToken이 잘못되었다는 에러 401이오면 바로 refresh로 연결해서 재 발급 받은걸 갈아 끼워서 동작하게 끔 해야한다.
	 *
	 * [백엔드]
	 * 1. 클라이언트의 AccessToken이 만료되어, refreshToken으로 재발급을 시도하려고 한다. 따라서 refreshToken이 유효한지 확인
	 * - 여기에서 클라이언트는 UnAuthorizedException를 받으면 로그인 다시 시켜야한다.
	 * 2. RefreshToken 값에서부터 userId를 추출한다.
	 * 3. Redis에 저장된 RefreshToken와 비교하고 동일하다면 AccessToken와 RefreshToken를 재발급하고, Redis에 RefreshToken를 저장해야함
	 *
	 * @param tokenRequestDto
	 * @return
	 */
	public TokenPair refresh(TokenRequestDto tokenRequestDto) {
		final String refreshToken = tokenRequestDto.refreshToken();

		// 1) 유효한 JWT인지(파싱 가능/서명 OK) + 만료면 여기서 예외로 끝
		jwtService.verifyToken(refreshToken);

		// 2) 이 토큰이 '진짜' RefreshToken인지 (sub=REFRESH_TOKEN)
		if (!jwtService.isRefreshToken(refreshToken)) {
			throw new BadRequestException(ErrorCode.INVALID_REFRESH_TOKEN_EXCEPTION);
		}

		// 3) userId 추출 후 유저 존재 확인
		final String userId = jwtService.getUserIdInToken(refreshToken);
		validateUserExists(Long.parseLong(userId));

		// 4) Redis 저장된 RefreshToken과 일치하는지 (토큰 로테이션 강제)
		if (!jwtService.compareRefreshToken(userId, refreshToken)) {
			throw new UnAuthorizedException(ErrorCode.TOKEN_TIME_EXPIRED_EXCEPTION);
		}

		// 5) (옵션) 기존 AccessToken 즉시 블랙리스트 처리
		if (tokenRequestDto.accessToken() != null && !tokenRequestDto.accessToken().isBlank()) {
			jwtService.blacklistAccessToken(tokenRequestDto.accessToken());
		}

		// 6) 새 토큰 페어 발급 (generateTokenPair가 이전 refresh 삭제 + 새 refresh 저장까지 수행)
		return jwtService.generateTokenPair(userId);
	}

	public List<AllUsersTokenDTO> getAllUsersToken() {
		List<UserVO> users = userMapper.findAll();

		return users.stream()
			.map(user -> {
				// 각 유저마다 새로운 액세스 토큰 생성
				String accessToken = jwtService.createAccessToken(String.valueOf(user.getUserId()));
				return AllUsersTokenDTO.from(user, accessToken);
			}).collect(Collectors.toList());
	}

	/***
	 * 주민등록번호에서 생년월일 추출
	 * @param residentNum
	 * @return LocalDate 
	 */
	private LocalDate extractBirthFromResidentNumber(String residentNum) {
		String birthPart = residentNum.substring(0, 6);
		String genderCode = residentNum.substring(7, 8);

		int year = Integer.parseInt(birthPart.substring(0, 2));
		int month = Integer.parseInt(birthPart.substring(2, 4));
		int day = Integer.parseInt(birthPart.substring(4, 6));

		if (genderCode.equals("1") || genderCode.equals("2")) {
			year += 1900;
		} else if (genderCode.equals("3") || genderCode.equals("4")) {
			year += 2000;
		} else {
			throw new BadRequestException(ErrorCode.INVALID_RESIDENT_NUMBER_EXCEPTION);
		}

		try {
			return LocalDate.of(year, month, day);
		} catch (Exception e) {
			throw new BadRequestException(ErrorCode.INVALID_RESIDENT_NUMBER_EXCEPTION);
		}
	}

	/***
	 * 주민등록번호에서 성별 추출
	 * @param residentNum
	 * @return gender (M/F)
	 */
	private Gender extractGenderFromResidentNumber(String residentNum) {
		String genderCode = residentNum.substring(7, 8);

		switch (genderCode) {
			case "1":
			case "3":
				return Gender.M; // 남성
			case "2":
			case "4":
				return Gender.F; // 여성
			default:
				throw new BadRequestException(ErrorCode.INVALID_RESIDENT_NUMBER_EXCEPTION);
		}
	}

	/***
	 * 유저가 leader인지 확인
	 * @param userId
	 * @return void
	 */
	public void validateUserIsLeader(Long userId) {
		if (!userMapper.existsLeaderInGroup(userId)) {
			throw new NotFoundException(ErrorCode.NOT_GROUP_ACCOUNT_LEADER_EXCEPTION);
		}
	}
}
