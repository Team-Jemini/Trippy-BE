package org.scoula.controller.voucher.dto.response;

import org.scoula.domain.voucher.AirTicketVO;

public record AirTicketDetailDto(
	Long airlineId,
	String reservationCode,
	String departureDate,
	AirportInfo departure,
	AirportInfo arrival,
	String terminal,
	String gate,
	String baggageWeight,
	String seat,
	String qrImg
) {

	public static AirTicketDetailDto from(AirTicketVO vo) {
		return new AirTicketDetailDto(
			vo.getAirlineId(),
			vo.getReservationCode(),
			vo.getDepartureDate(),
			new AirportInfo(vo.getDepartureCity(), vo.getDepartureAirport(), vo.getDepartureTime()),
			new AirportInfo(vo.getArrivalCity(), vo.getArrivalAirport(), vo.getArrivalTime()),
			vo.getTerminal(),
			vo.getGate(),
			vo.getBaggageWeight(),
			vo.getSeat(),
			vo.getQrImg()
		);
	}
}
