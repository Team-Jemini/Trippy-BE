package org.scoula.controller.voucher.dto.response;

import org.scoula.common.util.DateFormatUtil;
import org.scoula.domain.voucher.SightseeingVO;

public record SightSeeingInfo(
	String name,
	String viewingDate,
	String voucherImg
) {
	public static SightSeeingInfo from(SightseeingVO vo) {
		return new SightSeeingInfo(
			vo.getName(),
			DateFormatUtil.formatDateTime(vo.getViewingDate()),
			vo.getVoucherImg()
		);
	}
}