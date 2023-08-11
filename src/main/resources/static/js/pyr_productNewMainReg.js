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

		var originaAccName = $("#acc_name").val(); 
		var originaAccAddress = $("#acc_address").val(); 
		var originaAccSectors = $("#partner_sectors").val(); 

		//값 변경됨을 감지
		var isImageChanged = false; // 이미지 변경 여부
		var isAccExplainChanged = false; //사장님 한마디 변경 여부

		//이미지
		const imageInput = document
			.getElementById('addMainPhoto'); 
		const mainImgView = document
			.getElementById('mainImgView'); 

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
				isImageChanged = true; 
			}
		});

		acc_explain.addEventListener('input', function(event) {
			let content = event.target.value;

			if (content) {
				isAccExplainChanged = true;
			} else {
				isAccExplainChanged = false;
			}
		});



		$('#addButton').click(function(event) {

			event.preventDefault(); 

			var form = $('#productForm')[0]; 
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
				Swal.fire('최대인원을 입력해 주세요')
				return;
			}

			if (accMaxPeople <= 0) {
				Swal.fire('최대 인원은 0 보다 커야 합니다')
				return;
			}

			if (accPartnerSec === null) {
				alert('업종을 선택해 주세요');
				return;
			}

			if (!isImageChanged) {
				Swal.fire('이미지를 선택해 주세요')
				return;
			}

			if (!isAccExplainChanged) {
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
					Swal.fire(xhr.responseText) 
					window.location.href = '/user/mypage/my_productList';
				}
			});

		});
	});









