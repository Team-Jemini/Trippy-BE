package org.scoula.controller.identification;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.external.codef.identification.OcrService;
import org.scoula.controller.identification.dto.ResidentCardInquiryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api(tags = "신분증 관리")
public class ResidentCardController {

    @Autowired
    private final OcrService ocrService;

    @ApiOperation(value = "신분증 OCR", notes = "신분증 OCR을 하는 API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = ResidentCardInquiryDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청입니다."),
            @ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
    })
    @GetMapping("/ocr")
    public ResponseEntity<ResidentCardInquiryDTO> extractResidentCardInfo(
            @ApiParam(value = "신분증", required = true)
            @RequestParam("file") MultipartFile file) throws IOException {
        ResidentCardInquiryDTO residentCardInquiryDTO = ocrService.callOCRApi(file);

        return ResponseEntity.ok(residentCardInquiryDTO);
    }
}
