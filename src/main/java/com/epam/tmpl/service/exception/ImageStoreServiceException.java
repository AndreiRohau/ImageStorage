package com.epam.tmpl.service.exception;

/**
 * Exception to wrap the Service layer
 * 
 * @author Andrei_Rohau
 */
@SuppressWarnings("serial")
public class ImageStoreServiceException extends Exception {
	
	public ImageStoreServiceException() {
		super();
	}

	public ImageStoreServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImageStoreServiceException(String message) {
		super(message);
	}

	public ImageStoreServiceException(Throwable cause) {
		super(cause);
	}

}
