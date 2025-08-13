package org.scoula.external.sms;

import java.io.IOException;

public interface SmsService {
	boolean sendSms(String to, String verificationCode) throws IOException;
}

