$(document).ready(function() {
          $("#datepicker1").datepicker({
            multidate: 2,
            format: 'yyyy-mm-dd',
            startDate: '-0d',
            endDate: '+2m +15d',
            container: '#datepicker-container',
            showOn: "button",
            minDate: 0
          });
          
       // 페이지가 로드 되었을 때 세션에 checkin과 checkout이라는 키가 있는지 확인
          const checkin = sessionStorage.getItem("checkin");
          const checkOut = sessionStorage.getItem("checkOut");

          if (checkin && checkOut) {
            // 있다면 datepicker가 적용된 input의 기본값으로 출력
            $("#datepicker1").val(checkin + " ~ " + checkOut);
            
          } else {
            // 없다면 오늘날짜와 내일날짜를 구해 기본값으로 출력
            var today = new Date();
            var nextDay = new Date(today);
            nextDay.setDate(nextDay.getDate() + 1);
            var formattedToday = formatDate(today);
            var formattedNextDay = formatDate(nextDay);
            
            const checkin = formattedToday;
             
           const checkOut = formattedNextDay;
            
            sessionStorage.setItem("checkin", checkin);
           sessionStorage.setItem("checkOut", checkOut);
            
            $("#datepicker1").val(checkin + " ~ " + checkOut);
          }

          // 데이터 형식 지정 : 'YYYY-MM-DD'
          function formatDate(date) {
            var year = date.getFullYear();
            var month = (date.getMonth() + 1).toString().padStart(2, "0");
            var day = date.getDate().toString().padStart(2, "0");
            return year + "-" + month + "-" + day;
          }
          
          // 페이지가 로드 될 때 totalAmount들 모두 가져와서 세션에 저장하고
          // 예약하기 눌렀을 때 예약에 넘어가는 값들과 세션에 있는 값들 비교해서 유효한 값만 백에 넘기기
          // 예약하기 마지막에 세션 삭제
          
       // #totalAmount 아이디를 가진 요소의 텍스트 값을 추출하여 배열에 저장
          const totalAmountElements = document.querySelectorAll("#totalAmount");
          const reservePriceArray = [];

          totalAmountElements.forEach((element) => {
            const textValue = element.textContent.trim(); // 요소의 텍스트 값을 추출하고 앞뒤 공백 제거
            reservePriceArray.push(textValue);
          });

          // reservePriceArray를 세션에 저장하기 위해 배열을 스트링 타입으로 차례대로 저장
          let stringifiedArray = "";
          reservePriceArray.forEach((value, index) => {
            if (index > 0) {
              stringifiedArray += ","; // 각 값 사이에 쉼표(,) 추가
            }
            stringifiedArray += value;
          });

          // reservePriceArray를 세션에 저장
          sessionStorage.setItem("reservePriceArray", stringifiedArray);

          // 세션에 저장한 배열을 확인하기 위해 alert로 출력
          const storedReservePriceArray = sessionStorage.getItem("reservePriceArray").split(',');
          /* alert(storedReservePriceArray); */
          
        });
     
  
     /* 여러날짜를 입력받고 여러 날짜들중 가장 빠른날짜(체크인)와 가장 늦은 날짜를(체크아웃) 추출하는 함수*/
     function totalPrice() {
       // #datepicker1 요소에서 선택한 날짜 범위를 가져옴
       var selectedDates = $("#datepicker1").val();
       console.log("selectedDates:", selectedDates);
       // 선택한 날짜가 비어있는지(trim()으로 공백 제거 후 확인) 확인. 비어있다면 함수를 종료.
       if (selectedDates.trim() === "") {
           
           const checkin = sessionStorage.getItem("checkin");
           const checkOut = sessionStorage.getItem("checkOut");
           
           const accomID = document.getElementById('accomid').textContent;
           var url = `/tour/product/detail/${accomID}?checkin=${checkin}&checkOut=${checkOut}`;
           location.assign(url);
       
       } else {
          
       // 선택한 날짜를 쉼표로 분리하고(trim()으로 각 요소의 공백 제거) 배열로 만듬
       var datesArray = selectedDates.split(",").map(date => date.trim());
       // 유효한 날짜만 필터링하여 validDates 배열에 저장합니다. 유효한 날짜는 isDateValid 함수를 사용하여 확인
       var validDates = datesArray.filter(date => isDateValid(date));
       
       // 배열이 비어있지 않은 경우, 배열 내의 날짜 중 가장 작은 값을 minDate에 저장하고, 가장 큰 값을 maxDate에 저장
       if (validDates.length > 0) {
         var minDate = new Date(Math.min(...validDates.map(date => new Date(date))));
         var maxDate = new Date(Math.max(...validDates.map(date => new Date(date))));
         
         if (minDate.getTime() === maxDate.getTime()) {
             // If minDate and maxDate are equal, set maxDate to be one day after minDate
             maxDate.setDate(maxDate.getDate() + 1);
           }
         
         // 각 날짜들을 fomatting해줄 함수에 대입하고 결과 값 변수에 저장.
         var formattedMinDate = formatDate(minDate);
         console.log(formattedMinDate);
         var formattedMaxDate = formatDate(maxDate);
         console.log(formattedMaxDate);
         
         // fomatting된 날짜들 세션에 저장하기 위해 각 변수에 저장.
         const checkin = formattedMinDate;
        console.log("checkin:",checkin);
         const checkOut = formattedMaxDate;
         
         // 체크인, 체크아웃 날짜 세션에 담기
         sessionStorage.setItem("checkin", checkin);
         sessionStorage.setItem("checkOut", checkOut);
         
        // 화면에 보여질 두 날짜 형식 ex) 2023-07-17 ~ 2023-07-18
         $("#datepicker1").val(formattedMinDate + " ~ " + formattedMaxDate);
        // minDate과 maxDate의 일수 차이 계산
         const num = getDateDiff(minDate, maxDate);
         console.log(num);
         
          // 각 객실의 금액과 숙박 일수를 곱함
            setReservationSpanValue(num); 
 
         const accomID = document.getElementById('accomid').textContent;
 
            var url = `/tour/product/detail/${accomID}?checkin=${checkin}&checkOut=${checkOut}`;
             location.assign(url); 
       
       } 
         $("#datepicker1").val("");  // 입력값이 공백이라면 datepicker 초기화
       }
     }
     
     /* totalPrice() 끝*/
     
     /* totalPrice()에서 호출하는 함수들 */
     
     // 주어진 날짜가 'YYYY-MM-DD' 형식에 맞는지를 정규식을 사용하여 검사
     // isDateValid 함수는 주어진 날짜 문자열이 'YYYY-MM-DD' 형식에 맞으면 true를 반환하고, 그렇지 않으면 false를 반환
     function isDateValid(date) {
       var regex = /^\d{4}-\d{2}-\d{2}$/;
       return regex.test(date);
     }
     
     // 추출한 날짜 fomatting
     function formatDate(date) {
       var year = date.getFullYear();
       var month = (date.getMonth() + 1).toString().padStart(2, "0");
       var day = date.getDate().toString().padStart(2, "0");
       return year + "-" + month + "-" + day;
     }
     
     // 체크인과 체크아웃 사이의 일수 차이 계산
     function getDateDiff(date1, date2) {
       const oneDay = 24 * 60 * 60 * 1000; // 하루를 밀리초로 나타냄
       const diffDays = Math.round(Math.abs((date1 - date2) / oneDay));
       return diffDays;
     }
     
     // 다수의 날짜를 입력받았을 경우, 가장 빠른 날과 늦은 날 사이의 일수 차이를 구한뒤 금액에 곱하는 함수
     // 동일한 일수 x 각 객실의 금액(1박 기준)
     function setReservationSpanValue(num) {
       const reservePriceElements = document.querySelectorAll("#reservePrice");
       const totalAmountElements = document.querySelectorAll("#totalAmount");
       
       const reservePriceArray = []; // 값을 저장할 배열을 생성
     
       let index = 0;
       for (const totalAmountElement of totalAmountElements) {
         const totalAmount = parseInt(totalAmountElement.textContent);
         const reservePriceElement = reservePriceElements[index];
         const newValue = totalAmount * num;
         const totalPrice = addCommas(newValue);
         console.log('totalPrice',totalPrice);
         reservePriceElement.textContent = totalPrice.toString();
         
         reservePriceArray.push(totalPrice.toString()); // 값을 배열에 저장
         console.log(reservePriceArray);
         
         // 금액만 변질되지 않게 하기 위함이라 객실아이디는 필요가 없나? -> 어차피 같은 금액이라도 상관 없는 것 아닌가
         // 배열에 계속 저장하기 루프를 다 돌고 나서 배열을 fetch로 컨트롤러로 넘겨주기
         // -> 컨트롤러에서 해당 배열 받아서 쿼리문 날려서 -> 임시테이블에 저장
         // -> 성공의 결과로 해당 레코드의 pk 받아오거나 성공 여부만 숫자 0이나 1로 받음
         // 이후 fetch는 아무 것도 리다이렉션 하지 않고 끝?
         index++;
         if (index >= reservePriceElements.length) {
           break;
         }
       }
     // reservePriceArray를 fetch를 통해 컨트롤러로 전송.
       const url = `/price/savePrice`;
      // const data = { reservePrices: reservePriceArray }; // 전송할 데이터를 객체 형태로 만든다.

       fetch(url, {
         method: "POST", 
         headers: {
           "Content-Type": "application/json", // 전송하는 데이터 타입을 JSON으로 지정
         },
         /* body: JSON.stringify(data), */ // 데이터를 JSON 형태로 변환하여 전송
         body: JSON.stringify(reservePriceArray),
       })
       .then((response) => response.json())
       .then((data) => {
         // 컨트롤러로부터 받은 응답을 처리하는 로직
         console.log("Response from controller:", data);
       })
       .catch((error) => {
         // 오류 처리
         console.error("Error:", error);
       });
     }
     
     // 정규 표현식을 사용하여 세 자리 마다 콤마를 추가하는 함수
     function addCommas(num) {
       /*  alert(num); */
       return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
     }
           
     /* 컨트롤러로 객실정보 보내는 함수 */
     function handleClick(button) {
      const reservePriceElement = button.querySelector("#reservePrice");
      /* alert(reservePriceElement); */
      const totalPrice1 = reservePriceElement.textContent;
      /* alert('totalPrice1 : ', totalPrice1) */
      
      function findMatchingTotalPrice(totalPrice1) {
         // 세션에서 reservePriceArray 배열 불러오기
         const storedReservePriceArray = sessionStorage.getItem("reservePriceArray").split(',');

         // totalPrice1과 일치하는 값을 찾기 위한 변수 초기화
         let matchingTotalPrice = null;

         // storedReservePriceArray를 순회하면서 totalPrice1과 일치하는 값 찾기
         for (const value of storedReservePriceArray) {
           if (value === totalPrice1) {
             matchingTotalPrice = value;
             break; // 일치하는 값이 발견되면 순회 중단
           } else if(matchingTotalPrice === null){
               const reservePriceElements = document.querySelectorAll("#reservePrice");
               for(const rpe of reservePriceElements){
                  if (rpe.textContent === totalPrice1) {
                         matchingTotalPrice = rpe.textContent;
                         /* alert('matchingTotalPrice : ' + matchingTotalPrice); */
                         break; // 일치하는 값이 발견되면 순회 중단
                     }
               }
           }
         }

         // 일치하는 값을 반환
         return matchingTotalPrice;
       }
      
      let totalPrice2 = findMatchingTotalPrice(totalPrice1);
      
      /* alert(totalPrice2); */
      
      const totalPrice = addCommas(totalPrice2);
      /* alert(totalPrice); */
  
      console.log("totalPrice: ", totalPrice);
  
      // 세션에 저장한 checkin, checkOut 꺼냄
      const checkin = sessionStorage.getItem("checkin");
     /* alert(checkin); */
      const checkOut = sessionStorage.getItem("checkOut");
      /* alert(checkOut); */
  
      // value값 꺼내기
      const roomID = reservePriceElement.getAttribute("value");
     
      console.log(roomID);
      console.log(typeof roomID);
  
      const url = `/book/toss/${roomID}?totalPrice=${totalPrice}&checkin=${checkin}&checkOut=${checkOut}`;
  
      fetch(url, {
           method: 'GET',
           headers: {
             "Content-Type": "application/json",
           }
         })
           .then(response => {
             if (!response.ok) {
               throw new Error("Network response was not ok");
             }
             return response.json();
           })
           .then(data => {
                // 데이터에 null 값이 있는지 체크
                let hasNullValue = false;
                if (data !== null) {
                  const queryParams = new URLSearchParams();
                  // 원하는 필드를 queryParams에 추가.
                  if (data.bookerID) {
                    queryParams.append('bookerID', data.bookerID);
                  } else {
                    hasNullValue = true;
                  }
                  if (data.accomTitle) {
                    queryParams.append('accomTitle', data.accomTitle);
                  } else {
                    hasNullValue = true;
                  }
                  if (data.roomTitle) {
                    queryParams.append('roomTitle', data.roomTitle);
                  } else {
                    hasNullValue = true;
                  }
                  if (data.checkin) {
                    queryParams.append('checkin', data.checkin);
                  } else {
                    hasNullValue = true;
                  }
                  if (data.checkOut) {
                    queryParams.append('checkOut', data.checkOut);
                  } else {
                    hasNullValue = true;
                  }
                  if (data.bookHeadCount) {
                    queryParams.append('bookHeadCount', data.bookHeadCount);
                  } else {
                    hasNullValue = true;
                  }
                  if (data.totalPrice) {
                    queryParams.append('totalPrice', data.totalPrice);
                  } else {
                    hasNullValue = true;
                  }
                  if (data.tempRoomId) {
                    queryParams.append('tempRoomId', data.tempRoomId);
                  } else {
                    hasNullValue = true;
                  }
                  if (data.tempAccomId) {
                    queryParams.append('tempAccomId', data.tempAccomId);
                  } else {
                    hasNullValue = true;
                  }
                  // 추가로 필요한 필드를 queryParams에 추가

                  if (hasNullValue) {
                    // 데이터에 null 값이 있는 경우 '유효하지 않은 요청입니다.'라는 alert 창을 띄우고 이전 페이지로 이동
                    Swal.fire('경찰서 가고싶어?');
                    /* history.back(); */
                    window.location.reload();
                  } else {
                    const newURL = "/kdg/reserve?" + queryParams.toString();
                    window.location.href = newURL; // 뷰 페이지 URL로 이동합니다.
                  }
                } else {
                  // 데이터에 null 값이 있는 경우 '유효하지 않은 요청입니다.'라는 alert 창을 띄우고 이전 페이지로 이동
                  alert('유효하지 않은 요청입니다.');
                 /*  history.back(); */
                  window.location.reload();
                }
              })
              .catch(error => {
                console.error("Error occurred:", error);
                // 오류 처리를 위한 추가적인 코드를 작성
              });
      
      // 예약하기 버튼을 눌렀을 경우 세션에 저장된 날짜들 삭제
      sessionStorage.removeItem("checkin");
      sessionStorage.removeItem("checkOut");
      
  }   
