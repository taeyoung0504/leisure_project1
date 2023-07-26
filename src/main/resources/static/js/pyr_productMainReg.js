/*//취소 버튼을 이전 페이지 이동
document
	.getElementById("CancelButton")
	.addEventListener(
		"click",
		function() {
			var confirmed = confirm("변경 사항이 적용되지 않습니다. 이전 페이지로 돌아가시겠습니까?");
			if (confirmed) {
				window.history.back();
			}
		});
		
		*/


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
		var isContentChanged = false; //텍스트 변경 여부를 저장하는 변수

		console.log(isImageChanged);
		console.log(isContentChanged);

		//이미지
		const imageInput = document
			.getElementById('addMainPhoto');
		const mainImgView = document
			.getElementById('mainImgView');
		const partnerComment = document
			.getElementById('partnerComment');

		console.log(imageInput);
		console.log(mainImgView);
		console.log(partnerComment);

		imageInput.addEventListener('change', function(event) {
			const file = event.target.files[0];
			const reader = new FileReader();

			reader.addEventListener('load', function() {
				mainImgView.src = reader.result;
			});

			if (file) {
				reader.readAsDataURL(file);
				isImageChanged = true; // 이미지가 변경되었음을 표시
				console.log(isImageChanged);
			}
		});

		//텍스트 이벤트 감지
		partnerComment.addEventListener('input', function() {


			isContentChanged = true;
			console.log(isContentChanged);
		})



		$('#addButton').click(function(event) {
			event.preventDefault(); // 폼의 기본 동작인 서버로의 전송 방지

			if (isImageChanged || isContentChanged) {
				// 이미지 또는 텍스트 내용이 변경된 경우에만 서버로 전송
				var form = $('#productForm')[0];
				var formData = new FormData(form);

				if (!isImageChanged) {
					// 이미지 파일이 변경되지 않은 경우 formData에서 이미지 파일 필드를 삭제
					formData.delete('partnerMainImg');
				}

				var comment = $('#acc_explain').val();
				var commentLength = comment.length;

				if (commentLength < 10) {
					alert("글자수는 최소 10자입니다.");
					return;
				}

				$.ajax({
					url: '/product/productMainInfo',
					type: 'POST',
					data: formData,
					enctype: 'multipart/form-data',
					processData: false,
					contentType: false,
					success: function(response) {

						window.location.href = '/product/registerRoom/' + response;
						alert("변경되었습니다."); // 변경 성공 알림 메시지


					},
					error: function(xhr, status, error) {
						console.log('AJAX 요청 실패');
						console.log(xhr);
						console.log(status);
						console.log(error);
						console.log(product);
					}
				});
			} else {
				// 글과 이미지가 변경되지 않은 경우 처리 로직 작성
				alert('변경된 사항이 없습니다.');
			}
		});
	});