package org.scoula.common.typehandler.exchange;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.domain.exchange.ExchangeType;

@MappedTypes(ExchangeType.class)
public class ExchangeTypeTypeHandler extends EnumTypeHandler<ExchangeType> {
	public ExchangeTypeTypeHandler(Class<ExchangeType> type) {
		super(type);
	}
}