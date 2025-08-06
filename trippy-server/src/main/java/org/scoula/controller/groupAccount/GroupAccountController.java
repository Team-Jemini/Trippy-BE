package org.scoula.controller.groupAccount;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.groupAccount.dto.request.AcceptInviteTokenRequestDTO;
import org.scoula.controller.groupAccount.dto.request.GroupAccountCreateRequestDTO;
import org.scoula.controller.groupAccount.dto.request.InviteRequestDTO;
import org.scoula.controller.groupAccount.dto.request.SettlementRequestDTO;
import org.scoula.controller.groupAccount.dto.response.AcceptInviteResponseDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountCreateResponseDTO;
import org.scoula.controller.groupAccount.dto.response.InviteResponseDTO;
import org.scoula.service.groupaccount.GroupAccountService;
import org.scoula.service.groupaccount.InviteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "Group Account")
@RestController
@RequiredArgsConstructor
@RequestMapping("/group-account")
public class GroupAccountController {

	private final GroupAccountService groupAccountService;
	private final InviteService inviteService;

	@ApiOperation(value = "[JWT] 모임계좌 생성", notes = "모임계좌 생성 및 모임주를 등록하는 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "모임계좌 생성이 완료되었습니다", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다"),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다", response = ErrorResponse.class)
	})
	@ApiImplicitParam(name = "Authorization", value = "Access Token",
		required = true, dataType = "string", paramType = "header")
	@PostMapping("/create")
	public SuccessResponse<GroupAccountCreateResponseDTO> createGroupAccount(
		@ApiParam(value = "모임계좌", required = true)
		@RequestParam Long userId,
		@RequestBody GroupAccountCreateRequestDTO request) {

		return SuccessResponse.success(SuccessCode.CREATE_GROUP_ACCOUNT_SUCCESS,
			groupAccountService.createGroupAccount(request, userId));
	}

	@ApiOperation(value = "[JWT] 초대링크 발급", notes = "모임 계좌의 초대 링크를 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "초대링크 생성 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다"),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다", response = SuccessResponse.class)
	})
	@PostMapping("/invite/reissue")
	public SuccessResponse<InviteResponseDTO> reissueInviteTokenURL(
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId,
		@ApiParam(value = "초대 요청 정보", required = true) @RequestBody InviteRequestDTO request) {

		return SuccessResponse.success(SuccessCode.CREATE_INVITE_TOKEN_SUCCESS,
			inviteService.createInviteTokenURL(userId, request.accountId(), request.accountName()));
	}

	@ApiOperation(value = "[JWT] 초대 토큰 파싱", notes = "초대 링크의 토큰을 파싱하여 모임 정보를 반환합니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "초대 토큰 파싱 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다"),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다", response = SuccessResponse.class)
	})
	@GetMapping("/invite/token-info")
	public SuccessResponse<AcceptInviteResponseDTO> acceptInvite(
		@ApiParam(value = "초대 토큰", required = true) @RequestParam Long userId,
		@ApiParam(value = "초대 토큰", required = true) @RequestBody AcceptInviteTokenRequestDTO token) {

		return SuccessResponse.success(SuccessCode.PARSE_INVITE_TOKEN_SUCCESS,
			inviteService.parseInviteToken(userId, token.token()));
	}

	//모임계좌 멤버 조회

	//모임계좌 상세보기

	//정산 요청하기

	@ApiOperation(value = "[JWT] 정산 요청하기", notes = "정산 요청하기")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "정산 요청하기 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다"),
		@ApiResponse(code = 500, message = "서버 내부 오류이 발생한 경우", response = ErrorResponse.class)
	})
	@PostMapping("/settle")
	public SuccessNonDataResponse sendSettlementRequest(
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId,
		@ApiParam(value = "정산 요청 정보", required = true) @RequestBody SettlementRequestDTO requestDTO) {

		groupAccountService.sendSettlementRequest(userId, requestDTO);

		return SuccessNonDataResponse.success(SuccessCode.SETTLE_GROUP_ACCOUNT_SUCCESS);
	}
	//1/n 송금하기
}
