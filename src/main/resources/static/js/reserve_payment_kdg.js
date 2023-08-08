/*const paybtn = document.getElementById('payment-btn');
							    	
							    	//휴대폰 인증 검사
							    	var phondConfirm = false;
							    	paybtn.addEventListener('click', function() {
							    		const roomIDElement = document.getElementById('roomID');
							    		const roomID = roomIDElement.textContent;
							    		
							    		const orderName = [[ ${bookingvo.roomTitle} ]] null;
							    		
							    		//const amount = [[ ${bookingvo.totalPrice} ]] null;
							    		
							    		const amountStr = [[ ${bookingvo.totalPrice} ]] null;
							    		const amount = parseInt(amountStr.replace(/,/g, ''), 10);
							    		
							    		const customerName = document.getElementById('username').value;
							    		
							    		const payType = document.getElementById('payment-select').value;
							    		
							    		const bookerName = document.getElementById('username').value;
							    		
							    		//이름 특수문자, 송백, 숫자 확인
							    	    const specialCharRegex = /[0-9 `!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;

							    		    if (bookerName.trim() === '' || specialCharRegex.test(bookerName)) {
							    		    	Swal.fire("이름에 공백, 숫자, 특수 문자를 사용할 수 없습니다");
							    		        return;
							    		    }
							    		
							    		const bookerTel = document.getElementById('tel').value;
							    		
							    		//휴대폰 인증이 되지 않아 false 인 경우 
									    if (phondConfirm == false) {
									    	Swal.fire("휴대폰 인증이 필요합니다");
									        return;
									    }
							    		
							    		
							    		
									  //추가 결제시 체크박스 여부 확인
							    		const checkboxes = document.querySelectorAll('.inp_chk_02');
									    const checkedCnt = document.querySelectorAll('.inp_chk_02:checked').length;
									    const totalCnt = checkboxes.length;
									
																		    
									    if (totalCnt !== checkedCnt) {
											Swal.fire('전체동의가 필요합니다')
											return;
										}
							    		
							    		const url = `/book/create/${roomID}`;
							    		
								    		function kakao(url, payType, bookerName, bookerTel, orderName) {
									    		fetch(url, {
									    			method: 'POST',
									    			headers: {
									    				"Content-Type": "application/json",
									    			},
									    			body: JSON.stringify({
									    				payType: payType,
									    				bookerName: bookerName,
									    				bookerTel: bookerTel
									    			})	
									    		})
									    		.then(response => {
									    		    if (response.status === 200) {
									    		        return response.json();
									    		    } else {
									    		        throw new Error("Booking creation failed.");
									    		    }
									    		})
									    		.then(data => {
									    		    // data에서 "next_redirect_pc_url" 이름의 URL 추출
									    		    const nextRedirectPcUrl = data.next_redirect_pc_url;
		
									    		    // 추출한 URL로 사용자를 이동시키기
									    		    window.location.href = nextRedirectPcUrl;
									    		})
									    		.catch(error => {
									    		    console.error("Error:", error.message);
									    		});
								    			
								    		}
								    		
								    		 토스 
								    		
								    		const url2 = `/book/api/v1/payments/toss/${roomID}`;
								    		 const url2 = `/api/v1/payments/toss`; 
							    			
								    	 	 let successUrl = 'http://localhost:8080/api/v1/payments/toss/success';
							    			let failUrl = 'http://localhost:8080/api/v1/payments/toss/fail';  
							    			let successUrl = 'http://192.168.10.67:8080/api/v1/payments/toss/success';
							    			let failUrl = 'http://192.168.10.67:8080/api/v1/payments/toss/fail';     
								    		
							    		       function toss(url2, payType, bookerName, bookerTel, amount, orderName, successUrl, failUrl) {
									                fetch(url2, {
									                    method: 'POST',
									                    headers: {
									                        "Content-Type": "application/json",
									                    },
									                    body: JSON.stringify({
									                        payType: payType,
									                        amount : amount,
									                        orderName : orderName,
									                        customerName: bookerName,
									                        customerTel: bookerTel,
									                        yourSuccessUrl: successUrl,
									                        yourFailUrl: failUrl,
									                    }),
									                })
									                .then(response => {
									                    if (!response.ok) {
									                        throw new Error("Booking creation failed.");
									                    }
									                    return response.json();
									                })
									                .then(data => {
									                    // 응답 객체에서 추출한 데이터로 대입
									                    const {
									                        amount,
									                        orderId,
									                        orderName,
									                        customerName,
									                        customerEmail,
									                        successUrl,
									                        failUrl
									                    } = data;

									                    // ------ 클라이언트 키로 객체 초기화 ------
									                    var clientKey = 'test_ck_7DLJOpm5QrlRWgypka53PNdxbWnY';
									                    var tossPayments = TossPayments(clientKey);

									                    // ------ 결제창 띄우기 ------
									                    tossPayments.requestPayment('CARD', {
									                        amount: amount,
									                        orderId: orderId,
									                        orderName: orderName,
									                        customerName: customerName,
									                        customerEmail: customerEmail,
									                        successUrl: successUrl,
									                        failUrl: failUrl,
									                    });
									                })
									                .catch(error => {
									                    console.error("Error:", error.message);
									                });
									            
									            }
								    		
							    		 if(payType === 'KAKAO') {
							    			 
							    			kakao(url, payType, bookerName, bookerTel)
							    			
							    		}  else if(payType === 'TOSS') {
							    			
								            toss(url2, payType, bookerName, bookerTel, amount, orderName, successUrl, failUrl);
								            
							    		}
							    		 
							    	});
							    	
    */