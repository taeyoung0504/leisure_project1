
/*swal*/
const swalWithBootstrapButtons = Swal.mixin({
	customClass: {
		confirmButton: 'btn btn-success',
		cancelButton: 'btn btn-danger'
	}

});



//방 수정하기 버튼 클릭시 해당 하는 곳으로 이동한다. 
function modifyRoom(button) {
	var editButton = $(button);
	var productId = parseInt(editButton.parents('.Product_contents').find('.product_id').text());
	var url = '/partner/product/modifyRoom/' + productId;
	window.location.href = url;
}

//유효성 검사
$(document).ready(function() {
	// 입력 필드 값 변경 이벤트 처리
	$('#productForm input').on('input', function() {
		// 값이 변경된 입력 필드의 처리 로직을 작성합니다.
		var fieldName = $(this).attr('name');
		var fieldValue = $(this).val();

		validateField(fieldName, fieldValue)

	});
});


// 유효성 검사 함수 메세지(메세지를 출력)
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


// 방 등록 값 유효성 검사 (빈 공백)
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
				//아무런 작업을 하지 않는다
			}
		});

	});
});




//이미지 추가

var tempFiles = [];  // 전역 변수로 생성

// 파일이 등록되었을 때 이벤트 리스너를 추가합니다.
document.querySelector('#addPhoto').addEventListener('change', function(e) {
	var files = e.target.files; // 선택된 파일들을 가져옵니다.


	var files = $(this).get(0).files; // 새로 추가하려는 이미지
	var maxAllowedFiles = 5; //이미지 최대 등록 수 
	var existingImages = $('.file_info_li').length; // 기존에 등록된 이미지의 수



	//만약에 이미지를 5개 이상 한번에 등록하려 한다면 
	if (files.length > maxAllowedFiles) {
		Swal.fire('사진은 최대 5장까지 등록할 수 있습니다')
		// 선택한 파일 초기화
		$(this).val('');
		return;  // 즉시 함수를 종료
	}

	//만약 기존에 등록된 이미지가 있는데 새로 추가한거와 수를 비교했을 시 5개가 넘을때
	if (existingImages + files.length > maxAllowedFiles) {
		Swal.fire('사진은 최대 5장까지 등록할 수 있습니다')
		// 선택한 파일 초기화
		$(this).get(0).value = '';
		return;  // 즉시 함수를 종료

	}

	for (var i = 0; i < files.length; i++) {
		var file = files[i];
		tempFiles.push(file);  // 파일 정보를 임시 배열에 저장

		// 파일의 MIME 타입을 확인하여 png 또는 jpg인지 검사
		if (file.type !== 'image/png' && file.type !== 'image/jpeg') {
			Swal.fire('png 또는 jpg 이미지만 등록할 수 있습니다.');
			return;  // 이미지가 png 또는 jpg가 아니라면 더 이상 처리하지 않고 함수를 종료
		}
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


	// <li> 요소들이 포함된 부모 요소인 리스트(listItems)를 찾아야 함
	var listItems = listItem.parentElement.children;

	// <li> 요소들 중에서 삭제 버튼이 위치한 <li> 요소의 인덱스를 찾음
	var index = Array.prototype.indexOf.call(listItems, listItem);


	// tempFiles 배열에서 해당 인덱스의 파일을 삭제합니다.
	tempFiles.splice(index, 1);

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

								success: function() {

									// 등록 후 form 초기화
									$('#productForm')[0]
										.reset();
									//페이지 reload	
									window.location.reload();

								},
								error: function(xhr) {
									// 서버에서 반환된 에러 메시지를 가져옴
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

				//해당 상품의 id 를 찾음
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
							dataType: 'text', //text로 바꿔야지 swal 적용가능
							data: {
								"productId": productId,
							},
							success: function(response) {
								// 삭제 성공 시 동작
								Swal.fire('삭제 성공', response, 'success').then(() => {
									deleteButton.closest('.box').remove();
								});
							},
							error: function(xhr) {
								Swal.fire(xhr.responseText);//실패 알림 메세지
							}
						})

					} else if (result.dismiss === Swal.DismissReason.cancel) {
						//취소 버튼 클릭시 아무런 동작을 하지 않음
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



//숫자에 쉼표를 넣어서 단위 확인
$('.product_amount').each(
	function() {
		var amountText = $(this).text();
		var amountNumber = parseFloat(amountText.replace(
			/[^0-9.]/g, '')); //숫자로 변환
		var formattedNumber = amountNumber.toLocaleString(); //통화 기호
		$(this).text(formattedNumber); //변경
	});