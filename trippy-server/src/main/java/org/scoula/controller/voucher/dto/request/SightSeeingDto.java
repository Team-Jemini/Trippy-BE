package org.scoula.controller.voucher.dto.request;

import java.time.LocalDateTime;

public record SightSeeingDto(
	String name,
	LocalDateTime viewingDate
) {
}
