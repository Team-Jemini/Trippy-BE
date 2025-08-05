package org.scoula.controller.identification.dto.res;

public record ResidentCardOcrDTO(
        String resUserName,
        String resIssueDate,
        String resUserIdentity,
        String address
) {}
