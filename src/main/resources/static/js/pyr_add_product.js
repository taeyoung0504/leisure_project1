
//방 수정하기 버튼 클릭시 해당 하는 곳으로 이동한다. 
function modifyRoom(button) {
	var editButton = $(button);
	var productId = parseInt(editButton.parents('.Product_contents').find('.product_id').text());
	var url = '/product/modifyRoom/' + productId;
	window.location.href = url;
}




//최초 등록시 사진은 최대 5개 까지만 등록 가능하도록 설정
$(document).ready(function() {
	$('#addPhoto').on('change', function() {
		var files = $(this).get(0).files;
		var maxAllowedFiles = 5;

		if (files.length > maxAllowedFiles) {
			alert('객실 사진은 최대 5장까지 등록할 수 있습니다.');
			// 선택한 파일 초기화
			$(this).val('');
		}
	});
});

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
		clearError('product_type');
		clearError('product_amount');
		clearError('product_detail');
		clearError('product_count');
		clearError('product_pernum');
	});
});


//=============== 유효성 검사 끝 =================


//등록하기 버튼 클릭 이벤트 처리
$(document)
	.ready(
		function() {
			$('#addProductButton')
				.click(
					function() {

						var form = $('#productForm')[0]; //첫 번째 form요소
						var formData = new FormData(form);

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
						var files = $('#addPhoto').get(0).files;
						if (files.length === 0) {
							alert('이미지가 최소 1개가 필요합니다.');
							return; // 이미지가 선택되지 않았으므로 함수 종료
						}


						$
							.ajax({
								url: '/product/addproduct',
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

									// 요청 실패 시 실행할 코드
									console
										.log('AJAX 요청 실패');
									console
										.log(xhr.responseText);

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
							url: '/product/deleteProduct',
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

/*
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
});*/

/*swal*/
const swalWithBootstrapButtons = Swal.mixin({
	customClass: {
		confirmButton: 'btn btn-success',
		cancelButton: 'btn btn-danger'
	}

});




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