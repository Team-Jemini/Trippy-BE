package org.scoula.controller.transfer;

import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.config.resolver.UserId;
import org.scoula.controller.transfer.dto.request.GroupTransferRequestDTO;
import org.scoula.controller.transfer.dto.request.TransferRequestDTO;
import org.scoula.controller.transfer.dto.response.GroupTransferResponseDTO;
import org.scoula.controller.transfer.dto.response.TransferResponseDTO;
import org.scoula.service.transfer.TransferService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Transfer", description = "송금하기 기능을 관리합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferController {
	private final TransferService transferService;

	@ApiOperation(value = "[JWT]송금하기", notes = "송금하기 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "송금하기 성공", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "잘못된 계좌 번호가 요청되었습니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다.", response = ErrorResponse.class)
	})
	@PostMapping()
	public SuccessResponse<TransferResponseDTO> transfer(
		@ApiIgnore @UserId Long userId,
		@ApiParam(value = "송금 정보", required = true) @RequestBody TransferRequestDTO request) {
		return SuccessResponse.success(SuccessCode.TRANSFER_SUCCESS, transferService.transfer(userId, request));
	}

	@ApiOperation(value = "[JWT]모임계좌 송금하기", notes = "모임계좌 송금하기 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "모임계좌 송금하기 성공", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "잘못된 계좌 번호가 요청되었습니다.", response = ErrorResponse.class),
		@ApiResponse(code = 500, message = "서버 내부 오류입니다.", response = ErrorResponse.class)
	})
	@PostMapping("/group")
	public SuccessResponse<GroupTransferResponseDTO> groupTransfer(
		@ApiIgnore @UserId Long userId,
		@ApiParam(value = "송금 정보", required = true) @RequestBody GroupTransferRequestDTO request) {
		return SuccessResponse.success(SuccessCode.GROUP_TRANSFER_SUCCESS,
			transferService.groupTransfer(userId, request));
	}
}
