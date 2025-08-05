package org.scoula.controller.card;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.scoula.controller.card.dto.response.CardDetailResponseDTO;
import org.scoula.controller.card.dto.response.CardSummaryResponseDTO;
import org.scoula.service.card.CardQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@Api(tags = "Payment")
@RequiredArgsConstructor
public class CardQueryController {

	private final CardQueryService cardQueryService;

	@ApiOperation(value = "카드 요약 목록 조회", notes = "/payment 화면에 필요한 정보 조회")
	@GetMapping("/summary")
	public List<CardSummaryResponseDTO> getCardSummaries(
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId) {
		return cardQueryService.getCardSummaries(userId);
	}

	@ApiOperation(value = "카드 상세 목록 조회", notes = "/payment/settings 화면에 필요한 정보 조회")
	@GetMapping("/detail")
	public List<CardDetailResponseDTO> getCardDetails(
		@ApiParam(value = "유저 ID", required = true) @RequestParam Long userId) {
		return cardQueryService.getCardDetails(userId);
	}
}
