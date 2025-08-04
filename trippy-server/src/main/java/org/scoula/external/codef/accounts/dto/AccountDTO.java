package org.scoula.external.codef.accounts.dto;

public record AccountDTO (
        String resAccount, // 계좌번호
        String resAccountBalance, // 잔액
        String resAccountCurrency, // 통화 코드
        String resAccountName, // 계좌명
        String resAccountDisplay, // 표시용 계좌번호
        String resAccountEndDate // 만기일
) {

}
