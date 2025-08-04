package org.scoula.service.voucher;

import java.util.List;

import org.scoula.controller.voucher.dto.response.AirTicketDto;
import org.scoula.controller.voucher.dto.response.AirportInfo;
import org.scoula.mapper.voucher.AirTicketMapper;
import org.scoula.service.user.UserService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AirTicketService {

	private final AirTicketMapper airTicketMapper;
	private final UserService userService;

	/***
	 * 항공권 전체 조회
	 * - 최신순으로 정렬 조회
	 * @param userId
	 * @return AirTicketDto
	 */
	public List<AirTicketDto> getAirTicket(final Long userId) {
		userService.validateUserExists(userId);

		return airTicketMapper.findAllByUserIdOrderByDepartureDateDesc(userId).stream()
			.map(vo -> new AirTicketDto(
				vo.getAirlineId(),
				vo.getReservationCode(),
				vo.getDepartureDate(),
				new AirportInfo(vo.getDepartureCity(), vo.getDepartureAirport(), vo.getDepartureTime()),
				new AirportInfo(vo.getArrivalCity(), vo.getArrivalAirport(), vo.getArrivalTime()),
				vo.getFlightNumber(),
				vo.getBaggage(),
				vo.getSeatClass().getValue()
			))
			.toList();
	}
}
