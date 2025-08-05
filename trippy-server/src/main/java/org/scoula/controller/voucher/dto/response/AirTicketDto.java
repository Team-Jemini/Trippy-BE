package org.scoula.controller.voucher.dto.response;

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
}