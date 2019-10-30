package com.epam.tmpl.service.exception;

@SuppressWarnings("serial")
public class ImageStoreServiceValidatorException extends Exception {

	public ImageStoreServiceValidatorException() {
		super();
	}

	public ImageStoreServiceValidatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImageStoreServiceValidatorException(String message) {
		super(message);
	}

	public ImageStoreServiceValidatorException(Throwable cause) {
		super(cause);
	}

}
