function warnning() {
		Swal.fire('취소 후에 가능합니다.')
	}
	
	function warnning2() {
		Swal.fire('이용완료 이후 가능합니다.')
	}
	
	 // 모든 id가 "cancelBtn"인 요소들을 선택하여 NodeList로 반환
    const cancelBtns = document.querySelectorAll("#cancelBtn");

// NodeList에 대해 반복문을 사용하여 각 요소에 이벤트 리스너 추가
cancelBtns.forEach(cancelBtn => {
    cancelBtn.addEventListener("click", () => {
        const buttonText = cancelBtn.textContent;

        // Check if the button text is "이용중", and if so, return without doing anything
        if (buttonText === "이용중") {
            return;
        }
        
       
			swal.fire({
				title: '예약 취소',
				text: '정말 예약을 취소하시겠습니까?',
				icon: 'question',
				reverseButtons: true,
				confirmButtonText: '확인',
				cancelButtonText: '취소',
				showCancelButton: true

			}).then((result) => {
				if (result.isConfirmed) {
					// Yes 버튼을 눌렀을 때의 동작
					  const amount = cancelBtn.parentElement.querySelector(".in_price").textContent;
        const tid = cancelBtn.parentElement.parentElement.querySelector("#tid").textContent;
        const cleanedAmount = amount.replace(/,/g, '');
        const cancelReason = '단순 변심';
        /* const payType = document.getElementById('payType').textContent; */
        /* const payTypeElements = document.querySelectorAll('#payType'); */
        
        const payType = cancelBtn.parentElement.querySelector("#payType").textContent;
        
        //console.log(amount);


        if(payType === "KAKAO") {
        	
	        fetch(`/refundd`, {
	            method: "POST",
	            headers: {
	                "Content-Type": "application/json"
	            },
	            body: JSON.stringify({
	                tid: tid,
	                cancel_amount: cleanedAmount,
	                cancel_tax_free_amount: "200",
	                cancel_vat_amount: "200"
	            })
	        })
	        .then(response => response.json())
	        .then(cres => {
	            location.replace('/user/mypage/my_booking');
	        })
	        .catch(error => {
	            console.error("Error:", error);
	        });
        } else if(payType === "TOSS") {
        	
        	const paymentKey = tid;
        	
        	/* alert(paymentKey);
        	alert(typeof paymentKey); */
        	
        	 fetch(`/book/toss/cancel`, {
 	            method: "POST",
 	            headers: {
 	                "Content-Type": "application/json"
 	            },
 	            body: JSON.stringify({
 	            	paymentKey: paymentKey,
 	            	cancelReason : cancelReason
 	            })
 	        })
 	        .then(response => response.json())
 	        .then(cres => {
 	            location.replace('/user/mypage/my_booking');
 	        })
 	        .catch(error => {
 	            console.error("Error:", error);
 	        });
        }else {
            // payType 값이 "KAKAO" 또는 "TOSS"가 아닌 경우
            console.error("Invalid payType:", payType);
        }
					
					/* window.history.back(); */
				} else if (result.dismiss === Swal.DismissReason.cancel) {
					// No 버튼을 눌렀을 때의 동작
					// 추가적인 작업을 수행하거나 아무 동작도 하지 않을 수 있음.
				}
			});


      
        
    });
});
    /* 리뷰 id 출력
    $(document).ready(function() {
        // Add click event listener to the "리뷰작성" button
        $('.reviewBtn').click(function() {
            // Get the value of accomID from the data-accomid attribute
            var accomID = $(this).attr('data-accomid');
            // Log the value to the console
            console.log(accomID);
            // You can now use the accomID variable for further processing or display
        });
    });
	
    */
    
    
    // 리뷰 새창
 $(document).ready(function() {
    function disableButton(button) {
        if (!button.disabled) {
            button.disabled = true;
            $(button).removeClass('reviewBtn'); // Remove 'reviewBtn' class
            $(button).addClass('gray-button'); // Add the 'gray-button' class to change appearance
            var roomID = $(button).attr('data-roomid');
            var checkIn = $(button).attr('data-in')
            localStorage.setItem('disabled_review_button_'+checkIn+'-'+roomID, 'true'); // Store the disabled state in local storage

            // Update the button text to "작성 완료"
            $(button).text('리뷰 작성 완료');
        }
    }

    // Function to check and update the button state
    function checkAndUpdateButtonState() {
        $('.reviewBtn').each(function() {
            var roomID = $(this).attr('data-roomid');
            var checkIn = $(this).attr('data-in');
            var isDisabled = localStorage.getItem('disabled_review_button_'+checkIn+'-'+roomID);
            if (isDisabled === 'true') {
                $(this).prop('disabled', true);
                $(this).addClass('gray-button'); // Add the 'gray-button' class to change appearance
                $(this).text('리뷰 작성 완료'); // Update the button text to "작성 완료"
            }
        });
    }

    // Check and update the button state when the page loads
    checkAndUpdateButtonState();
	  
  $('.reviewBtn').click(function() {
	  var accomID = $(this).attr('data-accomid');
	    console.log(accomID);

	    var screenWidth = $(window).width();
	    var screenHeight = $(window).height();

	    var newWindowWidth = 950;
	    var newWindowHeight = 800;

	    var leftPosition = (screenWidth - newWindowWidth) / 2-1000;
	    var topPosition = (screenHeight - newWindowHeight) / 2;

	    var newWindow = window.open('', '_blank', `width=${newWindowWidth},height=${newWindowHeight},left=${leftPosition},top=${topPosition}`);
	    newWindow.document.write(`
      <html>
      <head>
        <style>
         body {
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin-left: -130px;
            }
            #review_title {
                margin-top: 100px;
                margin-left: 200px;
                font-size:30px;
                font-weight: bold;
            }
            #room_start_k {
                margin-left: 230px;
            	font-size:20px;
            }
            #myform fieldset {
                display: inline-block;
                direction: rtl;
                border: 0;
            }
            #myform fieldset legend {
                text-align: right;
            }
            #myform input[type=radio] {
                display: none;
            }
            #myform label {
                font-size: 3em;
                color: transparent;
                text-shadow: 0 0 0 #f0f0f0;
            }
            #myform label:hover {
                text-shadow: 0 0 0 rgba(250, 208, 0, 0.99);
                cursor: pointer;
            }
            #myform label:hover ~ label {
                text-shadow: 0 0 0 rgba(250, 208, 0, 0.99);
                cursor: pointer;
            }
            #myform input[type=radio]:checked ~ label {
                text-shadow: 0 0 0 rgba(250, 208, 0, 0.99);
                cursor: pointer;
            }
            #reviewContents {
                width: 450px;
                height: 150px;
                padding: 10px;
                box-sizing: border-box;
                border: solid 1.5px #d3d3d3;
                border-radius: 5px;
                font-size: 16px;
                resize: none;
            	margin-left:180px;
            }
            #review_confirm {
                border: none;
                margin-top: 30px;
                margin-left: 320px;
                width: 200px;
                height: 50px;
                background-color: black;
                color: white;
            }
            #review_confirm:hover {
                cursor: pointer;
            }

        </style>
      </head>
      <body>
        <div class="review_mb-3" name="myform" id="myform">
          <br>
          <span id="review_title">별점과 이용경험을 남겨주세요.</span><br>
          <form onsubmit="return validateForm()" 
                action="/create/review/${accomID}" 
                method="post">
            <fieldset id="room_start_k">
              <input type="radio" name="reviewStar" value="5" id="rate1"><label for="rate1">★</label>
              <input type="radio" name="reviewStar" value="4" id="rate2"><label for="rate2">★</label>
              <input type="radio" name="reviewStar" value="3" id="rate3"><label for="rate3">★</label>
              <input type="radio" name="reviewStar" value="2" id="rate4"><label for="rate4">★</label>
              <input type="radio" name="reviewStar" value="1" id="rate5"><label for="rate5">★</label>
            </fieldset>
            <div>
              <textarea class="col-auto form-control" 
                        name="reviewContents" 
                        id="reviewContents" 
                        placeholder="좋은 리뷰를 남겨주시면 DAE BAK에 큰 힘이 됩니다!"></textarea>
            </div>
            <div id="error_message"></div>
            <input sec:authorize="isAuthenticated()" 
                   type="submit" 
                   value="리뷰작성" 
                   id="review_confirm">
          </form>
        </div>
      </body>
      </html>
    `);
    disableButton(this);
  });
});
    
    
    
    
    
    