/*swal*/
const swalWithBootstrapButtons = Swal.mixin({
	customClass: {
		confirmButton: 'btn btn-success',
		cancelButton: 'btn btn-danger'
	}

});


//취소 버튼
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
					window.history.back();
				} else if (result.dismiss === Swal.DismissReason.cancel) {
					
				}
			});

		});



$(document).ready(
	function() {
		var isImageChanged = false; // 이미지 변경 여부

		//이미지
		const imageInput = document 
			.getElementById('addMainPhoto');
		const mainImgView = document 
			.getElementById('mainImgView');



		//기존의 값들을 저장
		var originaAccName = $("#acc_name").val(); 
		var originaAccAddress = $("#acc_address").val(); 
		var originaAccSectors = $("#partner_sectors").val(); 


		//이미지 변경 여부를 표시(변경 후 => true)
		imageInput.addEventListener('change', function(event) {
			let file = event.target.files[0];
			let reader = new FileReader();

			reader.addEventListener('load', function() {
				mainImgView.src = reader.result;
			});

			if (file) {
				reader.readAsDataURL(file);
				isImageChanged = true; 
			}
		});


		//등록하기 버튼을 누름
		$('#addButton').click(function(event) {
			event.preventDefault(); 

			//유효성 검사
			var accMaxPeople = $('#acc_max_people').val(); 
			var acc_explain = $('#acc_explain').val(); 
			var acc_info = $('#acc_info').val(); 

			//현재 값을 불러옴
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
				Swal.fire('최대인원을 입력해 주세요')
				return;
			}

			if (accMaxPeople <= 0) {
				Swal.fire('최대 인원은 0 보다 커야 합니다')
				return;
			}


			if (acc_explain.length < 10) {
				Swal.fire('글자 수는 최소 10자입니다')
				return;
			}

			if (acc_info.trim() === '') {
				Swal.fire('숙소 정보를 입력해 주세요')
				return;
			}

			var form = $('#productForm')[0];
			var formData = new FormData(form);

			if (!isImageChanged) {
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
					Swal.fire(xhr.responseText); 
					window.location.href = '/partner/product/registerRoom/' + response;
				}
			});

					
		});
	});