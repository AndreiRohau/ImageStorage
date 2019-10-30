package com.epam.tmpl.web.converter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

import com.epam.tmpl.dao.domain.Image;

/**
 * Converter is used to convert MultipartFile files from Client side to Image
 * object on the Back
 * 
 * @author Andrei_Rohau
 */
public class MultipartFileToImageConverter implements Converter<MultipartFile, Image> {

	/*
	 * Will convert only files, having content type starting with word [image]
	 */
	private static final String IMAGE_MATCHER_REGEX = "^image.*$";

	@Override
	public Image convert(MultipartFile uploadingImage) throws ConversionException {
		try {
			validateFileOfTypeImage(uploadingImage);
			return new Image(uploadingImage.getOriginalFilename(),
						Base64.getEncoder().encodeToString(uploadingImage.getBytes()));
		} catch (IOException e) {
			throw new ConversionNotSupportedException(uploadingImage, Image.class, e);
		}
	}
	
	private void validateFileOfTypeImage(MultipartFile uploadingImage) throws ConversionException {
		if (!uploadingImage.getContentType().matches(IMAGE_MATCHER_REGEX)) {
			throw new ConversionNotSupportedException(uploadingImage, Image.class, null);
		} 
	}

}
