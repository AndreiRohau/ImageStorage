package com.epam.tmpl.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.tmpl.dao.ImageRepository;
import com.epam.tmpl.dao.domain.Image;
import com.epam.tmpl.dao.domain.StorageType;
import com.epam.tmpl.dao.exception.ImageStoreDAOException;
import com.epam.tmpl.service.ImageService;
import com.epam.tmpl.service.exception.ImageStoreServiceException;
import com.epam.tmpl.service.exception.ImageStoreServiceValidatorException;
import com.epam.tmpl.service.util.Validator;

@Service
@Transactional("transactionManager")
public class ImageServiceImpl implements ImageService {

	private static final String SAVE_FILE_PATH = "\\uploads\\";

	private final ImageRepository imageRepository;

	@Autowired
	public ImageServiceImpl(ImageRepository imageRepository) throws IOException {
		this.imageRepository = imageRepository;
		prepareUploadsFolder();
	}
	
	/*
	 * creating directory for uploads, if exists, then cleans
	 */
	private void prepareUploadsFolder() throws IOException {
		File directory = new File(SAVE_FILE_PATH);
		if (directory.exists()) {
			FileUtils.cleanDirectory(directory);
		} else {
			new File(SAVE_FILE_PATH).mkdir();
		}
	}

	@Override
	public void save(StorageType storageType, List<Image> images) throws ImageStoreServiceException {
		try {
			Validator.validate(images);
			if (StorageType.DATABASE.equals(storageType)) {
				imageRepository.save(images);
			} else {
				for (Image image : images) {
					saveImageLocally(image);
				}
			}
		} catch (ImageStoreDAOException | ImageStoreServiceValidatorException e) {
			throw new ImageStoreServiceException(e);
		}
	}

	@Override
	public Image findByKey(StorageType storageType, String key) throws ImageStoreServiceException {
		try {
			Validator.validate(key);
			if (StorageType.DATABASE.equals(storageType)) {
				Long id = convertStringToLong(key);
				return imageRepository.findById(id);
			} else {
				return getLocalImage(key);
			}
		} catch (ImageStoreDAOException | ImageStoreServiceException | ImageStoreServiceValidatorException e) {
			throw new ImageStoreServiceException(e);
		}
	}

	@Override
	public void delete(StorageType storageType, String key) throws ImageStoreServiceException {
		try {
			Validator.validate(key);
			if (StorageType.DATABASE.equals(storageType)) {
				Long id = convertStringToLong(key);
				imageRepository.deleteById(id);
			} else {
				deleteLocalImageByName(key);
			}
		} catch (ImageStoreDAOException | ImageStoreServiceValidatorException e) {
			throw new ImageStoreServiceException(e);
		}
	}

	@Override
	public List<Image> findAll(Image criteria) throws ImageStoreServiceException {
		try {
			Validator.validate(criteria);
			List<Image> images = getLocalImages();
			images.addAll(imageRepository.findAll(criteria));
			return images;
		} catch (ImageStoreDAOException | ImageStoreServiceValidatorException e) {
			throw new ImageStoreServiceException(e);
		}
	}

	/**
	 * @param image
	 *            is a certain Image to be saved in local folder
	 * @return true if successful
	 * @throws ImageStoreServiceException
	 */
	private void saveImageLocally(Image image) throws ImageStoreServiceException {
		try {
			Validator.validate(image);
			if (image.getName() == null) {
				throw new ImageStoreServiceValidatorException();
			}
			File file = new File(SAVE_FILE_PATH, image.getName());
			if (!file.isDirectory()) {
				try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
					outputStream.write(Base64.getDecoder().decode((image.getContent())));
					outputStream.flush();
				}
			}
		} catch (ImageStoreServiceValidatorException | IOException e) {
			throw new ImageStoreServiceException("Error while saving images to local folder. ");
		}
	}

	/**
	 * @param name
	 *            of the Image
	 * @return Image or throws exception
	 * @throws ImageStoreServiceException
	 */
	private Image getLocalImage(String name) throws ImageStoreServiceException {
		try {
			File file = new File(SAVE_FILE_PATH + name);
			byte[] bytes = Files.readAllBytes(file.toPath());
			return new Image(file.getName(), StorageType.LOCAL.name(), Base64.getEncoder().encodeToString(bytes));
		} catch (IOException e) {
			throw new ImageStoreServiceException("Cannot find [" + name + "] locally");
		}
	}
	
	/**
	 * @param name
	 *            of the local Image to be deleted
	 * @throws ImageStoreServiceException
	 */
	private void deleteLocalImageByName(String name) throws ImageStoreServiceException {
		new File(SAVE_FILE_PATH, name).delete();
	}

	/**
	 * @return List of images from local Upload folder
	 * @throws IOException
	 */
	private List<Image> getLocalImages() throws ImageStoreServiceException {
		List<Image> images = new LinkedList<Image>();
		File file = new File(SAVE_FILE_PATH);
		File[] files = file.listFiles();
		if (files == null) {
			return images;
		}
		try {
			for (File f : files) {
				byte[] bytes = Files.readAllBytes(f.toPath());
				images.add(new Image(f.getName(), StorageType.LOCAL.name(), Base64.getEncoder().encodeToString(bytes)));
			}
			return images;
		} catch (IOException e) {
			throw new ImageStoreServiceException("Error while finding all Local images. ");
		}
	}

	/**
	 * @param str
	 *            to be converted to Long
	 * @return new Long or throws an exception
	 * @throws ImageStoreServiceValidatorException
	 */
	private Long convertStringToLong(String str) throws ImageStoreServiceValidatorException {
		try {
			return Long.valueOf(str);
		} catch (NumberFormatException e) {
			throw new ImageStoreServiceValidatorException("This [" + str + "] is not a database id.");
		}
	}

}
