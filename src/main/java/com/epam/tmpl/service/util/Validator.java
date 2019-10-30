package com.epam.tmpl.service.util;

import java.util.List;

import com.epam.tmpl.dao.domain.Image;
import com.epam.tmpl.service.exception.ImageStoreServiceValidatorException;

/**
 * Validates incoming to Service layer data
 * 
 * @author Andrei_Rohau
 */
public class Validator {

	/**
	 * @param longNumber
	 *            parameter to validate
	 * @return true if valid
	 */
	public static void validate(Long longNumber) throws ImageStoreServiceValidatorException {
		if (null == longNumber || longNumber.longValue() == 0) {
			throw new ImageStoreServiceValidatorException("Long is null or zero");
		}
	}

	/**
	 * @param string
	 *            parameter to validate
	 * @return true if valid
	 */
	public static void validate(String string) throws ImageStoreServiceValidatorException {
		if (null == string || string.isEmpty()) {
			throw new ImageStoreServiceValidatorException("Image name is null or emplty");
		}
	}

	/**
	 * @param image
	 *            parameter to validate
	 * @return true if valid
	 */
	public static void validate(Image image) throws ImageStoreServiceValidatorException {
		if (image == null) {
			throw new ImageStoreServiceValidatorException("Image is null");
		}
	}

	/**
	 * @param images
	 *            parameter to validate
	 * @return true if valid
	 */
	public static void validate(List<Image> images) throws ImageStoreServiceValidatorException {
		if (images == null || images.size() == 0) {
			throw new ImageStoreServiceValidatorException("List of images is null or empty");
		}
	}
}
