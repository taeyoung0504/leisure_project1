 // "room"이라는 클래스명을 가진 모든 요소들
					    var roomElements = document.getElementsByClassName("room");
					
					    // 루프 돌며 모든 "room"요소들에 swiper 적용
					    for (var i = 0; i < roomElements.length; i++) {
					        var roomElement = roomElements[i];
					        var swiper = new Swiper(roomElement.querySelector(".mySwiper"), {
					            loop: true,
					            spaceBetween: 10,
					            slidesPerView: 4,
					            freeMode: true,
					            watchSlidesProgress: true,
					        });
					        var swiper2 = new Swiper(roomElement.querySelector(".mySwiper2"), {
					            loop: true,
					            spaceBetween: 10,
					            navigation: {
					                nextEl: roomElement.querySelector(".swiper-button-next"),
					                prevEl: roomElement.querySelector(".swiper-button-prev"),
					            },
					            thumbs: {
					                swiper: swiper,
					            },
					        });
					    }