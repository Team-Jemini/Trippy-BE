package org.scoula.controller.card;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import org.scoula.common.dto.SuccessNonDataResponse;
import org.scoula.common.dto.ErrorResponse;
import org.scoula.common.exception.enums.SuccessCode;
import org.scoula.config.resolver.UserId;
import org.scoula.service.card.CardCommandService;
import org.springframework.web.bind.annotation.*;


@Api(tags = "Payment", description = "카드, 결제 기능을 관리합니다.")
@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardCommandController {

	private final CardCommandService cardCommandService;

	@ApiOperation(value = "[JWT]카드 삭제", notes = "카드 ID로 카드를 삭제하는 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "카드 삭제 성공", response = SuccessNonDataResponse.class),
		@ApiResponse(code = 404, message = "존재하지 않는 카드입니다.", response = ErrorResponse.class)
	})
	@DeleteMapping("/{cardId}")
	public SuccessNonDataResponse deleteCard(
		@ApiIgnore @UserId Long userId,
		@ApiParam(value = "삭제할 카드 ID", required = true, example = "10") @PathVariable Long cardId
	) {
		cardCommandService.deleteCard(cardId);
		return SuccessNonDataResponse.success(SuccessCode.DELETE_CARD_SUCCESS);
	}

	@ApiOperation(value = "[JWT]카드 별명 수정", notes = "카드에 등록된 별명을 수정합니다.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "별명 수정 성공", response = SuccessNonDataResponse.class),
		@ApiResponse(code = 404, message = "카드가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@PutMapping("/{cardId}/nickname")
	public SuccessNonDataResponse updateCardNickname(
		@ApiIgnore @UserId Long userId,
		@ApiParam(value = "카드 ID", required = true, example = "10") @PathVariable Long cardId,
		@ApiParam(value = "새 카드 별명", required = true, example = "강병현의 포인트카드") @RequestParam String cardNickname
	) {
		cardCommandService.updateCardNickname(cardId, cardNickname);
		return SuccessNonDataResponse.success(SuccessCode.UPDATE_CARD_NICKNAME_SUCCESS);
	}


	@ApiOperation(value = "[JWT]주카드 설정", notes = "해당 카드 ID를 주카드로 설정하는 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "주카드 설정 성공", response = SuccessNonDataResponse.class),
		@ApiResponse(code = 404, message = "카드가 존재하지 않습니다.", response = ErrorResponse.class)
	})
	@PutMapping("/{cardId}/main")
	public SuccessNonDataResponse setMainCard(
		@ApiIgnore @UserId Long userId,
		@ApiParam(value = "주카드로 설정할 카드 ID", required = true, example = "10")
		@PathVariable Long cardId
	) {
		cardCommandService.setMainCard(cardId);
		return SuccessNonDataResponse.success(SuccessCode.SET_MAIN_CARD_SUCCESS);
	}

}
