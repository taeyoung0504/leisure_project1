
//방 수정하기 버튼 클릭시 해당 하는 곳으로 이동한다. 
function modifyRoom(button) {
	var editButton = $(button);
	var productId = parseInt(editButton.parents('.Product_contents').find('.product_id').text());
	var url = '/partner/product/modifyRoom/' + productId;
	window.location.href = url;
}



/*
//최초 등록시 사진은 최대 5개 까지만 등록 가능하도록 설정
$(document).ready(function() {
	$('#addPhoto').on('change', function() {
		var files = $(this).get(0).files;
		var maxAllowedFiles = 5;

		if (files.length > maxAllowedFiles) {
			alert('객실 사진은 최대 5장까지 등록할 수 있습니다.');
			// 선택한 파일 초기화
			//$(this).val('');
			$(this).prop('value', null);
		}
	});
});
*/
//=============== 유효성 검사 ===============

$(document).ready(function() {
	// 입력 필드 값 변경 이벤트 처리
	//$('#productForm input').change(function () {
	$('#productForm input').on('input', function() {
		// 값이 변경된 입력 필드의 처리 로직을 작성합니다.
		var fieldName = $(this).attr('name');
		var fieldValue = $(this).val();

		validateField(fieldName, fieldValue)

	});
});

// 유효성 검사 함수
function validateField(fieldName, fieldValue) {
	// 필드 타입에 따라 유효성 검사를 수행
	switch (fieldName) {
		case 'product_type':
			if (!isValidProductLetter(fieldValue)) {
				showError(fieldName, '방 이름을 입력해 주세요');
			} else {
				clearError(fieldName);//수정된 값이 맞을 시에 제거
			}
			break;
		case 'product_amount':
			if (!isValidProductAmount(fieldValue)) {
				showError(fieldName, '숫자를 입력해 주세요');
			} else {
				clearError(fieldName);
			}
			break;

		case 'product_detail':
			if (!isValidProductLetter(fieldValue)) {
				showError(fieldName, '상세설명을 입력해 주세요');
			} else {
				clearError(fieldName);
			}
			break;


		case 'product_count':
			if (!isValidProductAmount(fieldValue)) {
				showError(fieldName, '숫자를 입력해 주세요');

			} else {
				clearError(fieldName);
			}
			break;

		case 'product_pernum':
			if (!isValidProductAmount(fieldValue)) {
				showError(fieldName, '숫자를 입력해 주세요');

			} else {
				clearError(fieldName);
			}
			break;

		default:
			break;
	}
}

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



// 오류 메시지 표시 함수
function showError(fieldName, errorMessage) {
	var errorElementId = fieldName + 'Error';
	var errorElement = $('#' + errorElementId);

	if (errorElement.length === 0) {
		// 오류 메시지 엘리먼트가 없으면 생성합니다.
		var errorHtml = '<span id="' + errorElementId + '" class="error-message">' + errorMessage + '</span>';
		$('input[name="' + fieldName + '"]').parent().append(errorHtml);
	} else {
		errorElement.text(errorMessage);
	}

}

// 오류 메시지 제거 함수
function clearError(fieldName) {
	var errorElementId = fieldName + 'Error';
	$('#' + errorElementId).remove();
}


//초기화 버튼을 누른 경우 오류 메세지 모두 제거
$(document).ready(function() {
	// 초기화 버튼 클릭 이벤트 핸들러
	$('#addProductReset').click(function() {

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
				clearError('product_type');
				clearError('product_amount');
				clearError('product_detail');
				clearError('product_count');
				clearError('product_pernum');
				$('#productForm').trigger('reset');
				deleteAllFiles();//전체 삭제 함수 호출

			} else if (result.dismiss === Swal.DismissReason.cancel) {
				//아무런 작업을 하지 않는다
			}
		});

	});
});



//=============== 유효성 검사 끝 =================

//========================= 이미지 추가 부분 =========================


var tempFiles = [];  // 전역 변수로 생성

// 파일이 등록되었을 때 이벤트 리스너를 추가합니다.
document.querySelector('#addPhoto').addEventListener('change', function(e) {
	var files = e.target.files; // 선택된 파일들을 가져옵니다.


	var files = $(this).get(0).files; // 새로 추가하려는 이미지들
	var maxAllowedFiles = 5; //이미지 최대 등록 수 
	var existingImages = $('.file_info_li').length; // 기존에 등록된 이미지의 수


	for (var i = 0; i < files.length; i++) {
		var file = files[i];
		tempFiles.push(file);  // 파일 정보를 임시 배열에 저장

		// 파일의 MIME 타입을 확인하여 png 또는 jpg인지 검사
		if (file.type !== 'image/png' && file.type !== 'image/jpeg') {
			Swal.fire('png 또는 jpg 이미지만 등록할 수 있습니다.');
			return;  // 이미지가 png 또는 jpg가 아니라면 더 이상 처리하지 않고 함수를 종료
		}
	}


	//만약에 이미지를 5개 이상 한번에 등록하려 한다면 
	if (files.length > maxAllowedFiles) {
		Swal.fire('사진은 최대 5장까지 등록할 수 있습니다')
		// 선택한 파일 초기화
		$(this).val('');
	}

	//만약 기존에 등록된 이미지가 있는데 새로 추가한거와 수를 비교했을 시 5개가 넘을때
	if (existingImages + files.length > maxAllowedFiles) {
		Swal.fire('사진은 최대 5장까지 등록할 수 있습니다')
		// 선택한 파일 초기화
		$(this).get(0).value = '';
	}


	for (var i = 0; i < files.length; i++) { // 선택된 파일들에 대하여 반복
		var file = files[i];
		var reader = new FileReader(); // 파일을 읽기 위한 FileReader 객체를 생성

		reader.onload = (function(file) { // 파일 읽기가 완료되면 실행되는 콜백 함수
			return function(e) {
				var li = document.createElement('li'); // 새로운 <li> 엘리먼트를 생성
				li.className = 'file_info_li'; // 클래스 이름을 설정
				li.innerHTML = `
                    <div class="file_img_and_link">
                        <img class="show_file_img" src="${e.target.result}" alt="${file.name}">
                        <div class="file_link_name">${file.name}</div>
                    </div>
                    <button class="file_img_deleate"  onclick="deleteFileItem(event)">x</button>
                `; // li 내부의 HTML을 설정

				document.querySelector('.file_infos').appendChild(li); // <ul> 안에 새로운 <li> 엘리먼트를 추가
			};
		})(file);



		reader.readAsDataURL(file); // 파일을 읽움. 비동기로 실행


	}



	// 이벤트 처리 후 파일 선택 필드 초기화 => 삭제 후에도 동일한 이름 이미지 올릴 수 있도록
	e.target.value = '';

	//전체 삭제 버튼 클릭시 전체 이미지 삭제
	document.querySelector('.file_list_header_del').addEventListener('click', deleteAllFiles);


});

//전체 삭제 버튼 클릭시 전체 이미지 삭제
function deleteAllFiles() {
	// 모든 <li> 엘리먼트를 선택
	var listItems = document.querySelectorAll('.file_info_li');

	// 모든 <li> 요소를 삭제
	listItems.forEach(function(listItem) {
		listItem.remove();
	});

	// tempFiles 배열 비우기
	tempFiles = [];
}



// 삭제 버튼 클릭 시 해당 파일 항목을 리스트에서 제거 (개당 제거)
function deleteFileItem(event) {
	var listItem = event.target.parentElement;

	console.log(listItem + "listItem");

	// <li> 요소들이 포함된 부모 요소인 리스트(listItems)를 찾아야 함
	var listItems = listItem.parentElement.children;

	// <li> 요소들 중에서 삭제 버튼이 위치한 <li> 요소의 인덱스를 찾음
	var index = Array.prototype.indexOf.call(listItems, listItem);

	console.log(index);

	// tempFiles 배열에서 해당 인덱스의 파일을 삭제합니다.
	tempFiles.splice(index, 1); //이거 전역변수로 하던가 그래야 할거 같은뎅..
	tempFiles.forEach(function(file) {
		console.log("삭제 후 배열 정보들 모음집");
		console.log(file);
	});
	console.log(tempFiles.length)

	// <li> 요소를 삭제하여 UI에서 제거
	listItem.remove();

}


//등록하기 버튼 클릭 이벤트 처리
$(document)
	.ready(
		function() {
			$('#addProductButton')
				.click(
					function() {

						var form = $('#productForm')[0]; //첫 번째 form요소
						var formData = new FormData(form);


						// 'change' 이벤트에서 처리했던 파일들을 다시 가져옴
						var files = document.querySelector('#addPhoto').files;

						// 각각의 파일을 FormData에 추가
						for (var i = 0; i < files.length; i++) {
							var file = files[i];
							formData.append('product_photo', file, file.name);
						}



						for (var i = 0; i < tempFiles.length; i++) {  // 임시 배열에 저장된 파일 정보 사용
							var file = tempFiles[i];
							formData.append('product_photo', file, file.name);
						}


						//방 등록 시 유효성 검사 (해당 사항 불 충족 시 값 전송 안됌)

						//방 등록 유효성 검사 
						if (!isValidProductLetter($('input[name="product_type"]').val())) {
							showError('product_type', '방 이름을 입력해 주세요');
							return; // 유효성 검사에 실패하면 함수 종료
						}


						//공백인 경우 유효성 검사
						if (!isValidProductAmount($('input[name="product_amount"]').val())) {
							showError('product_amount', '금액을 적어주세요');
							return; // 유효성 검사에 실패하면 함수 종료
						}


						//금액이 0인 경우 유효성 검사
						if (!isValidProductZeroAmount($('input[name="product_amount"]').val())) {
							showError('product_amount', '0보다 큰 숫자를 입력해 주세요');
							return; // 유효성 검사에 실패하면 함수 종료
						}


						//상세설명 유효성 검사 
						if (!isValidProductLetter($('input[name="product_detail"]').val())) {
							showError('product_detail', '상세설명을 입력해 주세요');
							return; // 유효성 검사에 실패하면 함수 종료
						}



						//방 갯수 유효성 검사 
						if (!isValidProductAmount($('input[name="product_count"]').val())) {
							showError('product_count', '방 갯수를 입력해 주세요');
							return; // 유효성 검사에 실패하면 함수 종료
						}


						//방 갯수 유효성 검사 
						if (!isValidProductZeroAmount($('input[name="product_count"]').val())) {
							showError('product_count', '0보다 큰 숫자를 입력해 주세요');
							return; // 유효성 검사에 실패하면 함수 종료
						}


						//최대 수용인원 유효성 검사 
						if (!isValidProductAmount($('input[name="product_pernum"]').val())) {
							showError('product_pernum', '최대 수용인원을 입력해 주세요');
							return; // 유효성 검사에 실패하면 함수 종료
						}


						//최대 수용인원 유효성 검사 
						if (!isValidProductZeroAmount($('input[name="product_pernum"]').val())) {
							showError('product_pernum', '0보다 큰 숫자를 입력해 주세요');
							return; // 유효성 검사에 실패하면 함수 종료
						}


						// 이미지 파일이 선택되었는지 확인
						//var files = $('#addPhoto').get(0).files;
						var files = $('.file_info_li').length;
						if (files === 0) {
							//	alert('이미지가 최소 1개가 필요합니다.');
							Swal.fire('이미지는 최소 1개가 필요합니다')
							return; // 이미지가 선택되지 않았으므로 함수 종료
						}


						$
							.ajax({
								url: '/partner/product/addproduct',
								type: 'POST',
								data: formData,
								enctype: 'multipart/form-data',
								processData: false,
								contentType: false,

								success: function(
									response) {

									// 등록 후 form 초기화
									$('#productForm')[0]
										.reset();

									//window.location.reload();

									setTimeout(function() {
										window.location.reload();
									}, 2000);

								},
								error: function(xhr,
									status, error) {
									// 처리 중에 에러가 발생한 경우의 동작 
									console.log(xhr);
									console.log(status);
									console.log(error);
									// 서버에서 반환된 에러 메시지를 가져옴
									var errorMessage = xhr.responseText;
									Swal.fire(errorMessage)

								}
							});
					});
		});





/* =====================  삭제버튼 기능 구현 ===================== */

// 삭제 버튼 클릭 이벤트 처리
$(document).ready(
	function() {
		$('.deleteProduct').click(
			function() {
				var deleteButton = $(this);

				var productId = parseInt(deleteButton.parents('.Product_contents').find('.product_id').text());

				swalWithBootstrapButtons.fire({
					title: '상품삭제',
					text: '상품을 삭제하시겠습니까?',
					icon: 'warning',
					showCancelButton: true,
					confirmButtonText: '확인',
					cancelButtonText: '취소',
					reverseButtons: true
				}).then((result) => {
					if (result.isConfirmed) {

						$.ajax({
							url: '/partner/product/deleteProduct',
							method: 'POST',
							dataType: 'json',
							data: {
								"productId": productId,
							},
							success: function(response) {
								// 삭제 성공 시 동작

								//	deleteButton.closest('.box').remove(); // 저장한 변수 사용
								console.log('AJAX 요청 성공');
								console.log(response);

								Swal.fire('삭제성공', '삭제되었습니다', 'success').then(() => {
									// 알림창이 닫힌 후에 1.5초 후에 이동
									deleteButton.closest('.box').remove(); // 저장한 변수 사용
								});

							},
							error: function(xhr, status, error) {
								console.log(error);
							}
						})

					} else if (result.dismiss === Swal.DismissReason.cancel) {
						// No 버튼을 눌렀을 때의 동작
						// 추가적인 작업을 수행하거나 아무 동작도 하지 않을 수 있습니다.
					}
				});

				/*	// 삭제 여부 확인
					var confirmDelete = confirm("삭제하시겠습니까?");
	
					if (confirmDelete) {
	
						$.ajax({
							url: '/product/deleteProduct',
							method: 'POST',
							dataType: 'json',
							data: {
								"productId": productId,
							},
							success: function(response) {
								// 삭제 성공 시 동작
								deleteButton.closest('.box').remove(); // 저장한 변수 사용
								console.log('AJAX 요청 성공');
								console.log(response);
							},
							error: function(xhr, status, error) {
								console.log(error);
							}
						});
	
					}*/

			});
	});


/*swal*/
const swalWithBootstrapButtons = Swal.mixin({
	customClass: {
		confirmButton: 'btn btn-success',
		cancelButton: 'btn btn-danger'
	}

});




// 이미지 업로드 인풋 필드의 값이 변경되면 실행되는 이벤트 핸들러
/*$('.imageUploadInput').change(function() {

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
			var buttonContainer = $('<div></div>').addClass('imgLists_deleteButton_position');


			// 이미지를 리스트에 추가
			imageContainer.append(image);
			listItem.append(imageContainer);
			listItem.append(buttonContainer);
			imageList.append(listItem);


			// 이미지 파일을 FormData 객체에 추가
			formData.append('images[]', file);


			// FormData 객체에 담긴 값들 출력
			for (var pair of formData.entries()) {
			}
		};

		reader.readAsDataURL(file);
	}
});
*/

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
		console.log(imgUrl);
	}

	prevButton.on('click', goToPrevSlide);
	nextButton.on('click', goToNextSlide);

}





//수정 취소 버튼을 눌렀을 시  ==========================

$(document).on('click', '.cancelProduct', function() {
	var productContainer = $(this).closest('#productContainer');
	var productId = parseInt(productContainer.find('.Product_contents .product_id').text());


	// 이전에 저장한 기존 값들로 각 input 필드 초기화
	productContainer.find('.edit_type').val(originalValues['type']);
	productContainer.find('.edit_detail').val(originalValues['detail']);
	productContainer.find('.edit_count').val(originalValues['count']);
	productContainer.find('.edit_pernum').val(originalValues['pernum']);
	productContainer.find('.edit_amount').val(originalValues['amount']);
	productContainer.find('.edit_checkin').val(originalValues['checkin']);
	productContainer.find('.edit_checkout').val(originalValues['checkout']);

	// 기존 이미지 요소들 초기화
	restoreImages(productContainer, productId);

	// 오류 메세지가 있다면 제거
	clearEditInputError('edit_type');
	clearEditInputError('edit_detail');
	clearEditInputError('edit_amount');
	clearEditInputError('edit_count');
	clearEditInputError('edit_pernum');




	console.log('이미지가 원상복구되었습니다.');

	productContainer.find('.editProduct').show();
	productContainer.find('.deleteProduct').show();
	productContainer
		.find('.editOkProduct, .cancelProduct, .resetProduct , .imgLists_deleteButton')
		.hide();

	// 해당 span 영역 보여주기
	productContainer.find('.product_count, .product_type, .product_detail,.product_pernum, .product_amount ,.checkin, .checkout')
		.show();

	// 기존의 input 영역 숨기기
	productContainer.find('.edit_count, .edit_type, .edit_detail ,.edit_pernum, .edit_amount ,.edit_checkin, .edit_checkout')
		.hide();
});


// 이미지 원상복구
function restoreImages(productContainer, productId) {
	var imageListContainer = productContainer.find('.imgLists .image-list');
	imageListContainer.empty();

	for (var i = 0; i < originalImageUrls.length; i++) {
		var imageInfo = originalImageUrls[i];
		var roomNumber = imageInfo.productId;
		var imageUrl = imageInfo.imageUrl || '';
		var imgId = imageInfo.imgId; // 수정: 변수명 imgId로 변경

		if (roomNumber === productId) {
			var listItem = $('<li><div class="img_list_position"><img src="' + imageUrl + '" alt="Product Image" class="list_img"><span th:text="' + imgId + '" style="display: none;" class="img_id"></span></div><div class="imgLists_deleteButton_position"><button class="imgLists_deleteButton">x</button></div></li>');


			imageListContainer.append(listItem);
			originalImageUrls.splice(i, 1);
			i--; // 인덱스 감소
		}
	}
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