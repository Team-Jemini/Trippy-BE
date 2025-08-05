package org.scoula.controller.card;

import java.util.List;

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
	public List<String> getAllCards() {
		try {
			return cardService.getAllMyCards();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of("에러 발생: " + e.getMessage());
		}
	}
}

