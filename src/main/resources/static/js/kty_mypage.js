$(function() {
	/* 비밀번호 변경 활성화 */
	$(document).ready(function() {
		$("#mypage_password_modify").on("click", function() {
			var modifyEmailInput = $("#modify_password_input");
			if (modifyEmailInput.css("display") === "none") {
				modifyEmailInput.css("display", "block");
			} else {
				modifyEmailInput.css("display", "none");
			}
		});
	});

	/* 주소 변경 활성화 * /
	/* 비밀번호 변경 활성화 */
	$(document).ready(function() {
		$("#mypage_addr_modify").on("click", function() {
			var modifyEmailInput = $("#modify_address_input");
			if (modifyEmailInput.css("display") === "none") {
				modifyEmailInput.css("display", "block");
			} else {
				modifyEmailInput.css("display", "none");
			}
		});
	});





	/* 페이지 새로 고침 시 프로필 사진 변경 */
	$(document).ready(function() {
		var profileImages = [
			"pro_img1.jpg",
			"pro_img2.jpg",
			"pro_img3.jpg",
			"pro_img4.jpg",
			"pro_img5.jpg"
		];


		var randomIndex = Math.floor(Math.random() * profileImages.length);


		$("#profile_lion").attr("src", "/img/profile_img/" + profileImages[randomIndex]);
	});







	var $button_addr_modify = $('#modifyaddr');
	var $input_modifyaddr1 = $('#modify_addr1');
	var $input_modifyaddr2 = $('#modify_addr2');
	var $input_modifyaddr3 = $('#modify_addr3');

	$button_addr_modify.on('click', function() {
		var a1 = $input_modifyaddr1.val();
		var a2 = $input_modifyaddr2.val();
		var a3 = $input_modifyaddr3.val();
		if (a1.trim() !== '' && a3.trim() !== '') {
			$.ajax({
				url: "/user/updateaddr",
				type: "POST",
				data: {
					modify_addr1: a1,
					modify_addr2: a2,
					modify_addr3: a3
				},
				success: function(response) {
					Swal.fire({
						title: 'Error!',
						html: '<b>인증번호 발송에 실패하였습니다 다시 시도해 주세요</b>',
						icon: 'error'
					});
					setTimeout(function() {
						location.reload(); // 1초 후에 페이지를 자동으로 새로고침
					}, 500); // 1초 (1000 밀리초) 후에 실행
				},
				error: function(xhr, status, error) {
					setTimeout(function() {
						location.reload(); // 3초 후에 페이지를 자동으로 새로고침
					}, 500); // 3초 (3000 밀리초) 후에 실행
				}
			});
		}
	});

});




function execPostCode2() {
	new daum.Postcode({
		oncomplete: function(data) {
			// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

			// 도로명 주소의 노출 규칙에 따라 주소를 조합한다.
			// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
			var fullRoadAddr = data.roadAddress; // 도로명 주소 변수
			var extraRoadAddr = ''; // 도로명 조합형 주소 변수

			// 법정동명이 있을 경우 추가한다. (법정리는 제외)
			// 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
			if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
				extraRoadAddr += data.bname;
			}
			// 건물명이 있고, 공동주택일 경우 추가한다.
			if (data.buildingName !== '' && data.apartment === 'Y') {
				extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
			}
			// 도로명, 지번 조합형 주소가 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
			if (extraRoadAddr !== '') {
				extraRoadAddr = ' (' + extraRoadAddr + ')';
			}
			// 도로명, 지번 주소의 유무에 따라 해당 조합형 주소를 추가한다.
			if (fullRoadAddr !== '') {
				fullRoadAddr += extraRoadAddr;
			}

			// 우편번호와 주소 정보를 해당 필드에 넣는다.
			//console.log(data.zonecode);
			//console.log(fullRoadAddr);


			$("[id=modify_addr1]").val(data.zonecode);
			$("[id=modify_addr2]").val(fullRoadAddr);

			/* document.getElementById('signUpUserPostNo').value = data.zonecode; //5자리 새우편번호 사용
			document.getElementById('signUpUserCompanyAddress').value = fullRoadAddr;
			document.getElementById('signUpUserCompanyAddressDetail').value = data.jibunAddress; */
		}
	}).open();
}



$(document).ready(function() {
	var $inputOldPwd = $('#modify_password');
	var $oldPwdError = $('#confirm_oldPwd');
	var $NewPwdError = $('#confirm_newPwd');
	var $NewPwdError1 = $('#confirm_newPwd1');
	var $inputNewPwd1 = $('#modify_password2_input');
	var $inputNewPwd2 = $('#modify_password3_input');
	var $modifyEmailBtn = $('.modifyEmail');

	$inputOldPwd.on('input focusout', function() {
		var oldPwd = $inputOldPwd.val();

		if (oldPwd === '') {
			disableNewPwdInputs();
		} else {
			$.ajax({
				type: 'POST',
				url: '/user/check_oldpwd',
				data: { modify_password: oldPwd },
				dataType: 'json',
				success: function(data) {
					if (data === 1) {
						$oldPwdError.text('비밀번호가 일치합니다').show();
						enableNewPwdInputs();
					} else {
						$oldPwdError.text('비밀번호가 일치하지 않습니다').show();
						disableNewPwdInputs();
					}
				},
				error: function(xhr, status, error) {
					console.log(error);
				}
			});
		}
	});

	function enableNewPwdInputs() {
		
		if ($inputOldPwd.val() === $inputNewPwd1.val()) {
			$inputNewPwd1.prop('disabled', false);
			$inputNewPwd2.prop('disabled', true);
			$NewPwdError.text('').hide();
		} else {
			$inputNewPwd1.prop('disabled', false);
			validateNewPwd();
		}
	}

	function validateNewPwd() {
    var newPassword = $inputNewPwd1.val();
    var passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,12}$/;
    var oldPwd = $inputOldPwd.val();

    if (newPassword === oldPwd) {
        $NewPwdError1.text('기존 비밀번호와 같습니다').show();
        $NewPwdError.hide();
        $inputNewPwd2.prop('disabled', true);
        $modifyEmailBtn.prop('disabled', true); // Disable the button when passwords are the same
    } else if (passwordRegex.test(newPassword)) {
        $NewPwdError1.text('사용 가능한 비밀번호입니다').show();
        $inputNewPwd2.prop('disabled', false);
        $modifyEmailBtn.prop('disabled', false); // Enable the button when the new password is valid
    } else {
        $NewPwdError1.text('문자,숫자,특수기호 포함 6-12자리 X').show();
        $NewPwdError.hide();
        $inputNewPwd2.prop('disabled', true);
        $modifyEmailBtn.prop('disabled', true); // Disable the button when the new password is invalid
    }

    validateBothPwdInputs();
}


	function validateBothPwdInputs() {
		if (
			$inputNewPwd1.val() !== '' &&
			$inputNewPwd2.val() !== '' &&
			$inputNewPwd1.val() === $inputNewPwd2.val()
		) {
			$modifyEmailBtn.prop('disabled', false);
			$NewPwdError.text('변경 가능한 비밀번호 입니다.').show();
		} else {
			$modifyEmailBtn.prop('disabled', true);
			$NewPwdError.text('새 비밀번호와 입력값이 일치하지 않습니다').show();
		} 
	}

	function disableNewPwdInputs() {
		$inputNewPwd1.val('');
		$inputNewPwd2.val('');
		$inputNewPwd1.prop('disabled', true);
		$inputNewPwd2.prop('disabled', true);
		$modifyEmailBtn.prop('disabled', true);
		$NewPwdError.text('').hide();
	}

	$inputNewPwd1.on('input', function() {
		validateNewPwd();
	});
	$inputNewPwd2.on('input', function() {
		validateBothPwdInputs();
	});

	$modifyEmailBtn.click(function() {
    // Check if any of the input fields is empty
    if ($inputOldPwd.val() === '' || $inputNewPwd1.val() === '' || $inputNewPwd2.val() === '') {
        return; // Return early, don't execute the rest of the code
    }

    var newPassword = $inputNewPwd1.val();

    $.ajax({
        type: 'POST',
        url: '/user/update_pwd',
        data: { modify_password2: newPassword },
        success: function(response) {
            Swal.fire({
                title: '<span class="centered-title">Success</span>',
                html: '<b>비밀번호 변경하였습니다. 다시 로그인하세요</b>',
                icon: 'success'
            });
            setTimeout(function() {
                window.location.href = '/user/logout';
            }, 2000);
        },
        error: function(xhr, status, error) {
            console.error(error);
        }
    });
});
});









$(document).ready(function() {
	$("#reg_partner_final").on("click", function() {
		var a1 = $('#company_name').val();
		var a2 = $('#company_address').val();
		var a3 = $('#partner_name').val();
		var a4 = $('#partner_tel').val();
		var a5 = $('#partner_sectors').val();
		var a6 = $('#partner_region').val();
		var file = $('#partner_input-file')[0].files[0];


		if (a1 === '' || a2 === '' || a3 === '' || a4 === '' || a5 === '' || file === undefined) {
			$("#reg_partner_final").val("필수값 미입력").css({ "color": "red", "font-weight": "bold" });
			return;
		} else {
			$("#reg_partner_final").val("파트너 신청하기").css("color", "");
		}

		var formData = new FormData();
		formData.append("company_name", a1);
		formData.append("company_address", a2);
		formData.append("partner_name", a3);
		formData.append("partner_tel", a4);
		formData.append("partner_sectors", a5);
		formData.append("partner_region", a6);
		formData.append("file", file);
		//sss
		$.ajax({
			url: "/user/reg_p",
			type: "POST",
			data: formData,
			contentType: false,
			processData: false,
			success: function(response) {
				Swal.fire({
					position: 'center',
					title: '<span class="centered-title">Success</span>',
					html: '<b>파트너 신청 완료되었습니다.</b><br><b>검토 후 개별연락으로 결과 전달드립니다.</b>',
					icon: 'success'
				}).then(function() {
					window.location.href = 'http://192.168.10.67:8080/user/mypage/my_partner_reg';
				});
			},
			error: function(xhr, status, error) {
				Swal.fire({
					position: 'center',
					title: '<span class="centered-title">Success</span>',
					html: '<b>파트너 신청 완료되었습니다.</b><br><b>검토 후 개별연락으로 결과 전달드립니다. </b>',
					icon: 'success'
				}).then(function() {

					window.location.href = 'http://192.168.10.67:80/user/mypage/my_partner_reg';
				});
			}
		});
	});
});
/* 인감증명서 파일 첨부 */

$(document).ready(function() {
	$('#partner_input-file').on('change', function() {
		var file = this.files[0];
		if (file) {
			var validExtensions = ["png", "pdf", "jpg"];
			var fileExtension = file.name.split('.').pop().toLowerCase();
			if ($.inArray(fileExtension, validExtensions) === -1) {
				alert("올바른 파일 확장자를 선택하세요.");
				$(this).val(null); // 선택한 파일 초기화
				$('#ch_file').text('파일 선택을 선택하세요( 이미지 또는 pdf 파일 첨부)');
			} else {
				$('#ch_file').text(file.name).css('color', 'black');
			}
		}
	});
});

