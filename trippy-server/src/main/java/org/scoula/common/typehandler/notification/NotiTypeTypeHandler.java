package org.scoula.common.typehandler.notification;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.domain.notification.NotiType;

@MappedTypes(NotiType.class)
public class NotiTypeTypeHandler extends EnumTypeHandler<NotiType> {
	public NotiTypeTypeHandler(Class<NotiType> type) {
		super(type);
	}
}