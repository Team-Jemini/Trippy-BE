package org.scoula.common.typehandler.user;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.domain.user.Gender;

@MappedTypes(Gender.class)
public class GenderTypeHandler extends EnumTypeHandler<Gender> {
	public GenderTypeHandler(Class<Gender> type) {
		super(type);
	}
}