package com.epam.tmpl.service.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.tmpl.dao.ImageRepository;
import com.epam.tmpl.dao.domain.Image;
import com.epam.tmpl.dao.domain.StorageType;
import com.epam.tmpl.service.exception.ImageStoreServiceException;
import com.epam.tmpl.service.impl.ImageServiceImpl;

/**
 * @author Andrei_Rohau
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplIntegrationTest {

	private static final String IMAGE_NAME = "img_not_found.jpg";
	private static final String SAVE_FILE_PATH = "\\uploads\\";

	private final File directory = new File(SAVE_FILE_PATH);
	private Image image;
	private List<Image> images = new LinkedList<Image>();

	@Mock
	private ImageRepository imageRepository;
	@InjectMocks
	private ImageServiceImpl imageServiceImpl;

	@BeforeClass
	public static void init() throws IOException {
		deleteUploadsFolder();
		new File(SAVE_FILE_PATH).mkdir();
	}

	@Before
	public void setUp() throws Exception {
		image = new Image(IMAGE_NAME, StorageType.LOCAL.name(), "base64image");
		images.clear();
		images.add(image);
		saveTestImageInUploadsFolder(image);

		when(imageRepository.findAll(new Image())).thenReturn(new LinkedList<Image>());
	}

	@After
	public void tearDown() throws Exception {
		FileUtils.cleanDirectory(directory);
	}

	@AfterClass
	public static void finalizeAfterClass() throws IOException {
		deleteUploadsFolder();
	}

	@Test
	public void saveImageLocallyTest() throws Exception {
		imageServiceImpl.save(StorageType.LOCAL, images);

	}

	@Test(expected = ImageStoreServiceException.class)
	public void saveImageLocallyTestThrowsException() throws Exception {
		images.add(null);
		imageServiceImpl.save(StorageType.LOCAL, images);

	}

	@Test
	public void getLocalImageTest() throws Exception {
		Image actual = imageServiceImpl.findByKey(StorageType.LOCAL, IMAGE_NAME);

		assertEquals(IMAGE_NAME, actual.getName());
	}

	@Test(expected = ImageStoreServiceException.class)
	public void getLocalImageTestThrowsException() throws Exception {
		imageServiceImpl.findByKey(StorageType.LOCAL, "IMAGE_NOT_EXISTS");
	}

	@Test
	public void deleteLocalImageByNameTest() throws Exception {
		imageServiceImpl.delete(StorageType.LOCAL, IMAGE_NAME);
		
		int actual = imageServiceImpl.findAll(new Image()).size();
		
		assertEquals(0, actual);
	}

	@Test
	public void getLocalImagesTest() throws Exception {
		List<Image> actual = imageServiceImpl.findAll(new Image());

		assertEquals(image.getName(), actual.get(0).getName());
	}

	/**
	 * Cleans and deletes uploading folder
	 * @throws IOException
	 */
	private static void deleteUploadsFolder() throws IOException {
		File directory = new File(SAVE_FILE_PATH);
		if (directory.exists()) {
			FileUtils.deleteDirectory(directory);
		}
	}

	/**
	 * @param image
	 *            to be saved in local folder
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void saveTestImageInUploadsFolder(Image image) throws IOException, FileNotFoundException {
		File file = new File(SAVE_FILE_PATH, image.getName());
		if (!file.isDirectory()) {
			try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
				outputStream.write(Base64.getDecoder().decode((image.getContent())));
				outputStream.flush();
			}
		}
	}

}
