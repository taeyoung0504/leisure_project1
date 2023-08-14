  const cancelBtns = document.querySelectorAll("#cancelBtn");

// NodeList에 대해 반복문을 사용하여 각 요소에 이벤트 리스너 추가
cancelBtns.forEach(cancelBtn => {
    cancelBtn.addEventListener("click", () => {
        const buttonText = cancelBtn.textContent;


        if (buttonText === "이용중") {
            return;
        }

        const amount = cancelBtn.parentElement.querySelector(".in_price").textContent;
        const tid = cancelBtn.parentElement.parentElement.querySelector("#tid").textContent;
        const cleanedAmount = amount.replace(/,/g, '');
        //console.log(amount);
        alert(cleanedAmount);

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
    });
});
   
    
    // 리뷰 새창
  $(document).ready(function() {
 
	  function disableButton(button) {
		    if (!button.disabled) {
		      button.disabled = true;
		      $(button).removeClass('reviewBtn'); 
		      $(button).addClass('gray-button'); 
		      var accomID = $(button).attr('data-accomid');
		      localStorage.setItem('disabled_review_button_' + accomID, 'true'); 

		      // Update the button text to "작성 완료"
		      $(button).text('작성 완료');
		    }
		  }

		  // Function to check and update the button state
		  function checkAndUpdateButtonState() {
		    $('.reviewBtn').each(function() {
		      var accomID = $(this).attr('data-accomid');
		      var isDisabled = localStorage.getItem('disabled_review_button_' + accomID);
		      if (isDisabled === 'true') {
		        $(this).prop('disabled', true);
		        $(this).addClass('gray-button');
		        $(this).text('작성 완료'); 
		      }
		    });
		  }

		  
		  checkAndUpdateButtonState();
	  
  $('.reviewBtn').click(function() {
    var accomID = $(this).attr('data-accomid');
    console.log(accomID);


    var newWindow = window.open('', '_blank', 'width=500,height=400');
    newWindow.document.write(`
      <html>
      <head>
        <style>
         body {
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
            }
            #review_title {
                margin-top: -120px;
                margin-left: 60px;
                font-size: 20px;
                font-weight: bold;
            }
            #room_start_k {
                margin-left: 50px;
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
                width: 400px;
                height: 150px;
                padding: 10px;
                box-sizing: border-box;
                border: solid 1.5px #d3d3d3;
                border-radius: 5px;
                font-size: 16px;
                resize: none;
            }
            #review_confirm {
                border: none;
                margin-top: 10px;
                margin-left: 330px;
                width: 70px;
                height: 40px;
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
                        placeholder="좋은 수강평을 남겨주시면 요기어때에 큰 힘이 됩니다!"></textarea>
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