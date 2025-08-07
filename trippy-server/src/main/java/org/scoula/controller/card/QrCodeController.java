
package org.scoula.controller.card;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.scoula.service.card.QrCodeService;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.scoula.controller.card.dto.response.QrCodeResponseDTO;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
@Api(tags = "Payment")
public class QrCodeController {

	private final QrCodeService qrCodeService;

	@ApiOperation(value = "QR코드 생성 및 활성화", notes = "비밀번호 인증 성공 시 호출되어 모든 카드의 QR코드를 생성하고 3분간 활성화합니다.")
	@PostMapping("/qr/activate")
	public SuccessResponse<List<QrCodeResponseDTO>> activateQrCodes(
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId
	) {
		List<QrCodeResponseDTO> qrCodes = qrCodeService.activateAndGenerateQrCodes(userId);
		return SuccessResponse.success(SuccessCode.QR_CODE_SUCCESS, qrCodes);
	}
}