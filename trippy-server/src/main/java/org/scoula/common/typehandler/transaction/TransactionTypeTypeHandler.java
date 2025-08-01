package org.scoula.common.typehandler.transaction;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.domain.transaction.TransactionType;

@MappedTypes(TransactionType.class)
public class TransactionTypeTypeHandler extends EnumTypeHandler<TransactionType> {
	public TransactionTypeTypeHandler(Class<TransactionType> type) {
		super(type);
	}
}
