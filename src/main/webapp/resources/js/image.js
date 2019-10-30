
/*
 * Pt#1 : UPLOAD FUNCTIONS
 */
function showInfo() {
	let allFiles = $("#selectedImages")[0].files;
	/*
	 * Information about uploading images : amount, size
	 */
    let nBytes = 0;
    let filesTotal = allFiles.length;
    for (let i = 0; i < filesTotal; i++) {
        nBytes += allFiles[i].size;
    }
    let totalSize = nBytes + " bytes";
    // optional code for multiples approximation
    for (let allMultiples = ["KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"], iMultiple = 0, nApprox = nBytes / 1024; nApprox > 1; nApprox /= 1024, iMultiple++) {
        totalSize = nApprox.toFixed(3) + " " + allMultiples[iMultiple] + " (" + nBytes + " bytes)";
    }
    // end of optional code
    $("#fileNum").html(filesTotal);
    $("#fileSize").html(totalSize);
    
    
}
/*
 * Thumbnails of uploading images on view page
 */
function previewSelectedImages() {
	let allFiles = $("#selectedImages")[0].files;
    $("#selectedImagesDisplay").empty();
	// Loop through the FileList and render image files as thumbnails.
	for (let i = 0, f; f = allFiles[i]; i++) {
		// Only process image files.
		if (!f.type.match('image.*')) {
			alert("Only images would be saved. [" + f.name + "] is not an image.");
		} else {
			let reader = new FileReader();
			// Closure to capture the file information.
			reader.onload = (function(theFile) {
				return function(e) {
					// Render thumbnail
					let span = $("<span></span>");
					span.html('<img class="thumb" src="' + e.target.result + '"/>');
					$('#selectedImagesDisplay')[0].insertBefore(span[0], null);
				};
			})(f);
			// Read in the image file as a data URL.
			reader.readAsDataURL(f);
		}
	}
}

/*
 * Upload images to server function
 */
function uploadImages() {
	let allFilesLength = $("#selectedImages")[0].files.length;
	let storageType = $("#storageType").val();
	
	// if no files to upload
	let imagesTotal;
	if(allFilesLength < 1) {
		alert("Per prima add files!");
		return;
	} else if (allFilesLength > 1) {
		imagesTotal = 'Images have';
	} else {
		imagesTotal = 'Image has';
	}
	
	let thisForm = $("#uploadingForm")[0];
	// uploads files
	let ajaxReq = $.ajax({
	    url: thisForm.action + '/' + storageType,
	    data: new FormData(thisForm),
	    cache: false,
	    contentType: false,
	    processData: false,
	    method: 'POST'
	});
	
    // calls on success of file upload
    ajaxReq.done(function(data){
    	$("#selectedImages").val('');
    	$("#fileNum").text('0');
    	$("#fileSize").text('0');
    	$("#selectedImagesDisplay").html("");
    	alert(imagesTotal + " been saved.");
    });
    
    // calls on failure of file upload
    ajaxReq.fail(function(data) {
		alert(data.responseText);
	});
}

/*
 * Pt#2 : DISPLAY FUNCTIONS
 */ 

/*
 * DISPLAY function
 */
function getImageByKey() {
	let thisForm = $("#viewFormSpecific")[0];
	let storageType = $("#storageType").val();

	let pk = $("#viewFormSpecific-input-num").val();

	if (pk == '') {
		alert("Enter an id or name.");
	} else {
		// retrieve file
		let ajaxReq = $.ajax({
		    url: thisForm.action + storageType + '/' + pk,
		    cache: false,
		    processData: false,
		    method: 'GET'
		});
		
	    // calls on success of file retrieve
	    ajaxReq.done(function(data){
	    	// clear vieport div from all images
	    	$("#imageContainer").empty();
    		if (data == '') {
    			alert("No content");
    		} else {
    			displayImages([data]);
    		}
	    });
	    
	    // calls on failure of file retrieve
	    ajaxReq.fail(function(data) {
			alert(data.responseText);
		});
	}
}

function getAllImages() {
	let thisForm = $("#viewForm")[0];
	// retrieve files
	let ajaxReq = $.ajax({
	    url: thisForm.action,
	    cache: false,
	    contentType: false,
	    processData: false,
	    method: 'GET'
	});
	
    // calls on success of file retrieve
    ajaxReq.done(function(data){
    	// clear vieport div from all images
    	$("#imageContainer").empty();
    	if (data.length == 0) {
    		alert("No content");
    	} else {
    		displayImages(data);
    	}
    });
    
    // calls on failure of file retrieve
    ajaxReq.fail(function(data) {
		alert(data.responseText);
	});
}

/*
 * A part of displayImages(), executes when success
 */
function displayImages(data) {	
	let imageTable = $("<table id='imageTable' name='imageTable' class='table table-bordered'></table>");
	let tBody = $("<tbody></tbody>");
	imageTable.append(tBody);
	
	for(let i in data){
		let imageObj = data[i];
		let storageType = imageObj['description'];
	    let pkey = imageObj['id'] == null ? imageObj['name'] : imageObj['id'];
	    
	    let tRow = $("<tr></tr>");
	    imageTable.append(tRow);
	    
	    for (let key in imageObj) {
	    	if (key != 'id') {
		        let value = imageObj[key];
		        
		        let td = $("<td id='" + key + "'></td>");
		        tRow.append(td);
		        
		        if (key == "content") {
		        	let img = $("<input type='image' class='imageContainer-img' src='data:image/jpg;base64," + value + "' onclick='changeImageSize(this)'>");
		        	td.append(img);
		        } else {
		        	td.append(value);
		        }
	    	}
	    }
	    let td = $("<td></td>");
	    tRow.append(td);
	    td.append("<input type='button' value='Delete' onclick='deleteImage(this, " + '"' + storageType + '"' + ',"' + pkey + '"' + ")' class='btn btn-default' >");
	}
	$("#imageContainer").append(imageTable);
}

/*
 * changes picture size on the page on click
 */
function changeImageSize(here) {
	if (here.className == "") {
		here.className = "imageContainer-img";
	} else {
		here.className = "";
	}
}

/*
 * Delete image from database by primary key
 */
function deleteImage(element, storageType, pkey) {
	
	let ajaxReq = $.ajax({
	    url: storageType + '/' + pkey,
	    cache: false,
	    contentType: false,
	    processData: false,
	    method: 'DELETE'
	});
	
    // calls on success of file DELETE
    ajaxReq.done(function(data){
    	alert("Image has been deleted.");
    	// erasing deleted image's row
    	$(element).parents('tr').empty();
    });
    
    // calls on failure of file DELETE
    ajaxReq.fail(function(data) {
		alert(data.responseText);
	});
}






































