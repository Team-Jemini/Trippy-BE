package org.scoula.controller.transfer.dto.request;

import org.scoula.domain.transaction.TransactionVO;

public record TransferRequestDTO(
        Long transactionId,
        String fromAccountId,
        String toAccountId,
        Long userId,
        Long amount,
        String currencyCode,
        String title
) {}
