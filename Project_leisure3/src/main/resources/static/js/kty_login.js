$(function() {
	$(document).ready(function() {
		$("#kakao_login_btn").on("click", function() {
			location.href = "/oauth2/authorization/kakao";
		});

		$("#google_login_btn").on("click", function() {
			location.href = "/oauth2/authorization/google";
		});

		$("#naver_login_btn").on("click", function() {
			location.href = "/oauth2/authorization/naver";
		});

		$(".login_title").on("click", function() {
			// 타임리프 URL을 정확히 지정해야 합니다.
			var url = "/";

			// 페이지 이동
			location.href = url;
		});

	});
});