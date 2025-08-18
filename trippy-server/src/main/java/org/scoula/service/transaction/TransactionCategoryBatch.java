package org.scoula.service.transaction;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransactionCategoryBatch {

	private final TransactionService transactionService;

	// 앱 시작 10초 후부터 60초 간격
	// @Scheduled(initialDelay = 10_000L, fixedDelay = 60_000L)
	public void fillMissingCategories() {
		int updated = transactionService.classifyAndSaveMissingCategories(100); // 1회 최대 100건
		log.info("Category batch updated {} rows", updated);
	}
}
