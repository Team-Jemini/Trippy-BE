package org.scoula.controller.voucher.dto.response;

import org.scoula.common.util.DateFormatUtil;
import org.scoula.domain.voucher.AccommodationVO;

public record AccommodationDetailDto(
	String accommodationId,
	String checkInDate,
	String checkOutDate,
	String userName,
	String accommodationName,
	String address,
	String contact
) {
	public static AccommodationDetailDto from(AccommodationVO vo, String userName) {
		return new AccommodationDetailDto(
			vo.getAccommodationId(),
			DateFormatUtil.formatDateTime(vo.getReservationStartDate(), vo.getCheckIn()),
			DateFormatUtil.formatDateTime(vo.getReservationEndDate(), vo.getCheckOut()),
			userName,
			vo.getAccommodationName(),
			vo.getAddress(),
			vo.getContact()
		);
	}
}
