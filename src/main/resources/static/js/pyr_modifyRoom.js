

// 빈 공백 검사
function isValidProductLetter(value) {
	if (value.trim() === '') {
		return false;
	}
	return true;
}

//유효성 검사 (빈 공백 +  숫자 )
function isValidProductAmount(value) {
	if (value.trim() === '') {
		return false;
	}
	return !isNaN(value);
}

//유효성 검사 (숫자 0)
function isValidProductZeroAmount(value) {
	const numericValue = Number(value);
	if (numericValue === 0) {
		return false;
	}
	return true;
}

//원래 값(전역)

var originalValues = {};

var originalImageUrls = [];


//전송 및 이미지 슬라이드(전역)

//이미지 슬라이드
let images = [];

//새로생긴 이미지 전송
let newImages = [];

//삭제된 이미지 PK
var deletedImageIds = [];

// formData 전역 변수로 선언
var formData = new FormData();


$(document).ready(function() {

	originalValues['type'] = $('.edit_type').val();
	originalValues['detail'] = $('.edit_detail').val();
	originalValues['amount'] = $('.edit_amount').val();
	originalValues['count'] = $('.edit_count').val();
	originalValues['pernum'] = $('.edit_pernum').val();
	originalValues['checkin'] = $('.edit_checkin').val();
	originalValues['checkout'] = $('.edit_checkout').val();

	$('.imgContainer').each(function() {
		var imageInfo = {
			imageUrl: $(this).find('.showImgs').attr('src'),
		};
		originalImageUrls.push(imageInfo);
	});
});



//수정 완료 버튼 클릭
$(document).on('click', '.editOkProduct', function() {
	//입력값 받은 값
	var productContainer = $(this).closest('#productContainer');

	var productId = parseInt($(this).closest('.box')
		.find('.product_id').text());

	//유효성 검사
	const editValidations = [
		{ field: '.edit_type', validator: isValidProductLetter, errorMessage: '객실이름을 입력해 주세요' },
		{ field: '.edit_detail', validator: isValidProductLetter, errorMessage: '상세설명을 입력해 주세요' },
		{ field: '.edit_amount', validator: isValidProductZeroAmount, errorMessage: '0보다 큰 수를 입력해 주세요' },
		{ field: '.edit_pernum', validators: [isValidProductAmount, isValidProductZeroAmount], errorMessages: ['최대인원을 입력해주세요', '0보다 큰 수를 입력해 주세요'] },
		{ field: '.edit_count', validators: [isValidProductAmount, isValidProductZeroAmount], errorMessages: ['객실수를 입력해주세요', '0보다 큰 수를 입력해 주세요'] }
	];

	for (let i = 0; i < editValidations.length; i++) {
		const validation = editValidations[i];
		const value = productContainer.find(validation.field).val();

		if (Array.isArray(validation.validators)) {
			for (let j = 0; j < validation.validators.length; j++) {
				if (!validation.validators[j](value)) {
					showEditInputError(validation.field.slice(1), validation.errorMessages[j]);
					return;
				}
			}
		} else {
			if (!validation.validator(value)) {
				showEditInputError(validation.field.slice(1), validation.errorMessage);
				return;
			}
		}
	}

	// 수정된 값을 가져옴
	var editedValues = {
		productId: productId,
		count: productContainer.find('.edit_count').val(),
		type: productContainer.find('.edit_type').val(),
		detail: productContainer.find('.edit_detail').val(),
		pernum: productContainer.find('.edit_pernum').val(),
		amount: productContainer.find('.edit_amount').val(),
		checkin: productContainer.find('.edit_checkin').val(),
		checkout: productContainer.find('.edit_checkout').val()
	};

	for (var key in editedValues) {
		formData.append(key, editedValues[key]);
	}

	// 최종적으로 배열에 새롭게 추가된 이미지들을 넣음
	for (var i = 0; i < newImages.length; i++) {
		formData.append('newImages[]', newImages[i]);
	}

	// 최종적으로 배열에 저장된 삭제된 이미지들의 PK들을 formData에 추가
	for (var i = 0; i < deletedImageIds.length; i++) {
		formData.append('deletedImages[]', deletedImageIds[i]);
	}


	$.ajax({
		url: '/partner/product/updateProduct',
		type: 'POST',
		data: formData,
		enctype: 'multipart/form-data',
		processData: false,
		contentType: false,
		dataType: 'text',
		success: function(response) {

			Swal.fire('수정성공', '수정 완료되었습니다', 'success').then(() => {
				window.location.href = '/partner/product/addproduct/' + response;
			});
		},
		error: function(xhr) {

			Swal.fire(xhr.responseText);
		}
	});

});


// 이미지 개당 삭제버튼 클릭
$('.imgLists').on('click', '.imgLists_deleteButton', function() {
	var imageURL = $(this).closest('li').find('.list_img').attr('src');
	var deletedImageId = $(this).closest('.img_list_position').find('.list_img_id').text(); //이미지 PK 를 가져온다
	var deletedImageContainer = $(this).closest('li');
	var imageName = $(this).closest('li').find('.image_name').text();

	// 이미지 개수 체크
	var imageCount = $(this).closest('.imgLists').find('.image-list li').length;

	// 최소 이미지 개수보다 작으면 알림 메시지 표시
	if (imageCount <= 1) {
		Swal.fire('이미지는 최소 1개가 필요합니다')
		return;
	}


	//새로운 FormData 객체 생성 후 기존의 FormData에 추가
	var newFormData = new FormData();

	newImages = newImages.filter(function(image) {
		return image.name !== imageName;
	});

	//새로운 FormData 객체를 원래의 formData에 할당
	formData = newFormData;

	//삭제된 이미지 배열 추가
	if (deletedImageId.trim() !== '') {
		deletedImageIds.push(deletedImageId);
	}

	//개별 삭제된 이미지 슬라이드에서 제거
	for (var i = 0; i < images.length; i++) {
		if (images[i].imgUrl === imageURL) {
			images.splice(i, 1);
			break;
		}
	}
	const productContainers = $('.slideshow-container');

	startSlideshow(productContainers, images);

	const fileInput = document.querySelector('.imageUploadInput');
	fileInput.value = ''; // 파일 선택 필드의 값을 초기화

	// 화면에서 삭제된 이미지를 제거
	deletedImageContainer.remove();

});



// 이미지 추가 버튼 클릭 
$('.addImagesButton').click(
	function() {
		$(this).closest('.imageUploadContainer').find(
			'.imageUploadInput').click();
	});



// 이미지 업로드 인풋 필드의 값이 변경되면 실행되는 이벤트 핸들러
$('.imageUploadInput').change(function() {

	var files = this.files;
	var imageList = $(this).closest('.imgLists').find('.image-list');

	//이미지 추가 갯수 제한
	var MaxImgCount = $(this).closest('.imgLists').find('.image-list li').length;

	var totalImgCount = files.length + MaxImgCount;

	if (totalImgCount > 5) {
		Swal.fire('이미지는 최대 5개만 가능합니다')
		return;
	}

	// 선택된 파일들을 순회하며 이미지를 화면에 추가
	for (var i = 0; i < files.length; i++) {
		var file = files[i];
		//png 또는 jpg인지 검사
		if (file.type !== 'image/png' && file.type !== 'image/jpeg') {
			Swal.fire('png 또는 jpg 이미지만 등록할 수 있습니다');
			return;
		}
		createImageListItem(file, imageList);
	}
});


function createImageListItem(file, imageList) {
	var reader = new FileReader();

	reader.onload = function(e) {
		var imageURL = e.target.result;
		var listItem = $('<li></li>');
		var imageContainer = $('<div></div>').addClass('img_list_position');
		var image = $('<img>').attr('src', imageURL).addClass('list_img');
		var imageName = $('<span></span>')
			.text(file.name)
			.addClass('image_name')
			.css('display', 'none');

		var deleteButton = $('<button></button>').text('x').addClass('imgLists_deleteButton');
		var buttonContainer = $('<div></div>').addClass('imgLists_deleteButton_position');

		// 이미지를 리스트에 추가
		imageContainer.append(image);
		buttonContainer.append(deleteButton);
		listItem.append(imageContainer);
		listItem.append(buttonContainer);
		imageList.append(listItem);

		listItem.append(imageName);

		newImages.push(file);

		formData.append('images[]', file);

		// 슬라이드 이미지 추가
		images.push({
			imgUrl: imageURL,
		});
	};

	reader.readAsDataURL(file);
}



//수정 취소 버튼
$(document).on('click', '.cancelProduct', function() {
	swalWithBootstrapButtons.fire({
		title: '변경 사항이 적용되지 않습니다',
		text: '이전 페이지로 돌아가시겠습니까?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonText: '확인',
		cancelButtonText: '취소',
		reverseButtons: true

	}).then((result) => {
		if (result.isConfirmed) {
			window.history.back();
		} else if (result.dismiss === Swal.DismissReason.cancel) {

		}
	});
});

/*swal*/
const swalWithBootstrapButtons = Swal.mixin({
	customClass: {
		confirmButton: 'btn btn-success',
		cancelButton: 'btn btn-danger'
	}
});



// 초기화 버튼 클릭
$(document).on('click', '.resetProduct', function() {


	swalWithBootstrapButtons.fire({
		title: '변경 사항이 적용되지 않습니다',
		text: '초기화 하시겠습니까?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonText: '확인',
		cancelButtonText: '취소',
		reverseButtons: true

	}).then((result) => {
		if (result.isConfirmed) {

			//초기화 버튼을 누른 경우 formdata 초기화
			formData = new FormData();

			// 초기화 버튼 클릭
			var productContainer = $(this).closest('#productContainer');
			var slideshowContainer = productContainer.find('.slideshow-container');

			//해당 영역만을 초기화 시킴
			productContainer.find('.edit_count').val(originalValues['count']);
			productContainer.find('.edit_type').val(originalValues['type']);
			productContainer.find('.edit_pernum').val(originalValues['pernum']);
			productContainer.find('.edit_amount').val(originalValues['amount']);
			productContainer.find('.edit_checkin').val(originalValues['checkin']);
			productContainer.find('.edit_checkout').val(originalValues['checkout']);
			productContainer.find('.edit_detail').val(originalValues['detail']);

			//이미지 다시 초기화
			resetImages(productContainer);

			//이미지 슬라이드 이미지 초기화
			images = [];
			originalImageUrls.forEach(function(imageObj) {
				images.push({ imgUrl: imageObj.imageUrl });
			});

			// 슬라이드 쇼 재시작
			startSlideshow(slideshowContainer, images);

			//오류 메세지가 있다면 제거
			clearEditInputError('edit_type');
			clearEditInputError('edit_detail');
			clearEditInputError('edit_amount');
			clearEditInputError('edit_count');
			clearEditInputError('edit_pernum');

		} else if (result.dismiss === Swal.DismissReason.cancel) {

		}
	});

});



// 이미지 초기화
function resetImages(productContainer) {
	var imageListContainer = productContainer.find('.imgLists .image-list');
	imageListContainer.empty();

	for (var i = 0; i < originalImageUrls.length; i++) {
		var imageInfo = originalImageUrls[i];
		var imageUrl = imageInfo.imageUrl || '';
		var imgId = imageInfo.imgId;

		var listItem = $('<li><div class="img_list_position"><img src="' + imageUrl + '" alt="Product Image" class="list_img"><span th:text="' + imgId + '" style="display: none;" class="img_id"></span></div><div class="imgLists_deleteButton_position"><button class="imgLists_deleteButton">x</button></div></li>');

		imageListContainer.append(listItem);
	}
}



//이미지 슬라이드
$(document).ready(
	function() {
		let productContainers = $('.slideshow-container');

		productContainers.each(function() {
			let slideshowContainer = $(this);


			$(this).find('.imgContainer').each(
				function() {
					const img = $(this).find('img');
					const imgUrl = img.attr('src');


					images.push({
						imgUrl: imgUrl,
					});
				});

			//이미지 슬라이드 시작
			startSlideshow(slideshowContainer, images);

		});
	});


//이미지 슬라이드
function startSlideshow(slideshowContainer, images) {
	let currentSlideIndex = 0;
	const slide = slideshowContainer.find('.slide');
	const prevButton = slideshowContainer.find('.prev');
	const nextButton = slideshowContainer.find('.next');

	updateSlide();


	// 이전 이미지
	function goToPrevSlide() {
		currentSlideIndex--;
		if (currentSlideIndex < 0) {
			currentSlideIndex = images.length - 1;
		}
		updateSlide();
	}

	// 다음 이미지
	function goToNextSlide() {
		currentSlideIndex++;
		if (currentSlideIndex >= images.length) {
			currentSlideIndex = 0;
		}
		updateSlide();
	}

	// 슬라이드 업데이트 함수
	function updateSlide() {
		const imgUrl = images[currentSlideIndex].imgUrl;
		slide.find('img').attr('src', imgUrl);
	}

	prevButton.on('click', goToPrevSlide);
	nextButton.on('click', goToNextSlide);

}



$(document).ready(function() {
	$('.Product_contents input').on('input', function() {

		var className = $(this).attr('class');
		var classValue = $(this).val();

		editInputValueCheck(className, classValue);
	});
});


function editInputValueCheck(className, classValue) {
	// 필드 타입에 따라 유효성 검사를 수행
	switch (className) {
		case 'edit_type':
			if (!isValidProductLetter(classValue)) {
				showEditInputError(className, '객실 이름을 입력해 주세요');
			} else {
				clearEditInputError(className);
			}
			break;


		case 'edit_detail':
			if (!isValidProductLetter(classValue)) {
				showEditInputError(className, '상세설명을 입력해 주세요');
			} else {
				clearEditInputError(className);
			}
			break;


		case 'edit_amount':
			if (!isValidProductAmount(classValue)) {
				showEditInputError(className, '숫자를 입력해 주세요');
			} else {
				clearEditInputError(className);
			}
			break;

		case 'edit_count':
			if (!isValidProductAmount(classValue)) {
				showEditInputError(className, '숫자를 입력해 주세요');

			} else {
				clearEditInputError(className);
			}
			break;

		case 'edit_pernum':
			if (!isValidProductAmount(classValue)) {
				showEditInputError(className, '숫자를 입력해 주세요');

			} else {
				clearEditInputError(className);
			}
			break;

		default:
			break;
	}
}

// 에러 메시지를 표시
function showEditInputError(className, errorMessage) {
	var errorContainer = $('#' + className + '_error');

	if (errorContainer.length === 0) {
		errorContainer = $('<a id="' + className + '_error" class="error"></a>');
		errorContainer.insertAfter($('.' + className).closest('div'));
	}
	errorContainer.text(errorMessage); // 에러 메시지 설정
}


// 오류 메시지 제거 함수
function clearEditInputError(className) {
	var errorElementClass = className + '_error';
	$('#' + errorElementClass).remove();
}



//숫자에 쉼표를 넣어서 단위 확인
$('.product_amount').each(
	function() {
		var amountText = $(this).text();
		var amountNumber = parseFloat(amountText.replace(
			/[^0-9.]/g, ''));
		var formattedNumber = amountNumber.toLocaleString();
		$(this).text(formattedNumber);
	});