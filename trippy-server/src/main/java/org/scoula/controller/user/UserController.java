package org.scoula.controller.user;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.dto.TokenPair;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.user.dto.request.CheckPasswordDTO;
import org.scoula.controller.user.dto.request.SignUpDTO;
import org.scoula.controller.user.dto.request.TokenRequestDto;
import org.scoula.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "User")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@ApiOperation(value = "회원가입 API", notes = "이름, 주민등록번호, 휴대폰번호, 비밀번호로 회원가입")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "회원가입 성공"),
		@ApiResponse(code = 400, message = "잘못된 요청 데이터 입니다. (주민등록번호 validation)"),
		@ApiResponse(code = 400, message = "유효하지 않은 토근값입니다."),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다.")
	})
	@PostMapping("/signup")
	public SuccessResponse<TokenPair> signUp(@RequestBody SignUpDTO signUpDTO) {
		return SuccessResponse.success(SuccessCode.SIGNUP_SUCCESS, userService.signUp(signUpDTO));
	}

	@ApiOperation(value = "[JWT]비밀번호 확인", notes = "비밀번호 확인하기 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "비밀번호 확인 성공"),
		@ApiResponse(code = 400, message = "유효하지 않은 토근값입니다."),
		@ApiResponse(code = 500, message = "서버 내부 오류")
	})
	@PostMapping("/password")
	public SuccessNonDataResponse checkPassword(@RequestParam Long userId, @RequestBody CheckPasswordDTO checkPasswordDTO) {
		userService.checkPassword(userId, checkPasswordDTO);
		return SuccessNonDataResponse.success(SuccessCode.CHECK_PASSWORD_SUCCESS);
	}

	@ApiOperation(value = "토큰 갱신 API", notes = "토큰 갱신 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "토큰 갱신 성공입니다."),
		@ApiResponse(code = 401, message = "토큰이만료되었습니다. 다시 로그인해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 404, message = "존재하지 않는 유저입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "서버 내부 오류", response = ErrorResponse.class)
	})
	@PostMapping("/refresh")
	public SuccessResponse<TokenPair> refresh(@RequestBody final TokenRequestDto tokenRequestDto) {
		return SuccessResponse.success(SuccessCode.REFRESH_SUCCESS, userService.refresh(tokenRequestDto));
	}
}
