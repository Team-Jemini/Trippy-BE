package org.scoula.controller.groupAccount;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.groupAccount.dto.request.GroupAccountCreateRequestDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountCreateResponseDTO;
import org.scoula.service.groupaccount.GroupAccountService;
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

	final GroupAccountService service;

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
			service.createGroupAccount(request, userId));
	}

	//모임계좌 멤버 조회

	//모임계좌 상세보기

	//정산 요청하기

	//1/n 송금하기
}
