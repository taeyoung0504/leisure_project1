

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


/* ====================  수정버튼 기능 구현  시작 ===================== */

//기존에 있는 값을 담기 위한 객체선언
var originalValues = {}; //


//기존에 있는 이미지를 담기 위한 배열
var originalImageUrls = []; //배열


// formData 전역 변수로 선언
var formData = new FormData();


$(document).ready(function() {

	// 각 필드의 값을 가져와 변수에 저장
	originalValues['type'] = $('.edit_type').val();
	originalValues['detail'] = $('.edit_detail').val();
	originalValues['amount'] = $('.edit_amount').val();
	originalValues['count'] = $('.edit_count').val();
	originalValues['pernum'] = $('.edit_pernum').val();
	originalValues['checkin'] = $('.edit_checkin').val();
	originalValues['checkout'] = $('.edit_checkout').val();


	console.log("originalValues:", originalValues);



	$('.imgContainer').each(function() {

		var imgid = $(this).find('.img_id').text();

		var imageInfo = {
			imageUrl: $(this).find('.showImgs').attr('src'),
			imgid: imgid
		};
		originalImageUrls.push(imageInfo);
	});


});



// 초기화 버튼 클릭 이벤트 처리 ===========================================


//수정 완료 버튼 클릭 시 이벤트를 처리 ===============
$(document).on('click', '.editOkProduct', function() {
	//입력값 받은 값들을 가져온다
	var productContainer = $(this).closest('#productContainer');

	//해당 영역의 pk를 가져온다
	var productContentsContainer = $(this).closest('.Product_contents');

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


	//위에 수정된 값을 FormData에 추가
	//  var formData = new FormData(); // FormData 객체 초기화

	for (var key in editedValues) {
		formData.append(key, editedValues[key]);
	}


	// 수정된 이미지 파일들과 삭제된 이미지 파일들을 FormData 객체에 추가
	var editedImages = productContainer.find('.edited_images');
	var deletedImages = productContainer.find('.deleted_images');



	// 개별 이미지 추가
	for (var i = 0; i < editedImages.length; i++) {
		var editedImageFile = editedImages[i].files[0];

		if (editedImageFile !== undefined && editedImageFile !== null) {
			formData.append('images[]', editedImageFile);
		}

	}



	//개별 이미지 삭제
	for (var i = 0; i < deletedImages.length; i++) {
		var deletedImageId = deletedImages[i].getAttribute('data-image-id');

		if (deletedImageId !== undefined && deletedImageId !== null) {
			formData.append('deletedImages[]', deletedImageId);
		}
	}




	// =============== 키 값 확인 ===============

	// FormData 객체의 키-값 쌍 확인
	for (let pair of formData.entries()) {
		console.log(pair[0], pair[1]);
	}



	// AJAX 요청을 통해 데이터를 서버에 전송하고 값을 변경
	$.ajax({
		url: '/product/updateProduct',
		type: 'POST',
		data: formData,
		enctype: 'multipart/form-data',
		processData: false,
		contentType: false,
		success: function(response) {
			// 서버 응답에 따른 처리
			//console.log("요청성공!!");
			//console.log(response);

			//alert("수정이 완료되었습니다.");

			//다시 이전페이지로 이동
			//  window.history.back();

			/*	Swal.fire('수정완료되었습니다');
				//1.5초 후에 이동
				setTimeout(function() {
					window.history.back();
				}, 1500); // 1.5초 후에 이전 페이지로 이동 
	*/

			Swal.fire('수정성공','수정완료되었습니다','success').then(() => {
				// 알림창이 닫힌 후에 1.5초 후에 이동
				setTimeout(function() {
					window.history.back();
				}, 1000); // 1초 후에 이전 페이지로 이동 
			});


		},
		error: function(xhr, status, error, product) {
			// 처리 중에 에러가 발생한 경우의 동작 
			console.log('AJAX 요청 실패');
			console.log(xhr);
			console.log(status);
			console.log(error);
			console.log(product);

		}
	});

});




// 이미지 개당 삭제버튼 클릭
$('.imgLists').on('click', '.imgLists_deleteButton', function() {
	var imageURL = $(this).closest('li').find('.list_img').attr('src');
	var deletedImageId = $(this).closest('.img_list_position').find('.list_img_id').text();
	var deletedImageContainer = $(this).closest('li');
	var imageName = $(this).closest('li').find('.image_name').text();


	console.log(imageName + "이미지 이름입니다."); //추가된 이미지 이름
	console.log(deletedImageId + "deletedImageId");  //이미지 id

	// 삭제된 이미지 파일을 FormData 객체에 추가
	//  formData.append('deletedImages[]', deletedImageId); //기존 삭제된 이미지의 id 값을 가져온다

	/*
		console.log(formData.get('images[]') + "들어온 이미지 입니다.");
	
	
	
		//해당 name이랑 form에 있는 images[] 와 이름 검사
		for (var file of formData.getAll('images[]')) {
			console.log(file.name + "for문 처음의 images");
	
			//파일 이름과 imageName이 같다면
			if (file.name === imageName) {
				console.log("============");
				console.log(file.name);
				console.log(imageName);
				console.log("============");
	
				//formData에서 해당 file을 삭제
				formData.delete('images[]', file);
				
			}
			console.log(file.name + "for문 마지막의 images");
			console.log(formData.getAll('images[]'));
	
		}
	
		console.log(formData.get('images[]') + " 이후의 images");
		console.log(formData.getAll('images[]'));
	*/

	//새로운 FormData 객체 생성
	var newFormData = new FormData();

	//기존의 formData에서 모든 파일을 순회
	for (var file of formData.getAll('images[]')) {
		//만약 파일의 이름이 삭제하고자 하는 파일의 이름과 같지 않다면
		if (file.name !== imageName) {
			//새로운 FormData에 해당 파일 추가
			newFormData.append('images[]', file);
		}
	}

	//새로운 FormData 객체를 원래의 formData에 할당
	formData = newFormData;



	// 삭제된 이미지의 id를 유효한 값으로 확인 후 추가
	if (deletedImageId.trim() !== '') {
		formData.append('deletedImages[]', deletedImageId);
	}


	// 이미지 개수 체크
	var imageCount = $(this).closest('.imgLists').find('.image-list li').length;

	// 최소 이미지 개수보다 작으면 알림 메시지 표시
	if (imageCount <= 1) {
		alert('이미지는 최소 1개가 필요합니다.');
		return;
	}

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


// =================== 이미지 길이 제한 =====================


// 이미지 업로드 인풋 필드의 값이 변경되면 실행되는 이벤트 핸들러
$('.imageUploadInput').change(function() {

	var files = this.files;
	//var imageList = $('.image-list');

	var imageList = $(this).closest('.imgLists').find('.image-list');

	//이미지 추가 갯수 제한
	var MaxImgCount = $(this).closest('.imgLists').find('.image-list li').length;

	// 선택된 파일 개수와 현재 이미지 개수를 더한 값
	var totalImgCount = files.length + MaxImgCount;
	//최대 이미지 개수보다 많으면 알림 메시지 표시
	if (totalImgCount > 5) {
		alert('이미지는 최대 5개만 가능합니다.');
		return;
	}

	// 선택된 파일들을 순회하며 이미지를 화면에 추가
	for (var i = 0; i < files.length; i++) {
		var file = files[i];
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




			// 이미지 파일을 FormData 객체에 추가
			formData.append('images[]', file);

			var imagesArray = formData.getAll('images[]');
			for (var i = 0; i < imagesArray.length; i++) {
				console.log(imagesArray[i]); // 각 이미지 파일 정보 출력
			}



		};

		reader.readAsDataURL(file);
	}

});


//========== 이미지 슬라이드 ==========

$(document).ready(
	function() {
		const productContainers = $('.slideshow-container');

		productContainers.each(function() {
			const slideshowContainer = $(this);
			const images = [];

			$(this).find('.imgContainer').each(
				function() {
					const img = $(this).find('img');
					const imgUrl = img.attr('src');



					var roomId = parseInt($(this).closest('.box')
						.find('.product_id').text());


					images.push({
						imgUrl: imgUrl,
						roomId: roomId
					});
				});

			startSlideshow(slideshowContainer, images);

		});
	});

function startSlideshow(slideshowContainer, images) {
	let currentSlideIndex = 0;
	const slide = slideshowContainer.find('.slide');
	const prevButton = slideshowContainer.find('.prev');
	const nextButton = slideshowContainer.find('.next');


	// 슬라이드 업데이트 함수 (에니메이션 효과와 함께 슬라이드를 업데이트)
	function updateSlide() {
		const imgUrl = images[currentSlideIndex].imgUrl;
		slide.css('background-image', `url(${imgUrl})`);
	}

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



//수정 취소 버튼을 눌렀을 시  ==========================
//이전 페이지로 돌아가도록 기능 구현(alert창 넣어서)

/*$(document).on('click', '.cancelProduct', function() {
	if (confirm('취소하시겠습니까?')) {
		window.history.back(); // 이전 페이지로 이동
	} else {
		// 아무 작업 없음, 현재 페이지에 머무름
	}
});*/

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
			// Yes 버튼을 눌렀을 때의 동작
			window.history.back();
		} else if (result.dismiss === Swal.DismissReason.cancel) {
			// No 버튼을 눌렀을 때의 동작
			// 추가적인 작업을 수행하거나 아무 동작도 하지 않을 수 있습니다.
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







// 초기화 버튼 클릭 이벤트 처리 ===========================================

$(document).on('click', '.resetProduct', function() {
	// 초기화 버튼 클릭 시 처리할 로직 작성
	// 해당 컨테이너 찾기
	var productContainer = $(this).closest('#productContainer');
	var productId = parseInt(productContainer.find('.Product_contents .product_id').text());



	//해당 영역만을 초기화 시킴
	productContainer.find('.edit_count').val(originalValues['count']);
	productContainer.find('.edit_type').val(originalValues['type']);
	productContainer.find('.edit_pernum').val(originalValues['pernum']);
	productContainer.find('.edit_amount').val(originalValues['amount']);
	productContainer.find('.edit_checkin').val(originalValues['checkin']);
	productContainer.find('.edit_checkout').val(originalValues['checkout']);
	productContainer.find('.edit_detail').val(originalValues['detail']);


	resetImages(productContainer);


	//오류 메세지가 있다면 제거
	clearEditInputError('edit_type');
	clearEditInputError('edit_detail');
	clearEditInputError('edit_amount');
	clearEditInputError('edit_count');
	clearEditInputError('edit_pernum');


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


function showEditInputError(className, errorMessage) {
	var errorContainer = $('#' + className + '_error'); // 에러 메시지를 표시할 컨테이너 요소 선택


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
		console.log(typeof amountText); //String 
		var amountNumber = parseFloat(amountText.replace(
			/[^0-9.]/g, '')); //숫자로 변환
		var formattedNumber = amountNumber.toLocaleString(); //통화 기호
		$(this).text(formattedNumber); //변경
	});