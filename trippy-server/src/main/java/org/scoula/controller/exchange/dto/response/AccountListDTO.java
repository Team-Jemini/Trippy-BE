package org.scoula.controller.exchange.dto.response;

import lombok.Data;

@Data
public class AccountListDTO {
    // setter 필요
    private String accountId;
    private String accountName;
    private Long balance;
    private String accountForeign;
    private boolean isDeleted;
}
