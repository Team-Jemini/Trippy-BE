package org.scoula.common.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.scoula.external.sms.SmsService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsUtil {
	private static final String VERIFICATION_MESSAGE_FORM = "[%s] trippy에서 보낸 인증번호입니다.";
	private static final int VERIFICATION_EXPIRE_TIME = 3;
	private final SmsService smsService;
	private final RedisTemplate<String, String> redisTemplate;

	public boolean sendVerificationCode(final String to, final String verificationCode) throws IOException {
		final String verificationCodeMessage = String.format(VERIFICATION_MESSAGE_FORM, verificationCode);
		return smsService.sendSms(to, verificationCodeMessage);
	}

	public void saveVerificationCode(final String to, final String verificationCode) {
		redisTemplate.opsForValue().set(to, verificationCode, VERIFICATION_EXPIRE_TIME, TimeUnit.MINUTES);
	}

	public boolean isVerificationCode(final String to) {
		return redisTemplate.opsForValue().get(to) != null;
	}

	public String getVerificationCode(final String to) {
		return redisTemplate.opsForValue().get(to);
	}

	public void deleteVerificationCode(final String to) {
		redisTemplate.delete(to);
	}

}

