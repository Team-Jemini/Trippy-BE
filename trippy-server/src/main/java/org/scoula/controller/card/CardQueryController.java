package org.scoula.controller.card;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.scoula.controller.card.dto.response.CardDetailResponseDTO;
import org.scoula.controller.card.dto.response.CardSummaryResponseDTO;
import org.scoula.service.card.CardQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.scoula.common.dto.SuccessResponse;
import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.exception.enums.SuccessCode;


@RestController
@RequestMapping("/cards")
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
@Api(tags = "Payment")
@RequiredArgsConstructor
public class CardQueryController {

	private final CardQueryService cardQueryService;
	@ApiOperation(value = "카드 요약 목록 조회", notes = "/payment 화면에 필요한 카드 요약 정보 조회 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "카드 요약 목록 조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping("/summary")
	public SuccessResponse<List<CardSummaryResponseDTO>> getCardSummaries(
		@RequestParam Long userId
	) {
		List<CardSummaryResponseDTO> summaries = cardQueryService.getCardSummaries(userId);
		return SuccessResponse.success(SuccessCode.FIND_CODEF_CARD_QUERY_SUCCESS, summaries);
	}

	@ApiOperation(value = "카드 상세 목록 조회", notes = "/payment/settings 화면에 필요한 카드 상세 정보 조회 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "카드 상세 목록 조회 성공", response = SuccessResponse.class),
		@ApiResponse(code = 404, message = "해당 유저가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@GetMapping("/detail")
	public SuccessResponse<List<CardDetailResponseDTO>> getCardDetails(
		@RequestParam Long userId
	) {
		List<CardDetailResponseDTO> details = cardQueryService.getCardDetails(userId);
		return SuccessResponse.success(SuccessCode.FIND_CODEF_CARD_QUERY_SUCCESS, details);
	}
}