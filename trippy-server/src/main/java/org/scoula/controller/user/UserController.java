package org.scoula.controller.user;

import java.util.List;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.dto.TokenPair;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.user.dto.response.AllUsersTokenDTO;
import org.scoula.controller.user.dto.request.CheckPasswordDTO;
import org.scoula.controller.user.dto.request.SignUpDTO;
import org.scoula.controller.user.dto.request.TokenRequestDto;
import org.scoula.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "User", description = "유저등록, 비밀번호 확인, 토큰을 관리합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@ApiOperation(value = "회원가입 API", notes = "이름, 주민등록번호, 휴대폰번호, 비밀번호로 회원가입")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "회원가입 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "주민번호가 잘못되었습니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다.", response = ErrorResponse.class)
	})
	@PostMapping("/signup")
	public SuccessResponse<TokenPair> signUp(@ApiParam(value = "회원가입 정보", required = true) @RequestBody SignUpDTO signUpDTO) {
		return SuccessResponse.success(SuccessCode.SIGNUP_SUCCESS, userService.signUp(signUpDTO));
	}

	@ApiOperation(value = "[JWT]비밀번호 확인", notes = "비밀번호 확인하기 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "비밀번호 확인 성공", response = SuccessNonDataResponse.class),
		@ApiResponse(code = 400, message = "비밀번호가 잘못되었습니다.", response = ErrorResponse.class),
		@ApiResponse(code = 400, message = "잘못된 토큰 타입입니다. Access Token을 사용해주세요", response = ErrorResponse.class),
		@ApiResponse(code = 400, message = "유효하지 않은 토큰을 입력했습니다.", response = ErrorResponse.class),
		@ApiResponse(code = 400, message = "만료된 엑세스 토큰입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "서버 내부 오류", response = ErrorResponse.class)
	})
	@PostMapping("/password")
	public SuccessNonDataResponse checkPassword(@RequestParam Long userId, @ApiParam(value = "비밀번호", required = true) @RequestBody CheckPasswordDTO checkPasswordDTO) {
		userService.checkPassword(userId, checkPasswordDTO);
		return SuccessNonDataResponse.success(SuccessCode.CHECK_PASSWORD_SUCCESS);
	}

	@ApiOperation(value = "토큰 갱신 API", notes = "토큰 갱신 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "토큰 갱신 성공입니다.", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "유효하지 않은 토큰을 입력했습니다.", response = ErrorResponse.class),
		@ApiResponse(code = 400, message = "유효하지 않은 리프레시 토큰을 입력했습니다.", response = ErrorResponse.class),
		@ApiResponse(code = 401, message = "토큰이만료되었습니다. 다시 로그인해주세요.", response = ErrorResponse.class),
		@ApiResponse(code = 404, message = "만료된 엑세스 토큰입니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "서버 내부 오류", response = ErrorResponse.class)
	})
	@PostMapping("/refresh")
	public SuccessResponse<TokenPair> refresh(@ApiParam(value = "토큰 정보", required = true) @RequestBody final TokenRequestDto tokenRequestDto) {
		return SuccessResponse.success(SuccessCode.REFRESH_SUCCESS, userService.refresh(tokenRequestDto));
	}

	@ApiOperation(value = "전체 유저의 엑세스 토큰값 조회 API", notes = "모든 유저의 ID, 이름, 액세스 토큰을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "전체 유저 조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 500, message = "서버 내부 오류", response = ErrorResponse.class)
	})
	@GetMapping("/users")
	public SuccessResponse<List<AllUsersTokenDTO>> getAllUsers() {
		return SuccessResponse.success(SuccessCode.GET_ALL_USERS_SUCCESS, userService.getAllUsersToken());
	}
}
