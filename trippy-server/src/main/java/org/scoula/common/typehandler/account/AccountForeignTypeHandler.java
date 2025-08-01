package org.scoula.common.typehandler.account;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.domain.account.AccountForeign;

@MappedTypes(AccountForeign.class)
public class AccountForeignTypeHandler extends EnumTypeHandler<AccountForeign> {
	public AccountForeignTypeHandler(Class<AccountForeign> type) {
		super(type);
	}
}
