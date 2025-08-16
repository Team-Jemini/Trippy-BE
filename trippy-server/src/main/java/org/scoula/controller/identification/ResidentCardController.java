package org.scoula.controller.identification;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.scoula.external.naver.identification.NaverOcrService;
import org.scoula.external.naver.identification.dto.IdCardDTO;
import springfox.documentation.annotations.ApiIgnore;

import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.config.resolver.UserId;
import org.scoula.controller.identification.dto.req.ResidentCardReq;
import org.scoula.controller.identification.dto.res.ResidentCardDTO;
import org.scoula.controller.identification.dto.res.ResidentCardOcrDTO;
import org.scoula.external.codef.identification.OcrService;
import org.scoula.service.identification.ResidentCardService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api(tags = "Resident Card", description = "주민등록증 추가, 조회, OCR 기능을 관리합니다.")
public class ResidentCardController {

	private final NaverOcrService naverOcrService;
	private final ResidentCardService residentCardService;

	@ApiOperation(value = "[JWT] 주민등록증 OCR", notes = "주민등록증 OCR을 하는 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "주민등록증 OCR 성공했습니다.", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다."),
		@ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
	})
	@PostMapping("/ocr")
	public SuccessResponse<IdCardDTO> extractResidentCardOcrInfo(
		@ApiIgnore @UserId Long userId,
		@ApiParam(value = "주민등록증", required = true)
		@RequestPart("file") MultipartFile file) {
		// TODO: userId 검증
		return SuccessResponse.success(SuccessCode.RESIDENT_CARD_OCR_SUCCESS, naverOcrService.callOCRApi(file));
	}

	@ApiOperation(value = "[JWT] 주민등록증 조회", notes = "주민등록증 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "주민등록증 조회에 성공했습니다.", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다."),
		@ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
	})
	@GetMapping("/residentCard")
	public SuccessResponse<ResidentCardDTO> getResidentCardInfo(@ApiIgnore @UserId Long userId) {

		return SuccessResponse.success(SuccessCode.RESIDENT_CARD_SUCCESS,
			residentCardService.getResidentCardInfo(userId));
	}

	@ApiOperation(value = "[JWT] 주민등록증 추가", notes = "주민등록증 추가하는 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "주민등록증 추가에 성공했습니다.", response = SuccessResponse.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다."),
		@ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
	})
	@PostMapping("/residentCard")
	public SuccessResponse<Integer> addResidentCardInfo(
		@ApiIgnore @UserId Long userId,
		@ApiParam(value = "주민등록증 정보", required = true) @RequestBody ResidentCardReq residentCardReq) {
		return SuccessResponse.success(SuccessCode.RESIDENT_CARD_ADD_SUCCESS,
			residentCardService.addResidentCardInfo(userId, residentCardReq));
	}
}
