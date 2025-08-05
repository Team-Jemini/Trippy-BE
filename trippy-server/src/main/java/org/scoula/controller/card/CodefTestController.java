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

	@PostMapping("/cards")
	public String getMyCards() {
		try {
			return cardService.getMyCards();
		} catch (Exception e) {
			e.printStackTrace();
			return "에러 발생: " + e.getMessage();
		}
	}
}
