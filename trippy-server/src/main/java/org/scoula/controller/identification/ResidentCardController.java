package org.scoula.controller.identification;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.controller.identification.dto.ResidentCardDTO;
import org.scoula.external.codef.identification.OcrService;
import org.scoula.controller.identification.dto.ResidentCardOcrDTO;
import org.scoula.service.identification.ResidentCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api(tags = "Resident Card")
public class ResidentCardController {

    private final OcrService ocrService;
    private final ResidentCardService residentCardService;

    @ApiOperation(value = "[JWT] 주민등록증 OCR", notes = "주민등록증 OCR을 하는 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "주민등록증 OCR 성공했습니다.", response = SuccessResponse.class),
            @ApiResponse(code = 400, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @PostMapping("/ocr")
    public SuccessResponse<ResidentCardOcrDTO> extractResidentCardOcrInfo(
            @ApiParam(value = "주민등록증", required = true)
            @RequestParam("file") MultipartFile file) throws IOException {

        return SuccessResponse.success(SuccessCode.RESIDENT_CARD_OCR_SUCCESS, ocrService.callOCRApi(file));
    }


    @ApiOperation(value = "[JWT] 주민등록증 조회", notes = "주민등록증 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "주민등록증 조회에 성공했습니다.", response = SuccessResponse.class),
            @ApiResponse(code = 400, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @GetMapping("/residentCard")
    public SuccessResponse<ResidentCardDTO> getResidentCardInfo(@RequestHeader("X-USER-ID") Long userId){

        return SuccessResponse.success(SuccessCode.RESIDENT_CARD_SUCCESS, residentCardService.getResidentCardInfo(userId));
    }
}
