package org.scoula.common.typehandler.account;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.domain.account.AccountType;

@MappedTypes(AccountType.class)
public class AccountTypeTypeHandler extends EnumTypeHandler<AccountType> {
	public AccountTypeTypeHandler(Class<AccountType> type) {
		super(type);
	}
}