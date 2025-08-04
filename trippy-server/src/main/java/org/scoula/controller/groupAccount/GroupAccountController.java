package org.scoula.controller.groupAccount;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.groupAccount.dto.request.GroupAccountCreateRequestDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountCreateResponseDTO;
import org.scoula.service.groupaccount.GroupAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Api(tags = "Group Account")
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/group-account")
public class GroupAccountController {

	final GroupAccountService service;

	//모임계좌 생성 및 모임주 저장
	@ApiOperation(value = "모임계좌 생성", notes = "모임계좌 생성 및 모임주를 등록하는 API입니다.")
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
		@RequestHeader("Authorization") String accessToken,
		@RequestBody GroupAccountCreateRequestDTO request) {
		Long userId = 1L; // 토큰에서 userId 구하기

		GroupAccountCreateResponseDTO response = service.createGroupAccount(request, userId);

		return SuccessResponse.success(SuccessCode.CREATE_GROUP_ACCOUNT_SUCCESS, response);
	}

	//모임계좌 멤버 초대

	//모임계좌 멤버 조회

	//모임계좌 상세보기

	//정산 요청하기

	//1/n 송금하기
}
