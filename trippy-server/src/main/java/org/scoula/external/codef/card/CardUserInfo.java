package org.scoula.external.codef.card;

public record CardUserInfo (
	String loginId,
	String password,
	String birthDate,
	String organization
){}
