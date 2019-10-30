<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<!-- CSS & JS-->
<spring:url value="/resources/js/jquery-3.3.1.js" var="jquery_3_3_1_js" />
<script src="${jquery_3_3_1_js}" type="text/javascript"></script>
<spring:url value="/resources/css/bootstrap.css" var="bootstrap" />
<link href="${bootstrap}" rel="stylesheet" />
<spring:url value="/resources/css/bootstrap.css.map" var="bootstrap_map" />
<link href="${bootstrap_map}" type="text/plain" />

<spring:url value="/resources/js/image.js" var="image_js" />
<script src="${image_js}" type="text/javascript"></script>
<spring:url value="/resources/css/image.css" var="image_css" />
<link href="${image_css}" rel="stylesheet" />

<title>Upload image</title>
</head>
<body>

	<div>
		<hr />
		<h1>
			IMAGE STORAGE
		</h1>
		<hr />
		<b> <a href="${pageContext.request.contextPath}">Upload
				/ View Image </a>
		</b> <br />
		<hr />
	</div>
	<div class="container">
		<form id="uploadingForm" name="uploadingForm"
			enctype="multipart/form-data"
			action="${pageContext.request.contextPath}/upload"
			method="POST">
			<div class="form-group">
				<label for="storageType">Choose storage type: </label> <select
					class="form-control" id="storageType" name="storageType">
					<option selected="selected">DATABASE</option>
					<option>LOCAL</option>
				</select>
			</div>
			<div class="form-group">
				<input class="form-control-file" id="selectedImages"
					name="uploadingImages" type="file" multiple="multiple"
					onchange="showInfo(); previewSelectedImages();" accept="image/*"/> selected files: <span
					id="fileNum">0</span> | size: <span id="fileSize">0</span>
			</div>
			<div class="form-group">
				<input class="btn btn-default" type="button"
					name="uploadingFormButton" value="Upload files"
					onclick="uploadImages()">
			</div>
		</form>
	</div>
	<br />

	<div id="selectedImagesDisplay" class="container"></div>

	<br />

	<hr />
	<div class="container">
		<table class="table table-bordered">
			<tbody>
				<tr>
					<td>
						<div class="container">
							<form id="viewForm" name="viewForm"
								action="${pageContext.request.contextPath}/get"
								method="GET">
								<div class="form-group">
									<input class="btn btn-default" type="button"
										value="Get all images" onclick="getAllImages()">
								</div>
							</form>
						</div>
					</td>
					<td>
						<div class="container">
							<form id="viewFormSpecific" name="viewForm"
								action="${pageContext.request.contextPath}/" method="GET">
								<div class="form-group">
									<input class="form-control" id="viewFormSpecific-input-num"
										type="text" placeholder="DATABASE id : '1' or LOCAL full name: '1.jpg'">
								</div>
								<div class="form-group">
									<input class="btn btn-default" type="button"
										value="Get specific image by id from database, or name locally"
										onclick="getImageByKey()">
								</div>
							</form>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<hr />
	<div id="imageContainer" class="container"></div>

	<hr />



</body>
</html>