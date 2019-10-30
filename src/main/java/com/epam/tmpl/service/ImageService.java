package com.epam.tmpl.service;

import java.util.List;

import com.epam.tmpl.dao.domain.Image;
import com.epam.tmpl.dao.domain.StorageType;
import com.epam.tmpl.service.exception.ImageStoreServiceException;

/**
 * Service interface for Image domain
 * 
 * @author Andrei_Rohau
 */
public interface ImageService {

	/**
	 * @param storageType
	 *            defines the storage place
	 * @param images
	 *            that are to be saved
	 * @throws ImageStoreServiceException
	 */
	void save(StorageType storageType, List<Image> images) throws ImageStoreServiceException;

	/**
	 * @param key
	 *            of the image to be found
	 * @return found Image or null
	 * @throws ImageStoreServiceException
	 */
	Image findByKey(StorageType storageType, String key) throws ImageStoreServiceException;

	/**
	 * @param pkey is an identifier of the deleting image
	 * @throws ImageStoreServiceException
	 */
	void delete(StorageType storageType, String key) throws ImageStoreServiceException;
	
	/**
	 * @param image
	 *            is an object in the meaning of searching criteria
	 * @return List<Image> images that comply with criteria
	 * @throws ImageStoreServiceException
	 */
	List<Image> findAll(Image criteria) throws ImageStoreServiceException;

}
