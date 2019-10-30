package com.epam.tmpl.dao.exception;

/**
 * Exception to wrap the DAO layer
 * 
 * @author Andrei_Rohau
 */
@SuppressWarnings("serial")
public class ImageStoreDAOException extends Exception {

	public ImageStoreDAOException() {
		super();
	}

	public ImageStoreDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImageStoreDAOException(String message) {
		super(message);
	}

	public ImageStoreDAOException(Throwable cause) {
		super(cause);
	}

}
