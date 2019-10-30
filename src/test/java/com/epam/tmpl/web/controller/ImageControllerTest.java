package com.epam.tmpl.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.epam.tmpl.dao.domain.Image;
import com.epam.tmpl.dao.domain.StorageType;
import com.epam.tmpl.service.ImageService;
import com.epam.tmpl.web.config.WebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebConfig.class })
@WebAppConfiguration
public class ImageControllerTest {

	private static final String IMAGE_PATH = "src\\main\\webapp\\WEB-INF\\images\\";
	private static final String IMAGE_NAME = "img_not_found.jpg";
	private static final String ACTION_UPLOAD = "/upload/";
	private static final String UPLOADING_IMAGES = "uploadingImages";

	private File imageFile;
	private MockMvc mockMvc;

	@Autowired
	private ImageService imageService;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		imageFile = new File(IMAGE_PATH + IMAGE_NAME);
		byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
		Image image = new Image(imageFile.getName(), Base64.getEncoder().encodeToString(imageBytes));
		imageService.save(StorageType.DATABASE, Arrays.asList(image));
	}

	@AfterClass
	public static void finalizeAfterClass() throws Exception {
		File directory = new File("\\uploads\\");
		if (directory.exists()) {
			FileUtils.cleanDirectory(directory);
			directory.delete();
		}
	}
	
	@Test
	public void uploadDatabaseTest() throws Exception {
		MockMultipartFile myImage = new MockMultipartFile(UPLOADING_IMAGES, IMAGE_NAME, MediaType.IMAGE_JPEG_VALUE,
				Files.readAllBytes(imageFile.toPath()));
		mockMvc.perform(MockMvcRequestBuilders.fileUpload(ACTION_UPLOAD + StorageType.DATABASE).file(myImage))
				.andExpect(status().is(200));
	}

	@Test
	public void uploadLocalTest() throws Exception {
		MockMultipartFile myImage = new MockMultipartFile(UPLOADING_IMAGES, IMAGE_NAME, MediaType.IMAGE_JPEG_VALUE,
				Files.readAllBytes(imageFile.toPath()));
		mockMvc.perform(MockMvcRequestBuilders.fileUpload(ACTION_UPLOAD + StorageType.LOCAL).file(myImage))
				.andExpect(status().is(200));
	}

	@Test
	public void uploadNotImageTypeTest() throws Exception {
		MockMultipartFile myImage = new MockMultipartFile(UPLOADING_IMAGES, IMAGE_NAME, MediaType.TEXT_HTML_VALUE,
				Files.readAllBytes(imageFile.toPath()));
		mockMvc.perform(MockMvcRequestBuilders.fileUpload(ACTION_UPLOAD + StorageType.DATABASE).file(myImage))
				.andExpect(status().is(418));
	}

	@Test
	public void handleExceptionTest() throws Exception {
		mockMvc.perform(post(ACTION_UPLOAD)).andExpect(status().is(404));
	}

	@Test
	public void getByKeyTest() throws Exception {
		mockMvc.perform(get("/" + StorageType.DATABASE.name() + "/1")).andExpect(status().is(200))
				.andExpect(jsonPath("$.name").value(IMAGE_NAME));
	}

	@Test
	public void getAllTest() throws Exception {
		mockMvc.perform(get("/get")).andExpect(status().is(200))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[1].name").value(IMAGE_NAME));
	}

	@Test
	public void deleteLocalTest() throws Exception {
		mockMvc.perform(delete("/" + StorageType.LOCAL.name() + "/1.jpg")).andExpect(status().isOk());
	}

	@Test
	public void deleteTest() throws Exception {
		mockMvc.perform(delete("/" + StorageType.DATABASE.name() + "/1")).andExpect(status().isOk());
	}
	
}
