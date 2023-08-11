
/*swal*/
const swalWithBootstrapButtons = Swal.mixin({
	customClass: {
		confirmButton: 'btn btn-success',
		cancelButton: 'btn btn-danger'
	}

});



//방 수정하기 이동
function modifyRoom(button) {
	var editButton = $(button);
	var productId = parseInt(editButton.parents('.Product_contents').find('.product_id').text());
	var url = '/partner/product/modifyRoom/' + productId;
	window.location.href = url;
}

//productForm에 input 이벤트 발생시 검사
$(document).ready(function() {

	$('#productForm input').on('input', function() {

		var fieldName = $(this).attr('name');
		var fieldValue = $(this).val();

		validateField(fieldName, fieldValue)

	});
});


// 유효성 검사 함수 
function validateField(fieldName, fieldValue) {

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


// 빈 공백 검사
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
		// 오류 메시지 엘리먼트가 없으면 생성
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

				deleteAllFiles();//이미지 전체 삭제 함수 호출

			} else if (result.dismiss === Swal.DismissReason.cancel) {

			}
		});

	});
});




//이미지 파일 저장 배열(전역 변수)
var tempFiles = [];

// 파일이 등록되었을 때 이벤트 리스너를 추가
document.querySelector('#addPhoto').addEventListener('change', function(e) {
	var files = e.target.files;


	var files = $(this).get(0).files; // 새로 추가하려는 이미지
	var maxAllowedFiles = 5; //이미지 최대 등록 수 
	var existingImages = $('.file_info_li').length; // 기존에 등록된 이미지의 수



	//이미지 5개 이상 한번에 등록
	if (files.length > maxAllowedFiles) {
		Swal.fire('사진은 최대 5장까지 등록할 수 있습니다')
		$(this).val('');
		return;
	}

	//기존 이미지 수 + 새로 추가한 이미지 수 5이하인지 확인
	if (existingImages + files.length > maxAllowedFiles) {
		Swal.fire('사진은 최대 5장까지 등록할 수 있습니다')
		$(this).get(0).value = '';
		return;
	}

	for (var i = 0; i < files.length; i++) {
		var file = files[i];
		tempFiles.push(file);

		//png 또는 jpg인지 검사
		if (file.type !== 'image/png' && file.type !== 'image/jpeg') {
			Swal.fire('png 또는 jpg 이미지만 등록할 수 있습니다');
			return;
		}
	}

	//새로 추가된 img 파일 보여준다
	for (var i = 0; i < files.length; i++) {
		var file = files[i];
		var reader = new FileReader();

		reader.onload = (function(file) {
			return function(e) {
				var li = document.createElement('li');
				li.className = 'file_info_li';
				li.innerHTML = `
                    <div class="file_img_and_link">
                        <img class="show_file_img" src="${e.target.result}" alt="${file.name}">
                        <div class="file_link_name">${file.name}</div>
                    </div>
                    <button class="file_img_deleate"  onclick="deleteFileItem(event)">x</button>
                `;

				document.querySelector('.file_infos').appendChild(li);
			};
		})(file);

		reader.readAsDataURL(file);
	}

	// 이벤트 처리 후 파일 선택 필드 초기화
	e.target.value = '';

	//전체 삭제 버튼
	document.querySelector('.file_list_header_del').addEventListener('click', deleteAllFiles);

});

//전체 삭제 버튼 함수
function deleteAllFiles() {

	var listItems = document.querySelectorAll('.file_info_li');

	listItems.forEach(function(listItem) {
		listItem.remove();
	});

	//이미지 저장 배열 비우기
	tempFiles = [];
}


// 개당 이미지 제거
function deleteFileItem(event) {
	var listItem = event.target.parentElement;

	var listItems = listItem.parentElement.children;

	//몇 번째 이미지인지 찾음	
	var index = Array.prototype.indexOf.call(listItems, listItem);

	tempFiles.splice(index, 1);

	listItem.remove();

}


//등록하기 버튼 클릭
$(document)
	.ready(
		function() {
			$('#addProductButton')
				.click(
					function() {

						var form = $('#productForm')[0];
						var formData = new FormData(form);

						// 'change' 이벤트에서 처리했던 파일들을 다시 가져옴
						var files = document.querySelector('#addPhoto').files;


						//tempFiles 에 있는 이미지 
						for (var i = 0; i < tempFiles.length; i++) {
							var file = tempFiles[i];
							formData.append('product_photo', file, file.name);
						}

						//유효성 검사
						var validations = [
							{ field: 'product_type', validator: isValidProductLetter, errorMessage: '방 이름을 입력해 주세요' },
							{ field: 'product_amount', validator: isValidProductAmount, errorMessage: '금액을 적어주세요' },
							{ field: 'product_amount', validator: isValidProductZeroAmount, errorMessage: '0보다 큰 숫자를 입력해 주세요' },
							{ field: 'product_detail', validator: isValidProductLetter, errorMessage: '상세설명을 입력해 주세요' },
							{ field: 'product_count', validator: isValidProductAmount, errorMessage: '방 갯수를 입력해 주세요' },
							{ field: 'product_count', validator: isValidProductZeroAmount, errorMessage: '0보다 큰 숫자를 입력해 주세요' },
							{ field: 'product_pernum', validator: isValidProductAmount, errorMessage: '최대 수용인원을 입력해 주세요' },
							{ field: 'product_pernum', validator: isValidProductZeroAmount, errorMessage: '0보다 큰 숫자를 입력해 주세요' }
						];

						for (var i = 0; i < validations.length; i++) {
							var validation = validations[i];
							var value = $('input[name="' + validation.field + '"]').val();
							if (!validation.validator(value)) {
								showError(validation.field, validation.errorMessage);
								return;
							}
						}

						// 이미지 파일이 선택되었는지 확인
						var files = $('.file_info_li').length;
						if (files === 0) {
							Swal.fire('이미지는 최소 1개가 필요합니다')
							return;
						}

						$
							.ajax({
								url: '/partner/product/addproduct',
								type: 'POST',
								data: formData,
								enctype: 'multipart/form-data',
								processData: false,
								contentType: false,

								success: function() {

									// 등록 후 form 초기화
									$('#productForm')[0]
										.reset();

									window.location.reload();

								},
								error: function(xhr) {
									var errorMessage = xhr.responseText;
									Swal.fire(errorMessage)

								}
							});
					});
		});



// 객실삭제 버튼 클릭 이벤트 처리
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
							dataType: 'text',
							data: {
								"productId": productId,
							},
							success: function(response) {
								Swal.fire('삭제 성공', response, 'success').then(() => {
									deleteButton.closest('.box').remove();
								});
							},
							error: function(xhr) {
								Swal.fire(xhr.responseText);
							}
						})

					} else if (result.dismiss === Swal.DismissReason.cancel) {

					}
				});
			});
	});


//이미지 슬라이드
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

//이미지 슬라이드 함수
function startSlideshow(slideshowContainer, images) {
	let currentSlideIndex = 0;
	const slide = slideshowContainer.find('.slide');
	const prevButton = slideshowContainer.find('.prev');
	const nextButton = slideshowContainer.find('.next');


	// 첫 번째 이미지로 초기화
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

	// 슬라이드 업데이트 함수 (에니메이션 효과와 함께 슬라이드를 업데이트)
	function updateSlide() {
		const imgUrl = images[currentSlideIndex].imgUrl;
		slide.find('img').attr('src', imgUrl);
	}

	prevButton.on('click', goToPrevSlide);
	nextButton.on('click', goToNextSlide);
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