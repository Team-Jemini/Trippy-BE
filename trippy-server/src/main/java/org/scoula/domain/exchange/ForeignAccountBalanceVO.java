package org.scoula.domain.exchange;

import lombok.Data;
import org.scoula.domain.BaseTime;

@Data
public class ForeignAccountBalanceVO extends BaseTime {
    private Long balanceId;
    private String userId;
    private String accountId;
    private String currencyCode;
    private Double balance;

}
