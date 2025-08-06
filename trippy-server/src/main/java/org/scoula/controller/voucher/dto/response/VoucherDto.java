package org.scoula.controller.voucher.dto.response;

import java.util.List;

public record VoucherDto(
	List<AccommodationInfo> accommodation,
	List<SightSeeingInfo> sightSeeing
) {
}
