package com.epam.tmpl.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.tmpl.dao.ImageRepository;
import com.epam.tmpl.dao.domain.Image;
import com.epam.tmpl.dao.domain.StorageType;
import com.epam.tmpl.dao.exception.ImageStoreDAOException;
import com.epam.tmpl.service.exception.ImageStoreServiceException;

/**
 * @author Andrei_Rohau
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {

	private static final long LONG_ID = 1L;
	private static final String IMAGE_NAME = "img_not_found.jpg";
	private static final long LONG_TO_THROW_EXCEPTION = 3L;
	private static final Image IMAGE_TO_THROW_EXCEPTION = new Image("exception", "exception");
	private static final List<Image> IMAGES_TO_THROW_EXCEPTION = new LinkedList<>();

	private Image image;
	private List<Image> images = new LinkedList<Image>();

	@Mock
	private ImageRepository imageRepository;
	@InjectMocks
	private ImageServiceImpl imageServiceImpl;

	@Before
	public void setUp() throws Exception {
		image = new Image(IMAGE_NAME, "base64image");
		images.clear();
		images.add(image);

		doThrow(new ImageStoreDAOException()).when(imageRepository).save(IMAGES_TO_THROW_EXCEPTION);
		when(imageRepository.findById(LONG_ID)).thenReturn(image);
		when(imageRepository.findById(LONG_TO_THROW_EXCEPTION)).thenThrow(new ImageStoreDAOException());
		when(imageRepository.deleteById(LONG_TO_THROW_EXCEPTION)).thenThrow(new ImageStoreDAOException());
		when(imageRepository.findAll(new Image())).thenReturn(images);
		when(imageRepository.findAll(IMAGE_TO_THROW_EXCEPTION)).thenThrow(new ImageStoreDAOException());
	}

	@Test
	public void testSaveInDatabase() throws Exception {
		imageServiceImpl.save(StorageType.DATABASE, images);

		verify(imageRepository).save(images);
	}

	@Test(expected = ImageStoreServiceException.class)
	public void testSaveInDatabaseThrowsException() throws Exception {
		imageServiceImpl.save(StorageType.DATABASE, IMAGES_TO_THROW_EXCEPTION);
	}

	@Test
	public void testSaveLocally() throws Exception {
		imageServiceImpl.save(StorageType.LOCAL, images);
	}

	@Test
	public void testFindByKeyInDatabase() throws Exception {
		Image actual = imageServiceImpl.findByKey(StorageType.DATABASE, "1");

		verify(imageRepository).findById(LONG_ID);
		assertEquals(image, actual);
	}

	@Test(expected = ImageStoreServiceException.class)
	public void testFindByKeyLocalFolder() throws Exception {

		imageServiceImpl.findByKey(StorageType.LOCAL, IMAGE_NAME);
	}

	@Test(expected = ImageStoreServiceException.class)
	public void testDeleteInDatabaseThrowsException() throws Exception {
		imageServiceImpl.delete(StorageType.DATABASE, String.valueOf(LONG_TO_THROW_EXCEPTION));
	}

	@Test
	public void testDeleteInDatabase() throws Exception {
		imageServiceImpl.delete(StorageType.DATABASE, "1");

		verify(imageRepository).deleteById(LONG_ID);
	}

	@Test
	public void testDeleteLocalFolder() throws Exception {
		imageServiceImpl.delete(StorageType.LOCAL, IMAGE_NAME);
	}

	@Test
	public void testFindAll() throws Exception {
		imageServiceImpl.findAll(new Image());

		verify(imageRepository).findAll(new Image());
	}

	@Test(expected = ImageStoreServiceException.class)
	public void testFindAllThrowsException() throws Exception {
		imageServiceImpl.findAll(IMAGE_TO_THROW_EXCEPTION);
	}
}
