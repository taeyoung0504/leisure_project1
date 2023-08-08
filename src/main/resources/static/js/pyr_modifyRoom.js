

//=============== 유효성 검사 ===============


// 방 등록 값 유효성 검사  (빈 공백)
function isValidProductLetter(value) {
	if (value.trim() === '') {
		// 값이 공백인 경우
		return false;
	}
	return true;
}

//유효성 검사 (빈 공백 +  숫자 )
function isValidProductAmount(value) {
	if (value.trim() === '') {
		// 값이 공백인 경우
		return false;
	}
	return !isNaN(value); // 숫자가 아니면 true
}

//유효성 검사 (숫자 0)
function isValidProductZeroAmount(value) {
	const numericValue = Number(value); // value를 숫자로 변환
	if (numericValue === 0) {
		// 값이 0 인 경우
		return false;
	}
	return true;
}

//초기화 버튼을 눌렀을 때를 위해 기존의 값들을 저장 ======

//기존에 있는 값을 담기 위한 객체
var originalValues = {}; //전역

//기존에 있는 이미지를 담기 위한 배열
var originalImageUrls = []; //전역


//전송 및 이미지 슬라이드를 위한 값들을 담는 배열 ======

//이미지 슬라이드 전역 변수로 설정 => 이미지 추가시 슬라이드에도 보여주가 위함
let images = [];

//새로생긴 이미지 전송을 위한 배열
let newImages = [];

//삭제된 이미지를 담을 배열(pk만 저장)
var deletedImageIds = [];

// formData 전역 변수로 선언
var formData = new FormData();


$(document).ready(function() {

	// 각 필드의 기본 값을 가져와서 저장
	originalValues['type'] = $('.edit_type').val();
	originalValues['detail'] = $('.edit_detail').val();
	originalValues['amount'] = $('.edit_amount').val();
	originalValues['count'] = $('.edit_count').val();
	originalValues['pernum'] = $('.edit_pernum').val();
	originalValues['checkin'] = $('.edit_checkin').val();
	originalValues['checkout'] = $('.edit_checkout').val();


	$('.imgContainer').each(function() {
		//var imgid = $(this).find('.img_id').text();
		var imageInfo = {
			imageUrl: $(this).find('.showImgs').attr('src'),
			//imgid: imgid
		};
		originalImageUrls.push(imageInfo);
	});

});




//수정 완료 버튼 클릭 시 값을 전송
$(document).on('click', '.editOkProduct', function() {
	//입력값 받은 값들을 가져온다
	var productContainer = $(this).closest('#productContainer');

	var productId = parseInt($(this).closest('.box')
		.find('.product_id').text());


	//객실 이름
	if (!isValidProductLetter(productContainer.find('.edit_type').val())) {
		showEditInputError('edit_type', '상세설명을 입력해 주세요');
		return; // 유효성 검사에 실패하면 함수 종료
	}


	//상세설명
	if (!isValidProductLetter(productContainer.find('.edit_detail').val())) {
		showEditInputError('edit_detail', '상세설명을 입력해 주세요');
		return; // 유효성 검사에 실패하면 함수 종료
	}


	//금액이 0 인경우
	if (!isValidProductZeroAmount(productContainer.find('.edit_amount').val())) {
		showEditInputError('edit_amount', '0보다 큰 수를 입력해 주세요');
		return; // 유효성 검사에 실패하면 함수 종료
	}


	//최대인원 공백시
	if (!isValidProductAmount(productContainer.find('.edit_pernum').val())) {
		showEditInputError('edit_pernum', '최대인원을 입력해주세요');
		return; // 유효성 검사에 실패하면 함수 종료
	}

	//최대인원 숫자가 아닐 시
	if (!isValidProductAmount(productContainer.find('.edit_pernum').val())) {
		showEditInputError('edit_pernum', '숫자를 입력해주세요');
		return; // 유효성 검사에 실패하면 함수 종료
	}

	//최대인원이 0 인경우
	if (!isValidProductZeroAmount(productContainer.find('.edit_pernum').val())) {
		showEditInputError('edit_pernum', '0보다 큰 수를 입력해 주세요');
		return; // 유효성 검사에 실패하면 함수 종료
	}

	//객실 수 
	if (!isValidProductAmount(productContainer.find('.edit_count').val())) {
		showEditInputError('edit_count', '객실수를 입력해주세요');
		return; // 유효성 검사에 실패하면 함수 종료
	}

	//객실 수 공백시
	if (!isValidProductAmount(productContainer.find('.edit_count').val())) {
		showEditInputError('edit_count', '최대인원을 입력해주세요');
		return; // 유효성 검사에 실패하면 함수 종료
	}

	//객실 수 숫자가 아닐 시
	if (!isValidProductAmount(productContainer.find('.edit_count').val())) {
		showEditInputError('edit_count', '숫자를 입력해주세요');
		return; // 유효성 검사에 실패하면 함수 종료
	}

	//객실 수 0 인경우
	if (!isValidProductZeroAmount(productContainer.find('.edit_count').val())) {
		showEditInputError('edit_count', '0보다 큰 수를 입력해 주세요');
		return; // 유효성 검사에 실패하면 함수 종료
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


	// formData에 저장된 이미지들을 순회하며 콘솔에 출력(출력용) ==========
	for (var file of formData.getAll('images[]')) {
		console.log(file); // 각 이미지 파일을 콘솔에 출력
	}


	// 최종적으로 배열에 새롭게 추가된 이미지들을 넣음
	for (var i = 0; i < newImages.length; i++) {
		formData.append('newImages[]', newImages[i]);
	}


	// 최종적으로 배열에 저장된 삭제된 이미지들의 PK들을 formData에 추가
	for (var i = 0; i < deletedImageIds.length; i++) {
		formData.append('deletedImages[]', deletedImageIds[i]);
	}


	// AJAX 요청을 통해 데이터를 서버에 전송하고 값을 변경
	$.ajax({
		url: '/partner/product/updateProduct',
		type: 'POST',
		data: formData,
		enctype: 'multipart/form-data',
		processData: false,
		contentType: false,
		success: function(response) {

			Swal.fire('수정성공', '수정 완료되었습니다', 'success').then(() => {
				window.location.href = '/partner/product/addproduct/' + response;
			});
		},
		error: function(xhr) {
			// 서버에서 반환된 에러 메시지를 가져옴
			var errorMessage = xhr.responseText;
			Swal.fire(errorMessage)
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


	// 새로운 이미지 배열에서 삭제하고자 하는 이미지를 제외한 나머지 이미지만을 남김
	newImages = newImages.filter(function(image) {
		//image의 이름이 imageName과 다른 경우에만 true를 반환
		return image.name !== imageName; // 이미지 이름이 일치하지 않는 요소만 반환하므로, 해당 이미지는 배열에서 제거
	});

	//새로운 FormData 객체를 원래의 formData에 할당
	formData = newFormData;

	//===== 여기서 부터 기존의 이미지들 삭제할 때 배열에 담는 기능 ======

	if (deletedImageId.trim() !== '') {
		console.log(deletedImageId + "값을 추가한다. ");
		deletedImageIds.push(deletedImageId); // 삭제된 이미지의 PK를 "배열"에 추가
		console.log(deletedImageIds);
	}


	// imageURL에 해당하는 이미지를 images 배열에서 찾아 제거(개별 삭제된 이미지 슬라이드에서 제거)
	for (var i = 0; i < images.length; i++) {
		if (images[i].imgUrl === imageURL) {
			images.splice(i, 1);  // i번째 요소를 제거
			break;
		}
	}
	const productContainers = $('.slideshow-container');

	// 슬라이드 쇼 재시작
	startSlideshow(productContainers, images);

	//중복되는 이미지를 여러개 올릴 수 있도록 
	const fileInput = document.querySelector('.imageUploadInput');
	fileInput.value = ''; // 파일 선택 필드의 값을 초기화


	// 화면에서 삭제된 이미지를 제거
	deletedImageContainer.remove();  // 이미지 요소 제거

});



// 이미지 추가 버튼 클릭 시 파일 선택 인풋 클릭 이벤트 발생
//해당 영역의 사진 추가 버튼만 구현이 된다
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

	// 선택된 파일 개수와 현재 이미지 개수를 더한 값
	var totalImgCount = files.length + MaxImgCount;
	//최대 이미지 개수보다 많으면 알림 메시지 표시
	if (totalImgCount > 5) {
		Swal.fire('이미지는 최대 5개만 가능합니다')
		return;
	}

	// 선택된 파일들을 순회하며 이미지를 화면에 추가
	for (var i = 0; i < files.length; i++) {
		createImageListItem(files[i], imageList);
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
			.css('display', 'none'); // 이미지 이름 요소를 숨김

		var deleteButton = $('<button></button>').text('x').addClass('imgLists_deleteButton');
		var buttonContainer = $('<div></div>').addClass('imgLists_deleteButton_position');

		// 이미지를 리스트에 추가
		imageContainer.append(image);
		buttonContainer.append(deleteButton);
		listItem.append(imageContainer);
		listItem.append(buttonContainer);
		imageList.append(listItem);

		listItem.append(imageName); // 이미지 이름 추가

		// 새로운 이미지들을 저장하는 newImages 배열에 추가
		newImages.push(file);
		console.log(file + " newImages . file 값을 추가한다. ");
		console.log(newImages);


		// 이미지 파일을 FormData 객체에 추가
		formData.append('images[]', file);

		// 슬라이드 이미지에 추가한 이미지 보여주기 위해서 추가
		images.push({
			imgUrl: imageURL,
		});
	};

	reader.readAsDataURL(file);
}



//수정 취소 버튼을 누름
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
			// No 버튼을 눌렀을 시 아무런 동작 없음
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



// 초기화 버튼 클릭 이벤트 처리
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

			//초기화 버튼을 누른 경우 값을 전송하는 formdata 초기화
			formData = new FormData();

			// 초기화 버튼 클릭 시 처리할 로직 작성
			// 해당 컨테이너 찾기
			var productContainer = $(this).closest('#productContainer');
			var slideshowContainer = productContainer.find('.slideshow-container');
			console.dir(slideshowContainer);

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
			//images = originalImageUrls.slice();
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
			//아무런 작업을 하지 않는다
		}
	});

});



// 이미지 초기화 (이미지 추가시 해다 영역에 이미지를 보여준다)
function resetImages(productContainer) {
	var imageListContainer = productContainer.find('.imgLists .image-list');
	imageListContainer.empty();

	for (var i = 0; i < originalImageUrls.length; i++) {
		var imageInfo = originalImageUrls[i];
		var imageUrl = imageInfo.imageUrl || '';
		var imgId = imageInfo.imgId; // 수정: 변수명 imgId로 변경

		var listItem = $('<li><div class="img_list_position"><img src="' + imageUrl + '" alt="Product Image" class="list_img"><span th:text="' + imgId + '" style="display: none;" class="img_id"></span></div><div class="imgLists_deleteButton_position"><button class="imgLists_deleteButton">x</button></div></li>');

		imageListContainer.append(listItem);
	}
}



//이미지 슬라이드
$(document).ready(
	function() {
		let productContainers = $('.slideshow-container');

		//productContainers 를 순회하며 images 배열에 이미지를 담는다
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


	// 첫 번째 이미지로 초기화
	updateSlide();


	// 이전 이미지로 이동하는 함수
	function goToPrevSlide() {
		currentSlideIndex--;
		if (currentSlideIndex < 0) {
			currentSlideIndex = images.length - 1;
		}
		updateSlide();
	}

	// 다음 이미지로 이동하는 함수
	function goToNextSlide() {
		currentSlideIndex++;
		if (currentSlideIndex >= images.length) {
			currentSlideIndex = 0;
		}
		updateSlide();
	}

	// 슬라이드 업데이트 함수 (에니메이션 효과와 함께 슬라이드를 업데이트)
	function updateSlide() {
		const imgUrl = images[currentSlideIndex].imgUrl;
		slide.find('img').attr('src', imgUrl);
	}

	prevButton.on('click', goToPrevSlide);
	nextButton.on('click', goToNextSlide);

}



$(document).ready(function() {
	// 수정하기 입력 필드 값 변경 이벤트 처리
	$('.Product_contents input').on('input', function() {
		// 값이 변경된 입력 필드의 처리 로직을 작성합니다.
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
				showEditInputError(className, '방 이름을 입력해 주세요');
			} else {
				clearEditInputError(className);//수정된 값이 맞을 시에 제거
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
	var errorContainer = $('#' + className + '_error'); //에러 메세지 표시 컨테이너 찾음

	if (errorContainer.length === 0) {
		// 컨테이너 요소가 없는 경우, 동적으로 생성하여 추가
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
			/[^0-9.]/g, '')); //숫자로 변환
		var formattedNumber = amountNumber.toLocaleString(); //통화 기호
		$(this).text(formattedNumber); //변경
	});