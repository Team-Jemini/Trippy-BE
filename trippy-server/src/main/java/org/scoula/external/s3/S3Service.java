package org.scoula.external.s3;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {
	private final static String AIT_TICKET_PATH = "AIR_TICKET";
	private final static String SIGHTSEEING_PATH = "SIGHTSEEING";
	private final static String CARD_PATH = "CARD";
	private final static String TRAVEL_LOG_PATH = "TRAVEL_LOG";
	private final static int IMAGE_URL_PREFIX_LENGTH = 41;
	private final AmazonS3 amazonS3;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;


	public String uploadAirTicketImage(MultipartFile multipartFile) {
		return uploadImage(multipartFile, AIT_TICKET_PATH);
	}

	public String uploadSightSeeingImage(MultipartFile multipartFile) {
		return uploadImage(multipartFile, SIGHTSEEING_PATH);
	}

	public String uploadCardImage(MultipartFile multipartFile) {
		return uploadImage(multipartFile, CARD_PATH);
	}

	public String uploadTravelLogImage(MultipartFile multipartFile) {
		return uploadImage(multipartFile, TRAVEL_LOG_PATH);
	}

	private String uploadImage(MultipartFile multipartFile, String path) {
		String fileName = createFileName(multipartFile.getOriginalFilename());
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(multipartFile.getSize());
		objectMetadata.setContentType(multipartFile.getContentType());

		try (InputStream inputStream = multipartFile.getInputStream()) {
			amazonS3.putObject(new PutObjectRequest(bucket + "/" + path, fileName, inputStream, objectMetadata));
			return amazonS3.getUrl(bucket + "/" + path, fileName).toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteS3Image(final String imageUrl) {
		final String imageKey = getImageUrlToKey(imageUrl);
		amazonS3.deleteObject(bucket, imageKey);
	}

	private String getImageUrlToKey(final String imageUrl) {
		return imageUrl.substring(IMAGE_URL_PREFIX_LENGTH + bucket.length());
	}

	private String createFileName(String fileName) {
		return UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}

	private String getFileExtension(String fileName) {
		if (fileName.isEmpty())
			throw new IllegalArgumentException();

		ArrayList<String> fileValidate = new ArrayList<>();
		fileValidate.add(".jpg");
		fileValidate.add(".jpeg");
		fileValidate.add(".png");
		fileValidate.add(".JPG");
		fileValidate.add(".JPEG");
		fileValidate.add(".PNG");
		fileValidate.add(".HEIC");
		fileValidate.add(".heic");

		String idxFileName = fileName.substring(fileName.lastIndexOf("."));
		if (!fileValidate.contains(idxFileName))
			throw new IllegalArgumentException();

		return fileName.substring(fileName.lastIndexOf("."));
	}
}
