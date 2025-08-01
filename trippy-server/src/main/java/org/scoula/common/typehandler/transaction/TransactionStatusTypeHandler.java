package org.scoula.common.typehandler.transaction;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.domain.transaction.TransactionStatus;

@MappedTypes(TransactionStatus.class)
public class TransactionStatusTypeHandler extends EnumTypeHandler<TransactionStatus> {
	public TransactionStatusTypeHandler(Class<TransactionStatus> type) {
		super(type);
	}
}
