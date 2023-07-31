//===== 이미지 슬라이드 ==========

$(document).ready(
	function() {

		const productContainers = $('.slideshow-container');

		productContainers.each(function() {
			const slideshowContainer = $(this);
			const images = [];

			$(this).find('.imgContainer')
				.each(
					function() {
						const img = $(this).find('img');
						const imgUrl = img.attr('src');
						var roomId = parseInt($(this)
							.closest('.box').find(
								'.product_id')
							.text());

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

//숫자에 쉼표를 넣어서 단위 확인
$('.product_amount').each(function() {
	var amountText = $(this).text();
	console.log(typeof amountText); //String 
	var amountNumber = parseFloat(amountText.replace(/[^0-9.]/g, '')); //숫자로 변환
	var formattedNumber = amountNumber.toLocaleString(); //통화 기호
	$(this).text(formattedNumber); //변경
});


/*//해당 숙소를 삭제한다. 
function confirmDelete() {
	if (confirm("숙소를 삭제하시겠습니까?")) {
		var accId = $(".acc_id").text();
		console.log(accId);

		$.ajax({
			url: '/product/deleteAcc/' + accId,
			type: 'DELETE',
			success: function(result) {
				// 요청 성공
				window.location.href = '/user/mypage/my_productList';
			},
			error: function(xhr, status, error) {
				// 요청 실패
				console.log('Error: ' + status + ' ' + error);
			}
		});
	}
}*/

//해당 숙소를 삭제한다. 
function confirmDelete() {
	var accId = $(".acc_id").text();
	swalWithBootstrapButtons.fire({
		title: '숙소 삭제',
		text: '정말 숙소를 삭제하시겠습니까?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonText: '확인',
		cancelButtonText: '취소',
		reverseButtons: true
	}).then((result) => {
		if (result.isConfirmed) {

			$.ajax({
				url: '/partner/product/deleteAcc/' + accId,
				type: 'DELETE',
				success: function(result) {
					// 요청 성공
					window.location.href = '/user/mypage/my_productList';
				},
				error: function(xhr, status, error) {
					// 요청 실패
					console.log('Error: ' + status + ' ' + error);
				}
			});
		} else if (result.dismiss === Swal.DismissReason.cancel) {
			// No 버튼을 눌렀을 때의 동작
			// 추가적인 작업을 수행하거나 아무 동작도 하지 않을 수 있습니다.
		}
	});
}


/*swal*/
const swalWithBootstrapButtons = Swal.mixin({
	customClass: {
		confirmButton: 'btn btn-success',
		cancelButton: 'btn btn-danger'
	}

});


