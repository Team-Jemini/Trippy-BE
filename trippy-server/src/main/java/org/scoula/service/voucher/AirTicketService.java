package org.scoula.service.voucher;

import java.util.List;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.NotFoundException;
import org.scoula.controller.voucher.dto.response.AirTicketDetailDto;
import org.scoula.controller.voucher.dto.response.AirTicketDto;
import org.scoula.controller.voucher.dto.response.AirportInfo;
import org.scoula.domain.voucher.AirTicketVO;
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
			.map(AirTicketDto::from)
			.toList();
	}

	/***
	 * 항공권 상세 조회
	 * @param userId
	 * @param airLineId
	 * @return AirTicketDto
	 */
	public AirTicketDetailDto getAirTicketDetail(final Long userId, final Long airLineId) {
		userService.validateUserExists(userId);

		AirTicketVO airTicketVO = airTicketMapper.findById(airLineId);
		if (airTicketVO == null){
			throw new NotFoundException(ErrorCode.AIR_TICKET_NOT_FOUND_EXCEPTION);
		}

		return AirTicketDetailDto.from(airTicketVO);
	}

}
