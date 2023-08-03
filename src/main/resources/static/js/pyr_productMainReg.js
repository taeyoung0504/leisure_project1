/*swal*/
const swalWithBootstrapButtons = Swal.mixin({
	customClass: {
		confirmButton: 'btn btn-success',
		cancelButton: 'btn btn-danger'
	}

});


//취소 버튼을 이전 페이지 이동
document
	.getElementById("CancelButton")
	.addEventListener(
		"click",
		function() {
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



$(document).ready(
	function() {
		var isImageChanged = false; // 이미지 변경 여부를 저장하는 변수


		//이미지
		const imageInput = document
			.getElementById('addMainPhoto');
		const mainImgView = document
			.getElementById('mainImgView');



		//값 변경됨을 감지하기 위함(기존의 값들을 저장)
		var originaAccName = $("#acc_name").val(); //숙소 이름
		var originaAccAddress = $("#acc_address").val(); //숙소 주소
		var originaAccSectors = $("#partner_sectors").val(); //숙박 업종



		//이미지 변경 여부를 표시(변경 후 => true)
		imageInput.addEventListener('change', function(event) {
			const file = event.target.files[0];
			const reader = new FileReader();

			reader.addEventListener('load', function() {
				mainImgView.src = reader.result;
			});

			if (file) {
				reader.readAsDataURL(file);
				isImageChanged = true; // 이미지가 변경되었음을 표시
			}
		});



		//등록하기 버튼을 누름
		$('#addButton').click(function(event) {
			event.preventDefault(); // 폼의 기본 동작인 서버로의 전송 방지

			//유효성 검사
			var accMaxPeople = $('#acc_max_people').val(); //최대인원
			var acc_explain = $('#acc_explain').val(); //사장님 한마디
			var acc_info = $('#acc_info').val(); //숙소 정보

			//현재 값들을 불러온다
			var accName = $('#acc_name').val();
			var accAddress = $('#acc_address').val();
			var accPartnerSec = $('#partner_sectors').val();


			//기존 이름 변경 검사
			if (accName !== originaAccName) {
				Swal.fire('기본 값은 변경 불가능 합니다')
				location.reload();
				return;
			}

			if (accAddress !== originaAccAddress) {
				Swal.fire('기본 값은 변경 불가능 합니다')
				location.reload();
				return;
			}


			if (accPartnerSec !== originaAccSectors) {
				Swal.fire('기본 값은 변경 불가능 합니다')
				location.reload();
				return;
			}


			if (accMaxPeople.trim() === '') {
				//최대인원이 공백인 경우
				Swal.fire('최대인원을 입력해 주세요')
				return;
			}

			if (accMaxPeople <= 0) {
				//alert('최대 인원은 0 보다 커야 합니다.');
				Swal.fire('최대 인원은 0 보다 커야 합니다')
				return;
			}


			if (acc_explain.length < 10) {
				//최소 글자수
				Swal.fire('글자 수는 최소 10자입니다')
				return;
			}

			if (acc_info.trim() === '') {
				//숙소 정보가 공백인 경우
				Swal.fire('숙소 정보를 입력해 주세요')
				return;
			}

			if (isImageChanged) {
				// 이미지가 변경되었다면
				var form = $('#productForm')[0];
				var formData = new FormData(form);

				if (!isImageChanged) {
					// 이미지 파일이 변경되지 않은 경우 formData에서 이미지 파일 필드를 삭제
					formData.delete('partnerMainImg');
				}

				$.ajax({
					url: '/partner/product/productMainInfo',
					type: 'POST',
					data: formData,
					enctype: 'multipart/form-data',
					processData: false,
					contentType: false,
					success: function(response) {

						Swal.fire('수정성공', '수정 완료되었습니다', 'success').then(() => {
							window.location.href = '/partner/product/registerRoom/' + response;
						});

					},
					error: function(xhr) {
						Swal.fire(xhr.responseText) // 수정 실패 알림 메세지
						window.location.href = '/partner/product/registerRoom/' + response;
					}
				});
			} else {

				// 이미지가 변경되었다면
				var form = $('#productForm')[0];
				var formData = new FormData(form);

				$.ajax({
					url: '/partner/product/productMainInfo',
					type: 'POST',
					data: formData,
					enctype: 'multipart/form-data',
					processData: false,
					contentType: false,
					success: function(response) {

						Swal.fire('수정성공', '수정 완료되었습니다', 'success').then(() => {
							window.location.href = '/partner/product/registerRoom/' + response;
						});

					},
					error: function(xhr) {
						Swal.fire(xhr.responseText) // 수정 실패 알림 메세지
						window.location.href = '/partner/product/registerRoom/' + response;
					}
				});

			}
		});
	});