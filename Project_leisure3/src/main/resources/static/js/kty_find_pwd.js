	$(document).ready(function () {
			var $checkEmail = $('#send_temppwd');
			var $email = $('#find_email');
			var $username = $('#find_username');

			$checkEmail.on('click', function () {
				var email = $email.val();
				var username = $username.val();
				if (email.trim() !== '') {
					$.ajax({
						url: "/user/temp_pwd",
						type: "POST",
						data: {
							"email": email,
							"username": username
						},
						success: function (response) {
							var data = response; // 인증번호 데이터 저장
							Swal.fire({
								ttitle: '<span class="centered-title">Success</span>',
								html: '<b>임시비밀번호가 발송되었습니다.</b>',
								icon: 'success'
							});

						},
						error: function (xhr, status, error) {
							Swal.fire({
								title: 'Error!',
								html: '<b>회원이 아니거나 아이디와 이메일이 맞지 않습니다.</b>',
								icon: 'error'
							});
						}
					});
				}
			});
		});