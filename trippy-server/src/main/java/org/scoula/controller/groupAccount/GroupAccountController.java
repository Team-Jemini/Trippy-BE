package org.scoula.controller.groupAccount;

import java.util.List;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.groupAccount.dto.request.AcceptInviteTokenRequestDTO;
import org.scoula.controller.groupAccount.dto.request.GroupAccountCreateRequestDTO;
import org.scoula.controller.groupAccount.dto.request.GroupAccountJoinRequestDTO;
import org.scoula.controller.groupAccount.dto.request.InviteRequestDTO;
import org.scoula.controller.groupAccount.dto.request.SettlementRequestDTO;
import org.scoula.controller.groupAccount.dto.response.AcceptInviteResponseDTO;
import org.scoula.controller.groupAccount.dto.response.AccountTransactionResponseDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountCreateResponseDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountDetailResponseDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountMemberResponseDTO;
import org.scoula.controller.groupAccount.dto.response.InviteResponseDTO;
import org.scoula.service.groupaccount.GroupAccountService;
import org.scoula.service.groupaccount.InviteService;
import org.scoula.service.groupaccount.MemberService;
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
	private final MemberService memberService;

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
		@ApiResponse(code = 500, message = "서버 내부 오류입니다", response = ErrorResponse.class)
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
		@ApiResponse(code = 500, message = "서버 내부 오류입니다", response = ErrorResponse.class)
	})
	@GetMapping("/invite/token-info")
	public SuccessResponse<AcceptInviteResponseDTO> acceptInvite(
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId,
		@ApiParam(value = "초대 토큰", required = true) @RequestBody AcceptInviteTokenRequestDTO token) {

		return SuccessResponse.success(SuccessCode.PARSE_INVITE_TOKEN_SUCCESS,
			inviteService.parseInviteToken(userId, token.token()));
	}

	@ApiOperation(value = "[JWT]모임계좌 참여", notes = "모임계좌에 참여합니다.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "모임계좌에 가입 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다"),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다", response = ErrorResponse.class)
	})
	@PostMapping("/join")
	public SuccessNonDataResponse joinGroupAccount(
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId,
		@ApiParam(value = "모임계좌 ID", required = true) @RequestBody GroupAccountJoinRequestDTO request
	) {
		inviteService.joinGroupAccount(userId, request);
		return SuccessNonDataResponse.success(SuccessCode.JOIN_GROUP_ACCOUNT_SUCCESS);
	}

	@ApiOperation(value = "[JWT]계좌 상세 조회", notes = "계좌 상세 조회.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "계좌 상세 조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다"),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다", response = ErrorResponse.class)
	})
	@GetMapping("/detail")
	public SuccessResponse<GroupAccountDetailResponseDTO> getGroupAccountDetail(
		@ApiParam(value = "모임계좌 ID", required = true) @RequestParam String accountId,
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId
	) {
		return SuccessResponse.success(SuccessCode.GET_GROUP_ACCOUNT_DETAIL_SUCCESS,
			groupAccountService.getGroupAccountDetail(accountId, userId));
	}

	@ApiOperation(value = "모임 계좌 멤버조회", notes = "모임 계좌 멤버조회.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "모임 계좌 멤버조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다"),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다", response = ErrorResponse.class)
	})
	@GetMapping("/members")
	public SuccessResponse<List<GroupAccountMemberResponseDTO>> getGroupAccountMembers(
		@ApiParam(value = "모임계좌 ID", required = true) @RequestParam String accountId
	) {
		return SuccessResponse.success(SuccessCode.FIND_GROUP_ACCOUNT_MEMBER_SUCCESS,
			memberService.getGroupAccountMembers(accountId));
	}

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

	@ApiOperation(value = "[JWT]계좌 거래 내역 조회", notes = "계좌 거래 내역 조회.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "계좌 거래 내역 조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다"),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다", response = ErrorResponse.class)
	})
	@GetMapping("/transactions")
	public SuccessResponse<List<AccountTransactionResponseDTO>> filterAccountTransactions(
		@ApiParam(value = "개인 계좌 ID", required = true) @RequestParam String accountId,
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId,
		@ApiParam(value = "거래 타입", required = true) @RequestParam String transactionType
	) {
		return SuccessResponse.success(SuccessCode.FILTER_ACCOUNT_TRANSACTION_SUCCESS,
			groupAccountService.filterAccountTransactions(accountId, userId, transactionType));
	}
	//1/n 송금하기
}
