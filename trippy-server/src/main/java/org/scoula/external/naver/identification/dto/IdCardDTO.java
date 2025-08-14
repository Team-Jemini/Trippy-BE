package org.scoula.external.naver.identification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IdCardDTO {
    private final String name;
    private final String personalNum;
    private final String address;
    private final String issueDate; // yyyy-MM-dd
    private final String authority;
}