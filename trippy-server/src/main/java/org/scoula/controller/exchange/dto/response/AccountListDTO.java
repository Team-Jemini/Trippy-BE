package org.scoula.controller.exchange.dto.response;

public record AccountListDTO ( String accountId,
                                String accountName,
                                Long balance,
                                String accountForeign,
                               boolean isDeleted) {
    public static AccountListDTO from(
            String accountId,
            String accountName,
            Long balance,
            String accountForeign,
            boolean isDeleted
    ) {
        return new AccountListDTO(accountId, accountName, balance, accountForeign, isDeleted);
    }
}
