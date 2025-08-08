package org.scoula.service.card;

import lombok.RequiredArgsConstructor;

import org.scoula.common.exception.enums.ErrorCode;
import org.scoula.common.exception.model.ServerErrorException;
import org.scoula.domain.card.CardVO;
import org.scoula.mapper.card.CardMapper;
import org.scoula.common.util.QrCodeActivationStore;
import org.scoula.controller.card.dto.response.QrCodeResponseDTO;
import org.scoula.common.util.QrCodeGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QrCodeService {

	private final CardMapper cardMapper;

	public List<QrCodeResponseDTO> activateAndGenerateQrCodes(Long userId) {
		List<CardVO> cards = cardMapper.findByUserId(userId);

		return cards.stream().map(card -> {
			QrCodeActivationStore.activateCard(card.getCardId());
			try {
				String qrBase64 = QrCodeGenerator.generateBase64QrCode("CARD:" + card.getCardId());
				return new QrCodeResponseDTO(card.getCardId(), qrBase64);
			} catch (Exception e) {
				throw new ServerErrorException(ErrorCode.QR_CODE_GENERATION_FAILED);
			}
		}).collect(Collectors.toList());
	}
}