package org.scoula.controller.groupAccount.dto.response;

import java.time.LocalDate;
import java.util.List;

public record DailyAccountTransactionDTO(
	LocalDate date,
	List<AccountTransactionResponseDTO> transactions
) {
}
