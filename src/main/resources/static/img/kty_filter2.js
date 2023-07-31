function getSevenDaysLater(date) {
	var currentDate = new Date(date);
	var sevenDaysLater = new Date(currentDate.setDate(currentDate.getDate() + 7));
	var year = sevenDaysLater.getFullYear();
	var month = ("0" + (sevenDaysLater.getMonth() + 1)).slice(-2);
	var day = ("0" + sevenDaysLater.getDate()).slice(-2);
	return year + "-" + month + "-" + day;
}

// 입력란 값 변경 시 기준 날짜를 설정하는 함수
/*
function setDefaultDate() {
	var input = document.getElementById("checkin_dropdown_daegu2");
	var selectedDate = input.value;
	var defaultDate = getSevenDaysLater(selectedDate);

	var checkoutInput = document.getElementById("checkout_dropdown_daegu3");
	checkoutInput.value = defaultDate;
}

// Add event listener to call setDefaultDate() when the check-in date changes
document.getElementById("checkin_dropdown_daegu2").addEventListener("change", setDefaultDate);
*/
// 가격범위
$(function() {
	$("#price-range").slider({
		range: true,
		min: 0,
		max: 100000,
		values: [0, 100000],
		step: 1000,
		slide: function(event, ui) {
			$("#min-price").text(ui.values[0]);
			$("#max-price").text(ui.values[1]);
		}
	});
});

//방찾기 상세 조회 기능 구현
const btnSearch2 = document.getElementById("btnbtn");
btnSearch2.addEventListener('click', function() {
	
	//지역 값 추출
    const regionDropdownDaegu = document.getElementById("region_dropdown_daegu");
    const selectedRegion = regionDropdownDaegu.value;

	//체크인 시간 추출
    const checkinDate = document.getElementById("checkin_dropdown_daegu2").value;
    const checkoutDate = document.getElementById("checkout_dropdown_daegu3").value;
	
	//체크아웃 시간 추출
    const checkboxes = document.querySelectorAll('input[name="RoomType"]:checked');
    const selectedRoomTypes = Array.from(checkboxes).map(checkbox => checkbox.value);
    
    
   
	//투숙 인원 추출
    const peopleDropdownDaegu = document.getElementById("people_dropdown");
    const selectedpeople = peopleDropdownDaegu.value;
	
	//평점 추출
    const checkboxes2 = document.querySelectorAll('input[name="scoreType"]:checked');
    const selectedStartTypes = Array.from(checkboxes2).map(checkbox => checkbox.value);

	// 최소금액 추출
    const minPriceInput = document.getElementById("kw8Option7");
    const minPrice = minPriceInput.value;
	
	// 최대금액 추출
    const maxPriceInput = document.getElementById("kw9Option8");
    const maxPrice = maxPriceInput.value;

    let url = "/tour/daegu_room";
    
    //지역만
    if(selectedRegion && !selectedRoomTypes.length && !selectedStartTypes.length && !selectedpeople && !minPrice && !maxPrice){
		url = "/tour/daegu_room2";
	
		//지역+평점
	} else if(selectedRegion && selectedStartTypes.length>0 && !minPrice && !maxPrice && !selectedpeople){
			url="/tour/daegu_room10";
			//지역+평점+투숙인원
			if(selectedpeople && !minPrice && !maxPrice){
				url="/tour/daegu_room11"
			}
			//지역+투숙
	} else if(selectedRegion && selectedpeople && !minPrice && !maxPrice){
		url ="/tour/daegu_room12";
	}
    
    //숙소 타입만
	if (selectedRoomTypes.length > 0 && !selectedRegion && !selectedStartTypes.length && !selectedpeople) {
    	url = "/tour/daegu_room3";
		
		
	} 
	
	// 평점만
	if (selectedStartTypes.length > 0 && !selectedRegion && !selectedRoomTypes.length && !selectedpeople) {
    	url = "/tour/daegu_room4";
	}
	
	
	//투숙인원만
	if (selectedpeople && !selectedRegion && !selectedRoomTypes.length && !selectedStartTypes.length && !minPrice && !maxPrice) {
    	url = "/tour/daegu_room5";
	}
	
	
	//지역+투숙인원+숙소타입+평점
	if(selectedRegion && selectedpeople && selectedRoomTypes.length >0 && selectedStartTypes.length>0 && !minPrice && !maxPrice){
		url = "/tour/daegu_room6";
	}
	
	//지역+숙소타입
	if(selectedRegion && selectedRoomTypes.length>0){
		if(!selectedStartTypes.length>0 && !selectedpeople){
			url ="/tour/daegu_room7";
		}
		// 지역+숙소+평점
		if(selectedStartTypes.length>0 && !selectedpeople){
			url ="/tour/daegu_room8";
		}
		
		 
		//지역+숙소+투숙인원
		if(selectedpeople && !selectedStartTypes.length>0 && !minPrice && !maxPrice ){
			url="/tour/daegu_room9";
		}
	}
	// 숙소타입 + 평점
	if(selectedRoomTypes.length>0 && selectedStartTypes.length>0){
		if(!selectedRegion && !selectedpeople && !maxPrice){
		url="/tour/daegu_room13";
		}
		if(selectedpeople && !selectedRegion && !minPrice && !maxPrice){
				url="/tour/daegu_room14";
		}
	} 
	
	if(!selectedRegion && !selectedRoomTypes.length>0){
		if(selectedStartTypes.length>0 && selectedpeople && !minPrice && !maxPrice){
			url="/tour/daegu_room15";
		}
		
	}
	
	
	
	//최소금액만 있을 때
	
	if(minPrice && !maxPrice){
		if(!selectedRegion && !selectedRoomTypes.length>0 && !selectedStartTypes.length >0 && !selectedpeople){
		url="/tour/daegu_room16";
		}else if(selectedRegion && !selectedRoomTypes.length>0 && !selectedStartTypes.length>0 && !selectedpeople){
			url="/tour/daegu_room19";
		} else if(selectedRoomTypes.length>0 && !selectedRegion && !selectedStartTypes.length>0 && !selectedpeople){
			url="/tour/daegu_room22";
		} else if(selectedRegion && selectedRoomTypes.length>0 && !selectedStartTypes.length>0 && !selectedpeople){
			url="/tour/daegu_room25";
		} else if(selectedStartTypes.length>0 && !selectedRegion && !selectedRoomTypes.length>0 && !minPrice && !maxPrice){
			url="/tour/daegu_room28";
		}  else if (selectedRegion && selectedRoomTypes.length>0 && selectedStartTypes.length>0 && minPrice){
			url="/tour/daegu_room31";
		} else if(selectedRegion && selectedStartTypes.length>0 && minPrice && !selectedpeople){
			url="/tour/daegu_room34";
		} else if(selectedRoomTypes.length>0 && selectedStartTypes.length>0 && !selectedRegion && !selectedpeople && !selectedpeople){
			url="/tour/daegu_room37";
		} else if(selectedpeople && !selectedRegion && !selectedRoomTypes.length>0 && !selectedStartTypes.length>0){
			url="/tour/daegu_room40";
		} else if(selectedRegion && selectedpeople && !selectedRoomTypes.length>0 && !selectedStartTypes.length>0){
			url="/tour/daegu_room43";
		} else if(selectedRoomTypes.length>0 && selectedpeople && !selectedStartTypes.length>0 && !selectedRegion){
			url="/tour/daegu_room46";
		} else if(selectedStartTypes.length>0 && selectedpeople && !selectedRoomTypes.length>0 && !selectedRegion){
				url="/tour/daegu_room49";
		}  else if(selectedRoomTypes.length>0 && selectedStartTypes.length>0 && selectedpeople){
			url="/tour/daegu_room52";
		} else if(selectedRoomTypes.length>0 && selectedRegion && selectedpeople){
			url="/tour/daegu_room55";
		} else if(selectedRegion && selectedStartTypes.length>0 && selectedpeople){
			url="/tour/daegu_room58";
		}
	}
	
	// 최대금액만 있을 때
	if(maxPrice && !minPrice){
		if(!selectedRegion && !selectedRoomTypes.length>0 && !selectedStartTypes.length >0 && !selectedpeople){
		url="/tour/daegu_room17";
	}else if(selectedRegion && !selectedRoomTypes.length>0 && !selectedStartTypes.length>0 && !selectedpeople){
			url="/tour/daegu_room20";
		} else if(selectedRoomTypes.length>0 && !selectedRegion && !selectedStartTypes.length>0 && !selectedpeople){
			url="/tour/daegu_room23";
		} else if(selectedRegion && selectedRoomTypes.length>0 && !selectedStartTypes.length>0 && !selectedpeople){
			url="/tour/daegu_room26";
		} else if(selectedStartTypes.length>0 && !selectedRegion && !selectedRoomTypes.length>0 &&!selectedpeople){
			url="/tour/daegu_room29";
		} else if (selectedRegion && selectedRoomTypes.length>0 && selectedStartTypes.length>0 && maxPrice){
			url="/tour/daegu_room32";
		} else if(selectedRegion && selectedStartTypes.length>0 && !selectedpeople){
				url="/tour/daegu_room35";
		} else if(selectedRoomTypes.length>0 && selectedStartTypes.length>0 && !selectedpeople){
			url="/tour/daegu_room38";
		} else if(selectedpeople && !selectedRegion && !selectedRoomTypes.length>0 && !selectedStartTypes.length>0){
			url="/tour/daegu_room41";
		} else if(selectedRegion && selectedpeople && !selectedRoomTypes.length>0 && !selectedStartTypes.length>0){
			url="/tour/daegu_room44";
		} else if(selectedRoomTypes.length>0 && selectedpeople && !selectedStartTypes.length>0 && !selectedRegion){
			url="/tour/daegu_room47";
		} else if(selectedStartTypes.length>0 && selectedpeople && !selectedRoomTypes.length>0 && !selectedRegion){
			url="/tour/daegu_room50";
		} else if(selectedRoomTypes.length>0 && selectedStartTypes.length>0 && selectedpeople){
			url="/tour/daegu_room53";
		} else if(selectedRegion && selectedRoomTypes.length>0 && selectedpeople){
			url="/tour/daegu_room56";
		} else if(selectedRegion && selectedStartTypes.length>0 && selectedpeople){
			url="/tour/daegu_room59";
		}
}
	
	// 둘다 있을 때 
	if(maxPrice && minPrice){
		if(!selectedRegion && !selectedRoomTypes.length>0 && !selectedStartTypes.length >0 && !selectedpeople){
		url="/tour/daegu_room18";
		}else if(selectedRegion && !selectedRoomTypes.length>0 && !selectedStartTypes.length>0 && !selectedpeople){
				url="/tour/daegu_room21";
		} else if(selectedRoomTypes.length>0 && !selectedRegion && !selectedStartTypes.length>0 && !selectedpeople){
			url="/tour/daegu_room24";
		} else if(selectedRegion && selectedRoomTypes.length>0 &&!selectedStartTypes.length>0 && !selectedpeople){
			url="/tour/daegu_room27";
		} else if(selectedStartTypes.length>0&& !selectedRegion && !selectedRoomTypes.length>0 && !selectedpeople){
				url="/tour/daegu_room30";
		} else if(selectedRegion && selectedRoomTypes.length>0 && selectedStartTypes.length>0 && !selectedpeople){
			url="/tour/daegu_room33";
		} else if(selectedRegion && selectedStartTypes.length>0 && minPrice && maxPrice && !selectedpeople){
			url="/tour/daegu_room36";
		} else if(selectedRoomTypes.length>0 && selectedStartTypes.length>0 && !selectedpeople){
				url="/tour/daegu_room39";
		} else if(selectedpeople && !selectedRegion && !selectedRoomTypes.length>0 && !selectedStartTypes.length>0){
			url="/tour/daegu_room42";
		} else if(selectedRegion && selectedpeople && !selectedRoomTypes.length>0 && !selectedStartTypes.length>0){
			url="/tour/daegu_room45";
		} else if(selectedRoomTypes.length>0 && selectedpeople && !selectedStartTypes.length>0 && !selectedRegion){
			url="/tour/daegu_room48";
		} else if(selectedStartTypes.length>0 && selectedpeople && !selectedRoomTypes.length>0 && !selectedRegion){
			url="/tour/daegu_room51";
		} else if(selectedRoomTypes.length>0 && selectedStartTypes.length>0 && selectedpeople && !selectedRegion){
			url="/tour/daegu_room54";
		} else if (selectedRegion && selectedRoomTypes.length>0 && selectedpeople && !selectedStartTypes.length>0){
			url="/tour/daegu_room57";
		} else if(selectedRegion && selectedStartTypes.length>0 && selectedpeople && !selectedRoomTypes.length>0){
			url="/tour/daegu_room60";
		} else if(selectedRegion && selectedRoomTypes.length>0 && selectedpeople  && selectedStartTypes.length>0){
			url="/tour/daegu_room61";
		}
		
	}


    const params = [];
    if (selectedRegion) params.push(`kw2=${encodeURIComponent(selectedRegion)}`);
    if (checkinDate) params.push(`kw3=${encodeURIComponent(checkinDate)}`);
    if (checkoutDate) params.push(`kw4=${encodeURIComponent(checkoutDate)}`);
    if (selectedRoomTypes.length > 0) params.push(`kw5=${encodeURIComponent(selectedRoomTypes)}`);
    if (selectedStartTypes.length > 0) params.push(`kw6=${encodeURIComponent(selectedStartTypes)}`);
    if (selectedpeople) params.push(`kw7=${encodeURIComponent(selectedpeople)}`);
    if (minPrice && !maxPrice) params.push(`kw8=${encodeURIComponent(minPrice)}`);
    if (maxPrice && !minPrice) params.push(`kw9=${encodeURIComponent(maxPrice)}`);
    if (maxPrice && minPrice) {
        params.push(`kw8=${encodeURIComponent(minPrice)}`);
        params.push(`kw9=${encodeURIComponent(maxPrice)}`);
    }

    if (params.length > 0) {
        url += `?${params.join("&")}`;
    }

    window.location.href = url;
});

 var productAmountElements = document.getElementsByClassName("productAmount");

// 각 요소에 대해 반복문을 실행하여 포맷팅을 적용합니다.
for (var i = 0; i < productAmountElements.length; i++) {
  var productAmountElement = productAmountElements[i];
  var productAmountText = productAmountElement.innerText;

  // 천 단위마다 쉼표(,)를 추가하여 포맷팅
  var formattedAmount = Number(productAmountText).toLocaleString();

  // 포맷팅된 값을 요소에 다시 설정
  productAmountElement.innerText = formattedAmount;
}


var producusetimeElements = document.getElementsByClassName("roomtime");

for (var i = 0; i < producusetimeElements.length; i++) {
  var producusetimeElement = producusetimeElements[i];
  var producusetimeText = producusetimeElement.innerText;

  var formattedTime = formatTime(producusetimeText);
  producusetimeElement.innerText = formattedTime;
}

function formatTime(time) {
  var formattedTime = time;

  // 시간 변환
  var hour = parseInt(time, 10);
  if (hour < 12) {
    formattedTime += ' AM';
  } else {
    formattedTime += ' PM';
  }

  return formattedTime;
}

/*

const page_elements = document.getElementsByClassName("page-link");
Array.from(page_elements).forEach(function(element) {
    element.addEventListener('click', function() {
        document.getElementById('page').value = this.dataset.page;
        document.getElementById('searchForm').submit();
    });
});

const page_elements2 = document.getElementsByClassName("page-link2");
Array.from(page_elements).forEach(function(element) {
    element.addEventListener('click', function() {
        document.getElementById('page').value = this.dataset.page;
        document.getElementById('searchForm2').submit();
    });
});
*/
const btn_search = document.getElementById("btn_search_home");
btn_search.addEventListener('click', function() {
    document.getElementById('kw').value = document.getElementById('search_kw').value;
    document.getElementById('page').value = 0;  // 검색버튼을 클릭할 경우 0페이지부터 조회한다.
    document.getElementById('searchForm').submit();
});



