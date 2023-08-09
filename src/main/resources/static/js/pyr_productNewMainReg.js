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
					// 확인 누를 시 이전 페이지로 이동
					window.history.back();
				} else if (result.dismiss === Swal.DismissReason.cancel) {
					// 취소 버튼 동작 => 아무런 동작 없음
				}
			});

		});


$(document).ready(
	function() {

		var originaAccName = $("#acc_name").val(); //숙소 이름
		var originaAccAddress = $("#acc_address").val(); //숙소 주소
		var originaAccSectors = $("#partner_sectors").val(); //숙박 업종

		//값 변경됨을 감지하기 위함(입력값이 있으면 등록 가능)
		var isImageChanged = false; // 이미지 변경 여부를 저장하는 변수
		var isAccExplainChanged = false; //사장님 한마디 변경 여부를 저장

		//이미지
		const imageInput = document
			.getElementById('addMainPhoto'); //이미지 등록
		const mainImgView = document
			.getElementById('mainImgView'); //이미지 보여주는 부분


		//사장님 한마디 
		let acc_explain = document.getElementById('acc_explain');

		//이미지 변경
		imageInput.addEventListener('change', function(event) {
			let file = event.target.files[0];
			let reader = new FileReader();

			reader.addEventListener('load', function() {
				mainImgView.src = reader.result;
			});

			if (file) {
				reader.readAsDataURL(file);
				isImageChanged = true; // 이미지가 변경되었음을 표시
			}
		});

		acc_explain.addEventListener('input', function(event) {
			let content = event.target.value;

			if (content) {
				//사장님 한마디 입력값 변경되면 true	
				isAccExplainChanged = true;
			} else {
				//사장님 한마디 입력값 변경 안되면 false
				isAccExplainChanged = false;
			}
		});



		$('#addButton').click(function(event) {

			event.preventDefault(); // 폼의 기본 동작인 서버로의 전송 방지

			var form = $('#productForm')[0]; //첫 번째 form요소
			var formData = new FormData(form);


			//유효성 검사
			var accName = $('#acc_name').val();
			var accAddress = $('#acc_address').val();
			var accMaxPeople = $('#acc_max_people').val();
			var accPartnerSec = $('#partner_sectors').val();
			var acc_explain = $('#acc_explain').val();


			if (accName.trim() === '') {
				Swal.fire('숙소 이름을 입력해주세요')
				return;
			}


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



			if (accAddress.trim() === '') {
				Swal.fire('주소를 입력해 주세요')
				return;
			}

			if (accMaxPeople.trim() === '') {
				//alert('최대인원을 입력해 주세요');
				Swal.fire('최대인원을 입력해 주세요')
				return;
			}

			if (accMaxPeople <= 0) {
				//alert('최대 인원은 0 보다 커야 합니다.');
				Swal.fire('최대 인원은 0 보다 커야 합니다')
				return;
			}

			if (accPartnerSec === null) {
				alert('업종을 선택해 주세요');
				return;
			}


			if (!isImageChanged) {
				// 이미지 파일이 변경되지 않은 경우 formData에서 이미지 파일 필드를 삭제
				//alert('이미지를 선택해 주세요.');
				Swal.fire('이미지를 선택해 주세요')
				return;
			}

			if (!isAccExplainChanged) {
				//사장님 한마디에 아무런 입력값이 없는 경우
				Swal.fire('업소에 대한 설명을 작성해 주세요')
				return;
			}

			if (acc_explain.length < 10) {
				Swal.fire('글자 수는 최소 10자입니다')
				return;
			}

			$.ajax({
				url: '/partner/product/productNewMainReg',
				type: 'POST',
				data: formData,
				enctype: 'multipart/form-data',
				processData: false,
				contentType: false,
				success: function() {

					Swal.fire({
						title: '숙소 등록이 완료되었습니다',
						icon: 'success',
						confirmButtonText: '확인'
					}).then(() => {
						window.location.href = '/user/mypage/my_productList';
					});
				},
				error: function(xhr) {
					Swal.fire(xhr.responseText) // 등록 실패 알림 메세지
					window.location.href = '/user/mypage/my_productList';
				}
			});

		});
	});









