package org.scoula.controller.travel.log.dto.req;

import java.util.List;

public record TravelLogTransactionListDTO(
	Long travelId,
	Long todayAmount,
	Long totalAmount,
	List<TravelLogTransactionDTO> travelLogTransactionDTOS
) {
}
