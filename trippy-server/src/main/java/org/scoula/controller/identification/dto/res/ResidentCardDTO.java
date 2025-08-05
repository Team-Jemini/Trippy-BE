package org.scoula.controller.identification.dto.res;

public record ResidentCardDTO(
        String resUserName,
        String resIssueDate,
        String resUserIdentity,
        String address,
        String QrUrl
) {}
