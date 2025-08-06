package org.scoula.controller.voucher.dto.response;

import org.scoula.domain.voucher.AirTicketVO;

public record AirTicketDto(
	Long airlineId,
	String reservationCode,
	String departureDate,
	AirportInfo departure,
	AirportInfo arrival,
	String flightNumber,
	String baggage,
	String seatClass
) {
	public static AirTicketDto from(AirTicketVO vo) {
		return new AirTicketDto(
			vo.getAirlineId(),
			vo.getReservationCode(),
			vo.getDepartureDate(),
			new AirportInfo(vo.getDepartureCity(), vo.getDepartureAirport(), vo.getDepartureTime()),
			new AirportInfo(vo.getArrivalCity(), vo.getArrivalAirport(), vo.getArrivalTime()),
			vo.getFlightNumber(),
			vo.getBaggage(),
			vo.getSeatClass().getValue()
		);
	}
}