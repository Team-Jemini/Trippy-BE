package org.scoula.controller.invite;

import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.invite.dto.request.InviteRequestDTO;
import org.scoula.controller.invite.dto.response.AcceptInviteResponseDTO;
import org.scoula.controller.invite.dto.response.InviteResponseDTO;
import org.scoula.service.invite.InviteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "Group Account")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invite")
public class InviteController {

	private final InviteService inviteService;
	private final String BASE_URL = "http://localhost:5173/?token=";

	@ApiOperation(value = "초대링크 발급", notes = "모임 계좌의 초대 링크를 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "초대링크 생성 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다"),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다")
	})
	@PostMapping("/reissue")
	public SuccessResponse<InviteResponseDTO> reissueInviteTokenURL(
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId,
		@ApiParam(value = "초대 요청 정보", required = true) @RequestBody InviteRequestDTO request) {

		String inviteTokenURL =
			BASE_URL + inviteService.createInviteTokenURL(userId, request.accountId(), request.accountName());
		InviteResponseDTO inviteResponse = new InviteResponseDTO(inviteTokenURL);

		return SuccessResponse.success(SuccessCode.CREATE_INVITE_TOKEN_SUCCESS, inviteResponse);
	}

	@ApiOperation(value = "초대 토큰 파싱", notes = "초대 링크의 토큰을 파싱하여 모임 정보를 반환합니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "초대 토큰 파싱 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다"),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다")
	})
	@GetMapping("/token-info")
	public SuccessResponse<AcceptInviteResponseDTO> acceptInvite(
		@ApiParam(value = "초대 토큰", required = true) @RequestParam String token) {

		return SuccessResponse.success(SuccessCode.PARSE_INVITE_TOKEN_SUCCESS, inviteService.parseInviteToken(token));
	}
}
