package org.scoula.controller.invite;

import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.invite.dto.request.InviteRequestDTO;
import org.scoula.controller.invite.dto.response.InviteResponseDTO;
import org.scoula.service.invite.InviteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "Group Account")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invite")
public class InviteController {

	private final InviteService inviteService;
	private final String BASE_URL = "http://localhost:5173/?token=";

	@ApiOperation(value = "초대링크 발급", notes = "모임 계좌의 초대 초대 링크를 생성합니다.")
	@PostMapping("/reissue")
	public SuccessResponse<InviteResponseDTO> reissueInviteTokenURL(
		@RequestParam Long userId,
		@RequestBody InviteRequestDTO request) {
		String inviteTokenURL =
			BASE_URL + inviteService.createInviteTokenURL(userId, request.accountId(), request.accountName());
		InviteResponseDTO inviteResponse = new InviteResponseDTO(inviteTokenURL);
		return SuccessResponse.success(SuccessCode.CREATE_INVITE_TOKEN_SUCCESS, inviteResponse);
	}
}
