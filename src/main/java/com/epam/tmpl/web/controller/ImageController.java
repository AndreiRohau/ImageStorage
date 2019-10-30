package com.epam.tmpl.web.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.epam.tmpl.dao.domain.Image;
import com.epam.tmpl.dao.domain.StorageType;
import com.epam.tmpl.service.ImageService;

@Controller
@RequestMapping("/")
public class ImageController {

	private final ImageService imageService;

	@Autowired
	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}

	@RequestMapping(value = "/upload/{storageType}", method = RequestMethod.POST)
	public @ResponseBody void upload(@PathVariable StorageType storageType,
			@RequestParam LinkedList<Image> uploadingImages) throws Exception {
		imageService.save(storageType, uploadingImages);
	}

	@RequestMapping(value = "/{storageType}/{key:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Image getByKey(@PathVariable StorageType storageType, @PathVariable String key) throws Exception {
		return imageService.findByKey(storageType, key);
	}

	@RequestMapping(value = "/{storageType}/{key:.+}", method = RequestMethod.DELETE)
	public @ResponseBody void delete(@PathVariable StorageType storageType, @PathVariable String key) throws Exception {
		imageService.delete(storageType, key);
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Image> getAll() throws Exception {
		return imageService.findAll(new Image());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
	public @ResponseBody String handleException(Exception e) {
		return e.getMessage();
	}


}
