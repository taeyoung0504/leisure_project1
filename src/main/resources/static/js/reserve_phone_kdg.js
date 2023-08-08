// 휴대폰 번호 입력란에 keyup 이벤트 리스너를 등록
							    	document.getElementById('tel').addEventListener('keyup', function() {
							        // 입력된 휴대폰 번호를 가져옴
							        var phoneNumber = this.value;
									
							        // 입력된 휴대폰 번호의 앞 3글자가 '010'이 아니거나, 숫자가 아니거나, 공백이 있는지 체크
							        if (!/^010/.test(phoneNumber) || isNaN(phoneNumber) || phoneNumber.indexOf(' ') !== -1
							        		|| phoneNumber.length < 10 || phoneNumber.length >= 12) {
							            // 조건에 맞지 않을 경우, tel_check 요소에 메시지를 표시
							            document.getElementById('tel_check').innerText = '휴대폰 번호를 확인해주세요.';
							        } else {
							            // 조건에 맞을 경우, tel_check 요소를 비움
							            document.getElementById('tel_check').innerText = '';
							            const code_btn = document.getElementById("code_btn")
							            code_btn.style.backgroundColor = "red";
										code_btn.disabled = false
							        }
							    });
	
	    						// 인증번호 전송 버튼을 눌렀을 때의 동작을 정의해야 함. (작성하지 않음)
	    						
	    						document.getElementById('code_btn').addEventListener('click', function() {
	    							// 인증번호 전송 버튼을 눌렀을 때 verificationCode div의 display 속성을 block으로 변경
	    						    /* const verificationCodeDiv = document.getElementById('verificationCode');
	    						    verificationCodeDiv.style.display = 'block'; */
	    						    
	    						    
	    						    
	    						    const phoneNumber = document.getElementById('tel').value;
	    						   	/* alert(phoneNumber);
	    						   	console.log(typeof phoneNumber); */
	    						   	
	    						   	const url = `/check/sendSMS?phoneNumber=${phoneNumber}`;
	    						    
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
	    						        // 에러메시지가 올 경우 swal로 창을 띄워주고 1분후 다시 시도하라는 메시지를 준다.
	    						        throw new Error('서버 응답이 실패했습니다.');
	    						      }
	    						      return response.json();
	    						    })
	    						    .then(responseData => {
	    						     	if(responseData.count > 3) {
	    						     		Swal.fire('나중에 다시 시도해 주세요.')
	    						     	} else {
	    						     		
	    						    	// 현재 인증번호가 성공적으로 발송된 후가 아니라 전송 버튼이 기준임
	    						    	const verificationCodeDiv = document.getElementById('verificationCode');
		    						    verificationCodeDiv.style.display = 'block'; 
		    						    const tel_btn = document.getElementById("code_btn")
    									tel_btn.disabled = true
    									tel_btn.style.backgroundColor = "gray";
		    						    const input_tel = document.getElementById("tel")
    									/* input_tel.readOnly = true */
    									input_tel.disabled = true
	    						    	
		    						    /* alert('인증번호 발송 완료!'); */
		    						    
    									var timer;
    								    var isRunning = false;
    								    
    								    
    								    sendAuthNum();
    								    
    								    // 인증번호 발송 및 타이머 함수 실행
    								    function sendAuthNum(){
    								        // 남은 시간(초)
    								        var leftSec = 10,
    								        display = document.querySelector('#timer');
    								        // 이미 타이머가 작동중이면 중지
    								        if (isRunning){
    								            clearInterval(timer);
    								        }
    								        startTimer(leftSec, display);
    								    }

    								    function startTimer(count, display) {
    								        var minutes, seconds;
    								        timer = setInterval(function () {
    								            minutes = parseInt(count / 60, 10);
    								            seconds = parseInt(count % 60, 10);
    								            minutes = minutes < 10 ? "0" + minutes : minutes;
    								            seconds = seconds < 10 ? "0" + seconds : seconds;
    								            display.textContent = minutes + ":" + seconds;
    								            // 타이머 종료
    								            if (--count < 0) {
    								                clearInterval(timer);
    								                display.textContent = "";
    								                isRunning = false;
    								           
    								                
    								                fetch(`/check/timeout`, {
    					    						      method: 'GET',
    					    						      headers: {
    					    						        'Content-Type': 'application/json'
    					    						      }/* ,
    					    						      body: JSON.stringify(requestBody) */
    					    						    })
    					    						    .then(response => response.json())
    					    						    .then(responseData => {
    					    						    	if(responseData == 1){
    					    									/* Swal.fire('그런 세션은 없습니다.') */
    					    									return
    					    								} else {
    					    									Swal.fire('인증번호를 다시 요청해주세요.')
    					    									/* const verificationCodeDiv = document.getElementById('verificationCode'); */
			    								                /* input_tel.readOnly = false */
    					    									tel_btn.disabled = false
			    								                input_tel.disabled = false
			    		    									tel_btn.innerText = '재전송';
			    								                tel_btn.style.backgroundColor = "red";
    					    									/* verificationCodeDiv.textContent = '';  */
    					    									return
    					    								}
    					    						    })
    					    						    .catch(error => {
    					    						      console.error('오류가 발생했습니다:', error);
    					    						    });
    								                
    								            }
    								        }, 1000);
    								        isRunning = true;
    								    }
	    						    	
											Swal.fire('인증번호 발송 완료')
	    						     	}
									
	    						    	
	    						    })
	    						    .catch(error => {
	    						    	// 에러메시지가 올 경우 swal로 창을 띄워주고 1분후 다시 시도하라는 메시지를 준다.
	    						      console.error('오류가 발생했습니다:', error);
	    						    });
	    						  });