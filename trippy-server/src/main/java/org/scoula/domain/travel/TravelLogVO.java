package org.scoula.domain.travel;

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
public class TravelLogVO extends BaseTime {
	private Long travelId;
	private Long userId;
	private String travelName;
	private LocalDateTime travelBegin;
	private LocalDateTime travelEnd;
	private String travelDestination;
	private Boolean reportGenerated;
	private String travelImg;
}