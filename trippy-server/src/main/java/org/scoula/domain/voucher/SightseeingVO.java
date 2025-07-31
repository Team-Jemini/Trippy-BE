package org.scoula.domain.voucher;

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
public class SightseeingVO extends BaseTime {
	private Long voucherId;
	private Long userId;
	private String voucherImage;
	private String voucherName;
	private LocalDateTime voucherDate;
}
