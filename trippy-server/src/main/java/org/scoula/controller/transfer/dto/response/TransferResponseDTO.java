package org.scoula.controller.transfer.dto.response;

public record TransferResponseDTO(
        String fromAccountId,
        String toAccountId,
        Long amount,
        Long balance,
        String currencyCode
) {}
