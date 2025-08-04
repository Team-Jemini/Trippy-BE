package org.scoula.controller.card;

import org.scoula.external.codef.card.CodefCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/codef")
public class CodefTestController {

	private final CodefCardService cardService;

	@Autowired  // ✅ 또는 생성자에 @Autowired 생략 가능
	public CodefTestController(CodefCardService cardService) {
		this.cardService = cardService;
	}

	@PostMapping("/cards")
	public String getMyCards(
		@RequestParam String loginId,
		@RequestParam String password,
		@RequestParam String birthDate,
		@RequestParam String organization
	) {
		try {
			return cardService.getMyCards(loginId, password, birthDate, organization);
		} catch (Exception e) {
			e.printStackTrace();
			return "에러 발생: " + e.getMessage();
		}
	}
}
