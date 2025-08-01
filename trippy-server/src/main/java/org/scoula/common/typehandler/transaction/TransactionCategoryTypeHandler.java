package org.scoula.common.typehandler.transaction;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.domain.transaction.TransactionCategory;

@MappedTypes(TransactionCategory.class)
public class TransactionCategoryTypeHandler extends EnumTypeHandler<TransactionCategory> {
	public TransactionCategoryTypeHandler(Class<TransactionCategory> type) {
		super(type);
	}
}
