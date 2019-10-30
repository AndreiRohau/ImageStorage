package com.epam.tmpl.dao;

import java.util.List;

import com.epam.tmpl.dao.domain.Image;
import com.epam.tmpl.dao.exception.ImageStoreDAOException;

/**
 * DAO interface for Image domain
 * 
 * @author Andrei_Rohau
 */
public interface ImageRepository {

	/**
	 * @param images
	 *            is a list of domain Image to save into database
	 * @throws ImageStoreServiceException
	 */
	void save(List<Image> images) throws ImageStoreDAOException;

	/**
	 * @param id
	 *            of the Image to be found
	 * @return found Image or null
	 * @throws ImageStoreServiceException
	 */
	Image findById(Long id) throws ImageStoreDAOException;

	/**
	 * @param id
	 *            of the Image to be deleted
	 * @return amount of deleted rows
	 * @throws ImageStoreServiceException
	 */
	int deleteById(Long id) throws ImageStoreDAOException;

	/**
	 * @param image
	 *            is an object in the meaning of searching criteria
	 * @return List<Image> images that comply with criteria
	 * @throws ImageStoreServiceException
	 */
	List<Image> findAll(Image criteria) throws ImageStoreDAOException;

}
