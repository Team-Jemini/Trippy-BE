package org.scoula.common.util;

import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QrCodeActivationStore {
	private static final Map<Long, LocalDateTime> activatedCards = new ConcurrentHashMap<>();

	public static void activateCard(Long cardId) {
		activatedCards.put(cardId, LocalDateTime.now().plusMinutes(3));
	}

	public static boolean isActivated(Long cardId) {
		LocalDateTime expiry = activatedCards.get(cardId);
		return expiry != null && LocalDateTime.now().isBefore(expiry);
	}

	public static void deactivateCard(Long cardId) {
		activatedCards.remove(cardId);
	}

	public static void deactivateAll() {
		activatedCards.clear();
	}

	public static Map<Long, LocalDateTime> getAllActivations() {
		return activatedCards;
	}
}