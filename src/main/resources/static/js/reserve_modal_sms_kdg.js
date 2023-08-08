// 인증번호 번호 입력란에 keyup 이벤트 리스너를 등록
								    	document.getElementById('digit').addEventListener('keyup', function() {
								        // 입력된 인증번호를 가져옴
								        var codeNumber = this.value;
										
								        // 입력된 인증번호 체크
								        if (isNaN(codeNumber) || codeNumber.indexOf(' ') !== -1
								        		|| codeNumber.length < 4 || codeNumber.length >= 5) {
								            // 조건에 맞지 않을 경우, code_check 요소에 메시지를 표시
								            document.getElementById('code_check').innerText = '유효한 인증번호가 아닙니다.';
								        } else {
								            // 조건에 맞을 경우, code_check 요소를 비움
								            document.getElementById('code_check').innerText = '';
								            const code_btn = document.getElementById("code_btn2")
								            code_btn.style.backgroundColor = "red";
											code_btn.disabled = false
								        }
								    });
		
		    						// 인증번호 전송 버튼을 눌렀을 때의 동작을 정의해야 함. (작성하지 않음)
		    						
		    						document.getElementById('code_btn2').addEventListener('click', function() {
		    							// 인증번호 전송 버튼을 눌렀을 때 verificationCode div의 display 속성을 block으로 변경
		    						    /* const verificationCodeDiv = document.getElementById('verificationCode');
		    						    verificationCodeDiv.style.display = 'block'; */
		    							const code = document.getElementById('digit').value;
		    						   	/* alert(code); */
		    						   	/* console.log(typeof code); */
		    						    const phoneNumber = document.getElementById('tel').value;
		    						    const url = `/check/codeCheck?code=${code}&phoneNumber=${phoneNumber}`;
		    						    
		    						    fetch(url, {
		    						      method: 'GET',
		    						      headers: {
		    						        'Content-Type': 'application/json'
		    						      }/* ,
		    						      body: JSON.stringify(requestBody) */
		    						    })
		    						    .then(response => {
		    						      if (!response.ok) {
		    						        // 오류 처리 로직
		    						        throw new Error('서버 응답이 실패했습니다.');
		    						      }
		    						      return response.json();
		    						    })
		    						    .then(responseData => {
		    						    	if(responseData == true){
		    									/* alert('인증 번호가 다릅니다.') */
		    									Swal.fire('인증 번호가 다릅니다.')
		    									/* phoneAuth=true
		    									inputCheck() */
		    								} else {
		    									/* alert('인증되었습니다') */
		    									Swal.fire('인증되었습니다.')
		    									const input_tel = document.getElementById("tel")
		    									input_tel.disabled = true
		    									
		    									const tel_btn = document.getElementById("code_btn")
		    									tel_btn.disabled = true
		    									
		    									tel_btn.style.backgroundColor = "gray";
		    									
		    									const verificationCodeDiv = document.getElementById('verificationCode');
				    						    verificationCodeDiv.style.display = 'none';
				    						    
				    						  //휴대폰 인증 되면 전역변수로 설정한 phondConfirm 가 true로 변함 
				    						    phondConfirm = true;
				    						    
		    									/* const input_code = document.getElementById("input_code")
		    									input_code.readOnly = true */
		    									
		    									/* const code_btn = document.getElementById("code_btn")
		    									code_btn.disabled = true */
		    									
		    									/* phoneAuth=false */
		    									/* inputCheck()  */
		    								}
		    						    })
		    						    .catch(error => {
		    						      console.error('오류가 발생했습니다:', error);
		    						    });
		    						    
		    						})
		    						
		    						window.onpageshow = function(e) {
		    							const input_tel = document.getElementById("tel")
		    							tel_btn.style.backgroundColor = "red";
    									input_tel.disabled = false;
    									
    									const tel_btn = document.getElementById("code_btn")
    									document.getElementById('digit').innerText = ''; // -> 이거 안먹히는 듯
    									tel_btn.disabled = false
		    						}
		    						
		    						 // 모달부분 //   
		    				    function openModal() {
        document.getElementById('modalOverlay').style.display = 'block';
        document.getElementById('modalOverlay2').style.display = 'none';
        document.getElementById('modalOverlay3').style.display = 'none';
        document.getElementById('modalOverlay4').style.display = 'none';
    }

    
    function openModal2() {
        document.getElementById('modalOverlay').style.display = 'none';
        document.getElementById('modalOverlay2').style.display = 'block';
        document.getElementById('modalOverlay3').style.display = 'none';
        document.getElementById('modalOverlay4').style.display = 'none';
    }
    
    function openModal3() {
        document.getElementById('modalOverlay').style.display = 'none';
        document.getElementById('modalOverlay2').style.display = 'none';
        document.getElementById('modalOverlay3').style.display = 'block';
        document.getElementById('modalOverlay4').style.display = 'none';
    }
    
    function openModal4() {
        document.getElementById('modalOverlay').style.display = 'none';
        document.getElementById('modalOverlay2').style.display = 'none';
        document.getElementById('modalOverlay3').style.display = 'none';
        document.getElementById('modalOverlay4').style.display = 'block';
    }

    
    
    function closeModal() {
        document.getElementById('modalOverlay').style.display = 'none';
        document.getElementById('modalOverlay2').style.display = 'none';
        document.getElementById('modalOverlay3').style.display = 'none';
        document.getElementById('modalOverlay4').style.display = 'none';
    }