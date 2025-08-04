package org.scoula.external.codef.identification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OcrResponseDTO {
    private String resIdCardType;
    private String resUserName;
    private String resIdCard;
    private String resIssueDate;
    private String resUserIdentity;
}