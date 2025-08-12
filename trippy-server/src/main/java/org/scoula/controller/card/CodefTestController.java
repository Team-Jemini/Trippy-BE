package org.scoula.controller.card;

import org.scoula.config.resolver.UserId;
import org.scoula.service.card.CodefCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.exception.enums.SuccessCode;

@Api(tags = "Payment", description = "카드, 결제 기능을 관리합니다.")
@RestController
@RequestMapping("/cards")
public class CodefTestController {

	private final CodefCardService cardService;

	@Autowired
	public CodefTestController(CodefCardService cardService) {
		this.cardService = cardService;
	}

	@ApiOperation(value = "[JWT] CODEF 카드 불러오기 및 저장", notes = "CODEF로부터 카드 정보를 가져와 DB에 저장합니다.")
	@PostMapping(value = "/codef", produces = "application/json; charset=UTF-8")
	public SuccessNonDataResponse getAllCardsAndSave(
		@ApiIgnore @UserId Long userId,
		@ApiParam(value = "계좌번호", required = true, example = "3333-02-123456") @RequestParam String accountId
	) {
		cardService.getAllMyCardsAndSave(userId, accountId);
		return SuccessNonDataResponse.success(SuccessCode.FIND_CODEF_CARD_SUCCESS);
	}
}

