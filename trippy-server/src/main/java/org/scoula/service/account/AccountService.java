package org.scoula.service.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.scoula.common.exception.model.ServerErrorException;
import org.scoula.domain.account.AccountType;
import org.scoula.domain.account.AccountVO;
import org.scoula.domain.account.DeletedStatus;
import org.scoula.external.codef.accounts.service.CodefAccountService;
import org.scoula.mapper.account.AccountMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import static org.scoula.common.exception.enums.ErrorCode.SAVE_ACCOUNTS_LIST_FAILED;

@Log4j2
@Service
@RequiredArgsConstructor
public class AccountService {

    private final CodefAccountService codefAccountService;
    private final AccountMapper accountMapper;

    public void saveAccounts(final Long userId) {
        try {
            String accountListJson = codefAccountService.getAccountList();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(accountListJson, Map.class);

            List<Map<String, Object>> accountList = (List<Map<String, Object>>)
                    ((Map<String, Object>) responseMap.get("data")).get("resDepositTrust");

            for (Map<String, Object> account : accountList) {
                String accountId = (String) account.get("resAccount");
                if (accountMapper.existsByAccountId(accountId)) {
                    log.info("중복 계좌 건너뜀: {}", accountId);
                    continue;
                }

                // 잔액 데이터 String -> Long 타입으로 형 변환
                String balanceStr = (String) account.get("resAccountBalance");
                Long balance = 0L;
                if (balanceStr != null && !balanceStr.isEmpty()) {
                    balance = Long.parseLong(balanceStr);
                }

                AccountVO vo = AccountVO.builder()
                        .userId(userId)
                        .accountId((String) account.get("resAccount"))
                        .accountName((String) account.get("resAccountName"))
                        .accountType(AccountType.valueOf("person"))
                        .ownerId(userId)
                        .balance(balance)
                        .accountCurrency((String) account.get("resAccountCurrency"))
                        .isDeleted(DeletedStatus.N)
                        .build();

                accountMapper.saveAccount(vo);
            }

        } catch (Exception e) {
            throw new ServerErrorException(SAVE_ACCOUNTS_LIST_FAILED);
        }
    }

}
