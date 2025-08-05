package org.scoula.external.codef.identification.dto;

public record OcrResponseDTO(
        String resIdCardType,
        String resUserName,
        String resIdCard,
        String resIssueDate,
        String resUserIdentity
) {}