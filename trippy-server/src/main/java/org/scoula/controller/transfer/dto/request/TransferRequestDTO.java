package org.scoula.controller.transfer.dto.request;

public record TransferRequestDTO(
    String fromAccountId,
    String toAccountId,
    Long amount,
    String currencyCode,
    String title
) {}
