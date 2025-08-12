
package org.scoula.controller.card;

import java.util.List;

import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.config.resolver.UserId;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.scoula.service.card.QrCodeService;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import org.scoula.controller.card.dto.response.QrCodeResponseDTO;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Payment", description = "카드, 결제 기능을 관리합니다.")
@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class QrCodeController {

	private final QrCodeService qrCodeService;

	@ApiOperation(value = "[JWT] QR코드 생성 및 활성화", notes = "비밀번호 인증 성공 시 호출되어 모든 카드의 QR코드를 생성하고 3분간 활성화합니다.")
	@PostMapping("/qr/activate")
	public SuccessResponse<List<QrCodeResponseDTO>> activateQrCodes(
		@ApiIgnore @UserId Long userId
	) {
		return SuccessResponse.success(SuccessCode.QR_CODE_SUCCESS, qrCodeService.activateAndGenerateQrCodes(userId));
	}
}