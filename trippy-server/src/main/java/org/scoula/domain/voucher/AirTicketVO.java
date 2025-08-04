package org.scoula.domain.voucher;

import org.scoula.domain.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AirTicketVO extends BaseTime {
	private Long userId;
	private Long airlineId;
	private String airline;
	private String reservationCode;
	private String flightNumber;
	private String departureAirport;
	private String arrivalAirport;
	private String departureCity;
	private String arrivalCity;
	private String departureDate;
	private String departureTime;
	private String boardingTime;
	private String arrivalTime;
	private String terminal;
	private String gate;
	private String seat;
	private String baggage;
	private String passengerName;
	private String qrImg;
	private boolean used; //mybatis에서 isUsed를 못 쓰게 한다. 인식이 안된다나...
	private String ticketImg;
	private SeatClass seatClass;
}