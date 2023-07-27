

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


/*swal*/
const swalWithBootstrapButtons = Swal.mixin({
	customClass: {
		confirmButton: 'btn btn-success',
		cancelButton: 'btn btn-danger'
	}

});



$(document).ready(
	function() {
		var isImageChanged = false; // 이미지 변경 여부를 저장하는 변수
		var isAccExplainChanged = false; //사장님 한마디 여부를 저장


		//이미지
		const imageInput = document
			.getElementById('addMainPhoto');
		const mainImgView = document
			.getElementById('mainImgView');


		//사장님 한마디 
		const acc_explain = document.getElementById('acc_explain');

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

		acc_explain.addEventListener('input', function(event) {
			const content = event.target.value;

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
				//alert('숙소 이름을 입력해주세요.');
				Swal.fire('숙소 이름을 입력해주세요')
				return;
			}

			if (accAddress.trim() === '') {
				alert('주소를 입력해주세요.');
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
				url: '/product/productNewMainReg',
				type: 'POST',
				data: formData,
				enctype: 'multipart/form-data',
				processData: false,
				contentType: false,
				success: function(response) {

					Swal.fire({
						title: '숙소 등록이 완료되었습니다',
						icon: 'success',
						confirmButtonText: '확인'
					}).then(() => {
						window.location.href = '/user/mypage/my_productList';
					});


				},
				error: function(xhr, status, error) {
					console.log('AJAX 요청 실패');
					console.log(xhr);
					console.log(status);
					console.log(error);
					console.log(product);

					alert("변경에 실패하였습니다."); // 변경 실패 알림 메세지
					window.location.href = '/user/mypage/my_productList';
				}
			});

		});
	});






