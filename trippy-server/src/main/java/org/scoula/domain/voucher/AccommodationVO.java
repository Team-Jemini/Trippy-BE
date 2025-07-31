package org.scoula.domain.voucher;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class AccommodationVO extends BaseTime {
	private String reservationCode;
	private Long userId;
	private String reservationName;
	private String roomName;
	private LocalDate reservationStartDate;
	private LocalDate reservationEndDate;
	private LocalDateTime checkIn;
	private LocalDateTime checkOut;
}
