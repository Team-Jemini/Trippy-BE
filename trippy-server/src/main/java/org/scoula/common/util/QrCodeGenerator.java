package org.scoula.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

public class QrCodeGenerator {

	public static String generateBase64QrCode(String content) throws Exception {
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 200, 200);

		BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < 200; x++) {
			for (int y = 0; y < 200; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
			}
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(image, "png", output);
		return Base64.getEncoder().encodeToString(output.toByteArray());
	}
}