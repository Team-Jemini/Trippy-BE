package org.scoula.service.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scoula.common.dto.TokenPair;
import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.BadRequestException;
import org.scoula.common.exception.model.NotFoundException;
import org.scoula.common.exception.model.UnAuthorizedException;
import org.scoula.config.jwt.JwtService;
import org.scoula.controller.user.dto.request.CheckPasswordDTO;
import org.scoula.controller.user.dto.request.SignUpDTO;
import org.scoula.controller.user.dto.request.TokenRequestDto;
import org.scoula.domain.user.Gender;
import org.scoula.domain.user.UserVO;
import org.scoula.mapper.user.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.log4j.Log4j2;

@ExtendWith(MockitoExtension.class)
@Log4j2
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공 - 비밀번호 암호화된 상태로 저장됨")
    void signUp_Success() {
        // given
        SignUpDTO dto = new SignUpDTO("안현주", "001125-4", "010-1234-5678", "123456");
        String encodedPassword = "ENCODED_123456";
        TokenPair tokenPair = new TokenPair("access.token", "refresh.token");

        when(passwordEncoder.encode(dto.password())).thenReturn(encodedPassword);
        when(jwtService.generateTokenPair("123")).thenReturn(tokenPair);

        doAnswer(invocation -> {
            UserVO user = invocation.getArgument(0);
            user.setUserId(123L);
            assertEquals(encodedPassword, user.getPassword()); // 저장 전에 암호화된 비번 확인
            return null;
        }).when(userMapper).save(any(UserVO.class));

        // when
        TokenPair result = userService.signUp(dto);

        // then
        assertNotNull(result);
        assertEquals(tokenPair, result);
        verify(passwordEncoder).encode(dto.password());
        verify(userMapper).save(any(UserVO.class));
        verify(jwtService).generateTokenPair("123");
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 잘못된 주민등록번호")
    void signUp_InvalidResidentNumber_ThrowsBadRequestException() {
        // given
        SignUpDTO signUpDTO = new SignUpDTO("안현주", "001125-9", "010-1234-5678","123456"); // 잘못된 성별 코드

        // when & then
        BadRequestException exception = assertThrows(BadRequestException.class,
            () -> userService.signUp(signUpDTO));
        assertEquals(ErrorCode.INVALID_RESIDENT_NUMBER_EXCEPTION, exception.getErrorCode());
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 잘못된 날짜")
    void signUp_InvalidDate_ThrowsBadRequestException() {
        // given
        SignUpDTO signUpDTO = new SignUpDTO("안현주", "001399-1", "010-1234-5678", "123456"); // 13월

        // when & then
        BadRequestException exception = assertThrows(BadRequestException.class,
            () -> userService.signUp(signUpDTO));
        assertEquals(ErrorCode.INVALID_RESIDENT_NUMBER_EXCEPTION, exception.getErrorCode());
    }

    @Test
    @DisplayName("생년월일 추출 테스트 - 2000년대 여성")
    void extractBirthFromResidentNumber_2000s_Female() {
        // given
        SignUpDTO signUpDTO = new SignUpDTO("안현주", "001125-4", "010-1234-5678", "123456");

        String encryptedPassword = "encrypted";

        when(passwordEncoder.encode(signUpDTO.password())).thenReturn(encryptedPassword);
        doAnswer(invocation -> {
            UserVO user = invocation.getArgument(0);
            user.setUserId(1L);
            // 생년월일과 성별 검증
            assertEquals(LocalDate.of(2000, 11, 25), user.getBirth());
            assertEquals(Gender.F, user.getGender());
            return null;
        }).when(userMapper).save(any(UserVO.class));
        when(jwtService.generateTokenPair("1")).thenReturn(new TokenPair("token", "refresh"));

        // when
        userService.signUp(signUpDTO);

        // then
        verify(userMapper).save(any(UserVO.class));
    }

    @Test
    @DisplayName("생년월일 추출 테스트 - 1900년대 남성")
    void extractBirthFromResidentNumber_1900s_Male() {
        // given
        SignUpDTO signUpDTO = new SignUpDTO("안현주", "851125-1", "010-1234-5678", "123456"
            + "");
        String encryptedPassword = "encrypted";

        when(passwordEncoder.encode(signUpDTO.password())).thenReturn(encryptedPassword);
        doAnswer(invocation -> {
            UserVO user = invocation.getArgument(0);
            user.setUserId(1L);
            // 생년월일과 성별 검증
            assertEquals(LocalDate.of(1985, 11, 25), user.getBirth());
            assertEquals(Gender.M, user.getGender());
            return null;
        }).when(userMapper).save(any(UserVO.class));
        when(jwtService.generateTokenPair("1")).thenReturn(new TokenPair("token", "refresh"));

        // when
        userService.signUp(signUpDTO);

        // then
        verify(userMapper).save(any(UserVO.class));
    }

    @Test
    @DisplayName("비밀번호 확인 성공 테스트")
    void checkPassword_Success() {
        // given
        Long userId = 123L;
        String rawPassword = "123456";
        String encodedPassword = "encoded-password";
        CheckPasswordDTO checkPasswordDTO = new CheckPasswordDTO(rawPassword);

        when(userMapper.existsById(userId)).thenReturn(true);
        when(userMapper.findById(userId)).thenReturn(
            UserVO.builder()
                .userId(userId)
                .password(encodedPassword)
                .build()
        );
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // when & then
        assertDoesNotThrow(() -> userService.checkPassword(userId, checkPasswordDTO));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 불일치")
    void checkPassword_InvalidPassword_ThrowsUnAuthorizedException() {
        // given
        Long userId = 123L;
        String rawPassword = "wrong-password";
        String encodedPassword = "encoded-password";
        CheckPasswordDTO checkPasswordDTO = new CheckPasswordDTO(rawPassword);

        UserVO user = UserVO.builder()
            .userId(userId)
            .password(encodedPassword)
            .build();

        when(userMapper.existsById(userId)).thenReturn(true);
        when(userMapper.findById(userId)).thenReturn(user);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // when & then
        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class,
            () -> userService.checkPassword(userId, checkPasswordDTO));

        assertEquals(ErrorCode.INVALID_PASSWORD_EXCEPTION, exception.getErrorCode());
    }

    @Test
    @DisplayName("유저 존재 검증 성공 테스트")
    void validateUserExists_UserExists_Success() {
        // given
        Long userId = 123L;
        when(userMapper.existsById(userId)).thenReturn(true);

        // when & then
        assertDoesNotThrow(() -> userService.validateUserExists(userId));
    }

    @Test
    @DisplayName("유저 존재 검증 실패 테스트 - 유저 없음")
    void validateUserExists_UserNotExists_ThrowsNotFoundException() {
        // given
        Long userId = 123L;
        when(userMapper.existsById(userId)).thenReturn(false);

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> userService.validateUserExists(userId));
        assertEquals(ErrorCode.USER_NOT_FOUND_EXCEPTION, exception.getErrorCode());
    }

    @Test
    @DisplayName("유저명 조회 성공 테스트")
    void getUserName_Success() {
        // given
        Long userId = 123L;
        String expectedUserName = "안현주";
        when(userMapper.existsById(userId)).thenReturn(true);
        when(userMapper.findUserName(userId)).thenReturn(expectedUserName);

        // when
        String actualUserName = userService.getUserName(userId);

        // then
        assertEquals(expectedUserName, actualUserName);
    }

    @Test
    @DisplayName("유저명 조회 실패 테스트 - 유저 없음")
    void getUserName_UserNotExists_ThrowsNotFoundException() {
        // given
        Long userId = 123L;
        when(userMapper.existsById(userId)).thenReturn(false);

        // when & then
        assertThrows(NotFoundException.class, () -> userService.getUserName(userId));
    }

    @Test
    @DisplayName("토큰 갱신 성공 테스트")
    void refresh_Success() {
        // given
        String accessToken = "valid.access.token";
        String refreshToken = "valid.refresh.token";
        String userId = "123";
        TokenRequestDto tokenRequestDto = new TokenRequestDto(accessToken, refreshToken);
        TokenPair newTokenPair = new TokenPair("new.access.token", "new.refresh.token");

        when(jwtService.verifyToken(refreshToken)).thenReturn(true);
        when(jwtService.getUserIdInToken(refreshToken)).thenReturn(userId);
        when(userMapper.existsById(Long.parseLong(userId))).thenReturn(true);
        when(jwtService.compareRefreshToken(userId, refreshToken)).thenReturn(true);
        when(jwtService.generateTokenPair(userId)).thenReturn(newTokenPair);

        // when
        TokenPair result = userService.refresh(tokenRequestDto);

        // then
        assertEquals(newTokenPair, result);
        verify(jwtService).saveRefreshToken(userId, newTokenPair.refreshToken());
    }

    @Test
    @DisplayName("토큰 갱신 실패 테스트 - 잘못된 RefreshToken")
    void refresh_InvalidRefreshToken_ThrowsUnAuthorizedException() {
        // given
        String accessToken = "valid.access.token";
        String refreshToken = "invalid.refresh.token";
        TokenRequestDto tokenRequestDto = new TokenRequestDto(accessToken, refreshToken);

        when(jwtService.verifyToken(refreshToken)).thenReturn(false);

        // when & then
        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class,
            () -> userService.refresh(tokenRequestDto));
        assertEquals(ErrorCode.TOKEN_TIME_EXPIRED_EXCEPTION, exception.getErrorCode());
    }

    @Test
    @DisplayName("토큰 갱신 실패 테스트 - 만료된 RefreshToken")
    void refresh_ExpiredRefreshToken_ThrowsUnAuthorizedException() {
        // given
        String accessToken = "valid.access.token";
        String refreshToken = "expired.refresh.token";
        TokenRequestDto tokenRequestDto = new TokenRequestDto(accessToken, refreshToken);

        when(jwtService.verifyToken(refreshToken)).thenThrow(new UnAuthorizedException(ErrorCode.TOKEN_TIME_EXPIRED_EXCEPTION));

        // when & then
        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class,
            () -> userService.refresh(tokenRequestDto));
        assertEquals(ErrorCode.TOKEN_TIME_EXPIRED_EXCEPTION, exception.getErrorCode());
    }

    @Test
    @DisplayName("토큰 갱신 실패 테스트 - 유저 없음")
    void refresh_UserNotExists_ThrowsNotFoundException() {
        // given
        String accessToken = "valid.access.token";
        String refreshToken = "valid.refresh.token";
        String userId = "999";
        TokenRequestDto tokenRequestDto = new TokenRequestDto(accessToken, refreshToken);

        when(jwtService.verifyToken(refreshToken)).thenReturn(true);
        when(jwtService.getUserIdInToken(refreshToken)).thenReturn(userId);
        when(userMapper.existsById(Long.parseLong(userId))).thenReturn(false);

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> userService.refresh(tokenRequestDto));
        assertEquals(ErrorCode.USER_NOT_FOUND_EXCEPTION, exception.getErrorCode());
    }

    @Test
    @DisplayName("토큰 갱신 실패 테스트 - Redis의 RefreshToken과 불일치")
    void refresh_RefreshTokenMismatch_ThrowsUnAuthorizedException() {
        // given
        String accessToken = "valid.access.token";
        String refreshToken = "valid.refresh.token";
        String userId = "123";
        TokenRequestDto tokenRequestDto = new TokenRequestDto(accessToken, refreshToken);

        when(jwtService.verifyToken(refreshToken)).thenReturn(true);
        when(jwtService.getUserIdInToken(refreshToken)).thenReturn(userId);
        when(userMapper.existsById(Long.parseLong(userId))).thenReturn(true);
        when(jwtService.compareRefreshToken(userId, refreshToken)).thenReturn(false);

        // when & then
        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class,
            () -> userService.refresh(tokenRequestDto));
        assertEquals(ErrorCode.TOKEN_TIME_EXPIRED_EXCEPTION, exception.getErrorCode());
    }
}