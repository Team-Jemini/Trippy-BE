package org.scoula.controller.voucher.dto.response;

import org.scoula.common.util.DateFormatUtil;
import org.scoula.domain.voucher.AccommodationVO;

public record AccommodationInfo(
	String accommodationId,
	String accommodationName,
	String roomName,
	String nights,
	String checkInDate,
	String checkOutDate

) {
	public static AccommodationInfo from(AccommodationVO vo) {
		return new AccommodationInfo(
			vo.getAccommodationId(),
			vo.getAccommodationName(),
			vo.getRoomName(),
			String.valueOf(vo.getReservationEndDate().toEpochDay() - vo.getReservationStartDate().toEpochDay())+"박",
			DateFormatUtil.formatDateTime(vo.getReservationStartDate(), vo.getCheckIn()),
			DateFormatUtil.formatDateTime(vo.getReservationEndDate(), vo.getCheckOut())
		);	}
}