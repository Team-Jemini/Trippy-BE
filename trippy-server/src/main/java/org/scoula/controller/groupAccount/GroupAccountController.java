package org.scoula.controller.groupAccount;

import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.groupAccount.dto.request.GroupAccountCreateRequestDTO;
import org.scoula.controller.groupAccount.dto.response.GroupAccountCreateResponseDTO;
import org.scoula.service.groupAccount.GroupAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/group-account")
public class GroupAccountController {

	final GroupAccountService service;

	//모임계좌 생성 및 모임주 저장
	@PostMapping("/create")
	public SuccessResponse<GroupAccountCreateResponseDTO> createGroupAccount(
		@RequestBody GroupAccountCreateRequestDTO requestDTO) {
		Long userId = 123L;// 토큰에서 userId 구하기
		GroupAccountCreateResponseDTO response = service.createGroupAccount(requestDTO, userId);

		return SuccessResponse.success(SuccessCode.CREATE_GROUP_ACCOUNT_SUCCESS, response);
	}
	//모임계좌 멤버

	//모임계좌 멤버 초대

	//모임계좌 초대링크 생성

	//모임게좌 멤버 조회

	//모임계좌 상세보기

	//정산 요청하기

	//1/n 송금하기
}
