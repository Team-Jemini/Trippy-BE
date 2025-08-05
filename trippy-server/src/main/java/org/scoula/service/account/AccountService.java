package org.scoula.service.account;

import org.scoula.controller.account.dto.response.AccountDTO;
import org.scoula.domain.account.AccountVO;
import org.scoula.mapper.account.AccountMapper;
import org.scoula.service.user.UserService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountMapper accountMapper;
    private final UserService userService;

    public List<AccountDTO> getAccountsList(final Long userId) {
        userService.validateUserExists(userId);

        return accountMapper.findAllByUserIdOrderByUpdatedAt(userId).stream()
                .map(vo -> new AccountDTO(
                        vo.getUserId(),
                        vo.getAccountId(),
                        vo.getAccountName(),
                        vo.getAccountType(),
                        vo.getOwnerId(),
                        vo.getBalance(),
                        vo.getAccountCurrency(),
                        vo.getIsDeleted()
                ))
                .toList();
    }
}
