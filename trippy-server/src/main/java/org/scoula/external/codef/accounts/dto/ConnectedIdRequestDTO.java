package org.scoula.external.codef.accounts.dto;

public record ConnectedIdRequestDTO (
        String countryCode,
        String businessType,
        String clientType,
        String organization,
        String loginType,
        String id,
        String password
) {}
