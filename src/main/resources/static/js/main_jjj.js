////헤더 스크롤 색상 변경
//$(function() {
//	// 스크롤 시 header fade-in
//	$(document).on('scroll', function() {
//		if ($(window).scrollTop() > 100) {
//			$("#header").removeClass("jj_navbar");
//			$("#header").addClass("activee");
//		} else {
//			$("#header").removeClass("activee");
//			$("#header").addClass("jj_navbar");
//		}
//	})
//
//});

$(function() {
    function updateHeaderClass() {
        if ($(window).scrollTop() > 100) {
            if ($(window).width() > 1000) {
                $("#header").removeClass("jj_navbar");
                $("#header").addClass("activee");
            }
        } else {
            $("#header").removeClass("activee");
            if ($(window).width() > 1000) {
                $("#header").addClass("jj_navbar");
            }
        }
    }

    // 최초 로드시 클래스 업데이트
    updateHeaderClass();

    // 스크롤 시 header fade-in 및 클래스 업데이트
    $(document).on('scroll', function() {
        updateHeaderClass();
    });

    // 창 크기 변경 시 클래스 업데이트
    $(window).on('resize', function() {
        updateHeaderClass();
    });
});





// 햄버거 영역
//  toggleBtn 변수에 .navbar_toogleBtn 클래스를 찾아서 변수에 연결
const toggleBtn = document.querySelector(".jj_navbar_toogleBtn");
const menu = document.querySelector(".jj_navbar_menu");

// 검색 영역 

//const jj_search = document.getElementById('search');
//const jj_icon = document.getElementById("jj_search_icon");
//const searchForm = document.getElementById("searchForm");
//let searchToggled = false;
//
//jj_icon.addEventListener('click', () => {
//	jj_search.classList.toggle('search_input');
//});



/*jj_icon.addEventListener('click', (event) => {
	event.preventDefault(); // 검색 아이콘의 기본 동작인 form 제출을 막음

	if (!searchToggled) {
		searchToggled = true;
		jj_search.classList.add('search_input'); // 검색창을 펼침
		jj_search.focus(); // 검색창에 포커스 설정
	} else {
		const searchKeyword = jj_search.value.trim();
		if (searchKeyword) {
			document.getElementById("kw").value = searchKeyword;
			searchForm.submit(); // 폼을 서버로 제출하여 검색 수행
		}
	}
});

// 검색창 클릭 이벤트 처리
jj_search.addEventListener('click', (event) => {
	event.stopPropagation(); // 검색창 클릭 시, 토글 기능이 동작하도록 이벤트 전파 막지 않음
});



// 문서 전체 클릭 이벤트 처리
document.addEventListener('click', () => {
	if (searchToggled) {
		searchToggled = false;
	}
});

// 검색어 입력 시 검색 버튼을 활성화하도록 처리
jj_search.addEventListener('input', () => {
	const searchKeyword = jj_search.value.trim();
	if (searchKeyword) {
		jj_icon.style.pointerEvents = 'auto'; // 검색어가 입력되었을 때 버튼 활성화
	} else {
		jj_icon.style.pointerEvents = 'none'; // 검색어가 없을 때 버튼 비활성화
	}
});

// 페이지 로드 시 검색창 상태를 설정
window.addEventListener('DOMContentLoaded', () => {
	const kwInput = document.getElementById("kw");
	const searchKeyword = kwInput.value.trim();
	if (searchKeyword) {
		jj_search.value = searchKeyword; // 검색어를 검색창에 설정
		jj_search.classList.add('search_input'); // 검색창을 펼쳐서 보이도록 함
		jj_search.focus(); // 검색창에 포커스 설정
		searchToggled = true; // 검색어가 있을 경우 검색창 토글 상태를 true로 설정
	}
});

*/


// 햄버거 실행 
toggleBtn.addEventListener("click", () => {

	menu.classList.toggle("active");

});

// 모바일 네비 더보기 드롭다운 부분 
const jj_drop = document.querySelector(".jj_drop");
const jj_list = document.querySelector(".jj_list");

// 더보기 클릭 이벤트 핸들러 추가
jj_drop.addEventListener("click", (event) => {
	event.preventDefault(); // 링크로 이동되는 동작을 막습니다.
	jj_list.classList.toggle("active");

	// 드롭다운 메뉴가 열렸을 때만 다른 곳을 클릭하면 닫히도록 이벤트 리스너를 추가합니다.
	if (jj_list.classList.contains("active")) {
		document.addEventListener("click", closeDropdown);
	} else {
		document.removeEventListener("click", closeDropdown);
	}
});

// 다른 곳을 클릭했을 때 드롭다운 메뉴가 닫히도록 하는 함수
function closeDropdown(event) {
	if (!event.target.closest(".jj_menubar")) {
		jj_list.classList.remove("active");
		document.removeEventListener("click", closeDropdown);
	}
}


// 슬라이드
document.addEventListener("DOMContentLoaded", function() {
	var slideIndex = 1;
	showSlides(slideIndex);

	// 각 버튼 클릭 시 이미지 전환
	document.querySelector('.jj_dot_btn_slide-1').addEventListener("click", function() {
		showSlides(1);
	});

	document.querySelector('.jj_dot_btn_slide-2').addEventListener("click", function() {
		showSlides(2);
	});

	document.querySelector('.jj_dot_btn_slide-3').addEventListener("click", function() {
		showSlides(3);
	});

	// 2초 마다 자동으로 다음 이미지로 전환하는 함수 
	var slideInterval = setInterval(function() {
		showSlides(slideIndex + 1);
	}, 3000);

	function showSlides(n) {
		var slides = document.getElementsByClassName("jj_slide-box");
		var dots = document.getElementsByClassName("jj_dot_btn");

		// 이미지 인덱스 유효성 확인
		if (n < 1) {
			slideIndex = slides.length;
		} else if (n > slides.length) {
			slideIndex = 1;
		} else {
			slideIndex = n;
		}

		// 모든 이미지 숨김
		for (var i = 0; i < slides.length; i++) {
			slides[i].style.display = "none";
		}

		// 모든 버튼 비활성화
		for (var i = 0; i < dots.length; i++) {
			dots[i].classList.remove("active");
		}

		// 현재 이미지 표시 및 해당 버튼 활성화
		slides[slideIndex - 1].style.display = "block";
		dots[slideIndex - 1].classList.add("active");
	}
});

// 추천 숙소
const jj_carousel = document.querySelector(".card-container"),
	firstImg = jj_carousel.querySelectorAll(".card")[0];
arrowIcons = document.querySelectorAll(".jj_wrapper i");
// 아이콘 숨김
arrowIcons.forEach(icon => {
  icon.style.display = "none";
});

// 마우스가 올라갈 때 아이콘 보이기
jj_carousel.addEventListener("mouseenter", () => {
  arrowIcons.forEach(icon => {
    icon.style.display = "block";
  });
});

// 마우스가 영역을 벗어날 때 아이콘 숨기기
jj_carousel.addEventListener("mouseleave", () => {
  arrowIcons.forEach(icon => {
    icon.style.display = "none";
  });
});


let isDragStrart = false, prevPageX, prevScrollLeft;
let firstImgWidth = firstImg.clientWidth + 270;

arrowIcons.forEach(icon => {
	icon.addEventListener("click", () => {
		jj_carousel.scrollLeft += icon.id == "left" ? -firstImgWidth : firstImgWidth;
	});
});

const dragStart = (e) => {
	isDragStrart = true;
	prevPageX = e.pageX;
	prevScrollLeft = jj_carousel.scrollLeft;
}

const dragging = (e) => {
	if (!isDragStrart) return;
	e.preventDefault();
	jj_carousel.classList.add("dragging");
	let positionDiff = e.pageX - prevPageX;
	jj_carousel.scrollLeft = prevScrollLeft - positionDiff;

}

const dragstop = () => {
	isDragStrart = false;
	jj_carousel.classList.remove("dragging");
}
jj_carousel.addEventListener("mousedown", dragStart);
jj_carousel.addEventListener("mousemove", dragging);
jj_carousel.addEventListener("mouseup", dragstop);

// 메인로고 클릭되면 세션에 저장되어있던 체크인, 아웃 날짜 삭제
function mainlogoOnClick(event) {
	/*event.preventDefault();*/
	
	sessionStorage.removeItem('checkin');
	sessionStorage.removeItem('checkOut');
}

