package org.scoula.controller.card;

import org.scoula.external.codef.card.CodefCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/test/codef")
@Api(tags = "CODEF 카드 API")
public class CodefTestController {

	private final CodefCardService cardService;

	@Autowired
	public CodefTestController(CodefCardService cardService) {
		this.cardService = cardService;
	}

	@ApiOperation(value = "CODEF 카드 불러오기 및 저장", notes = "CODEF로부터 카드 정보를 가져와 DB에 저장합니다.")
	@PostMapping(value = "/cards", produces = "application/json; charset=UTF-8")
	public String getAllCardsAndSave(
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId,
		@ApiParam(value = "계좌 ID", required = true) @RequestParam String accountId
	) {
		try {
			cardService.getAllMyCardsAndSave(userId, accountId);
			return "카드 정보 저장 완료";
		} catch (Exception e) {
			e.printStackTrace();
			return "❌ 에러 발생: " + e.getMessage();
		}
	}
}

