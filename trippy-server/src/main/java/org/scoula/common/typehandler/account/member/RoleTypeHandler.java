package org.scoula.common.typehandler.account.member;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.domain.account.member.Role;

@MappedTypes(Role.class)
public class RoleTypeHandler extends EnumTypeHandler<Role> {
	public RoleTypeHandler(Class<Role> type) {
		super(type);
	}
}
