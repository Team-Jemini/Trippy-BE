package org.scoula.controller.card;

import org.scoula.external.codef.card.CodefCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/codef")
public class CodefTestController {

	private final CodefCardService cardService;

	@Autowired
	public CodefTestController(CodefCardService cardService) {
		this.cardService = cardService;
	}

	// 💡 userId와 accountId를 입력받아 카드 저장
	@PostMapping(value = "/cards", produces = "application/json; charset=UTF-8")
	public String getAllCardsAndSave(
		@RequestParam Long userId,
		@RequestParam String accountId
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
