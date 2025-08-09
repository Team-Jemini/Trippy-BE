package org.scoula.domain.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.scoula.domain.BaseTime;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ForeignAccountBalanceVO extends BaseTime {
    private Long balanceId;
    private String userId;
    private String accountId;
    private String currencyCode;
    private Double balance;
}
