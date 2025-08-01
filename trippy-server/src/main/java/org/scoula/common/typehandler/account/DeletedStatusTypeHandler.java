package org.scoula.common.typehandler.account;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.domain.account.DeletedStatus;

@MappedTypes(DeletedStatus.class)
public class DeletedStatusTypeHandler extends EnumTypeHandler<DeletedStatus> {
	public DeletedStatusTypeHandler(Class<DeletedStatus> type) {
		super(type);
	}
}

