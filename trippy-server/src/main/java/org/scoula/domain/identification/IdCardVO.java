package org.scoula.domain.identification;

import lombok.*;
import org.scoula.domain.BaseTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class IdCardVO extends BaseTime {
	private Long userId;
	private String name;
	private String idCardNum;
	private String address;
	private String idCardDate;
	private String imgUrl;
}