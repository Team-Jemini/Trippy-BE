package org.scoula.common.typehandler.voucher;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.domain.voucher.SeatClass;

@MappedTypes(SeatClass.class)
public class SeatClassTypeHandler extends EnumTypeHandler<SeatClass> {
	public SeatClassTypeHandler(Class<SeatClass> type) {
		super(type);
	}
}
