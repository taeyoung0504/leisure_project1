$(document).ready(function() {
	var reviewList = $("#review_list");

	// Sort the list of reviews based on the review creation time
	var sortedReviews = reviewList.children("li").sort(function(a, b) {
		var timeA = new Date($(a).find(".time_real").text());
		var timeB = new Date($(b).find(".time_real").text());
		return timeB - timeA;
	});

	// Clear the existing list of reviews
	reviewList.empty();

	// Append the sorted reviews to the list
	reviewList.append(sortedReviews);

	// Update the time labels for each review
	reviewList.find(".time_real").each(function() {
		// Get the review creation time text
		var createTimeText = $(this).text();

		// Convert the review creation time to a Date object
		var createTime = new Date(createTimeText.replace(/\. /g, "-").replace(/\./g, "").replace(/-/g, "/"));

		// Get the current time
		var currentTime = new Date();

		// Calculate the time difference in milliseconds
		var timeDiff = currentTime - createTime;

		// Calculate the time difference in minutes, hours, days, months, and years
		var minutes = Math.floor(timeDiff / (1000 * 60));
		var hours = Math.floor(timeDiff / (1000 * 60 * 60));
		var days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));
		var months = Math.floor(timeDiff / (1000 * 60 * 60 * 24 * 30));
		var years = Math.floor(timeDiff / (1000 * 60 * 60 * 24 * 365));

		// Define the time labels
		var timeLabels = ["방금 전", "분 전", "시간 전", "일 전", "달 전", "년 전"];

		// Choose the appropriate time label based on the time difference
		var timeLabel;
		if (minutes < 1) {
			timeLabel = timeLabels[0];
		} else if (minutes < 60) {
			timeLabel = minutes + timeLabels[1];
		} else if (hours < 24) {
			timeLabel = hours + timeLabels[2];
		} else if (days < 30) {
			timeLabel = days + timeLabels[3];
		} else if (months < 12) {
			timeLabel = months + timeLabels[4];
		} else {
			timeLabel = years + timeLabels[5];
		}

		// Update the text of the element with the calculated time difference
		$(this).text(timeLabel);
	});

	/*222sss11ss2s*/


	document.querySelectorAll(".declaration").forEach(function(button) {
		button.addEventListener("click", function() {
			var modal = document.getElementById("myModa");
			var reviewId = button.getAttribute('data-review-id');
			console.log(reviewId);
			modal.setAttribute('data-review-id', reviewId);
			modal.style.display = "block";
		});
	});

	document.addEventListener("click", function(event) {
		var modal = document.getElementById("myModa");
		if (event.target === modal || event.target.classList.contains("close")) {
			modal.style.display = "none";
		}
	});

	/* 신고 모달 */
$(".declaration").click(function() {
    var modal = $("#myModa");
    var reviewId = $(this).data("review-id");
    console.log(reviewId);
    modal.attr("data-review-id", reviewId);

    // Save the reviewId to local storage
    localStorage.setItem("reviewId", reviewId);

    // Input fields reset
    $("#reason_dec").val("");
    $("#decl_detail").val("");

    modal.css("display", "block");
});

$(document).click(function(event) {
    var modal = $("#myModa");
    if (event.target === modal[0] || $(event.target).hasClass("close")) {
        modal.css("display", "none");
    }
});

$("#declarationForm").submit(function(event) {
    event.preventDefault();

    var reviewId = localStorage.getItem("reviewId");
    var reason = $("#reason_dec").val();
    var declarationDetail = $("#decl_detail").val();

    if (!reason) {
        Swal.fire({
            icon: 'error',
            text: '신고사유는 필수 입력사항 입니다!!'
        });
        return;
    }

    $.ajax({
        type: "POST",
        url: "/user/decl",
        data: {
            reviewId: reviewId,
            reason: reason,
            decl_detail: declarationDetail
        },
        success: function(data) {
            alert('Success');
        },
        error: function(xhr, status, error) {
            Swal.fire({
                position: 'center',
                icon: 'success',
                text: '신고가 접수 되었습니다.',
                showConfirmButton: false,
                timer: 1500
            });

            var modal = $("#myModa");
            modal.css("display", "none");

            // Hide the specific "신고" button for the review
            var targetButton = $(`button[data-review-id="${reviewId}"]`);
            targetButton.hide();
              localStorage.setItem('reportedReviewId_' + reviewId, 'true');
        }
    });
});


$(document).ready(function() {
    // Loop through all the review buttons and check if they were reported
    $(".declaration").each(function() {
        var reviewId = $(this).data("review-id");
        var isReported = localStorage.getItem('reportedReviewId_' + reviewId);
        if (isReported === 'true') {
            $(this).hide(); // Hide the button if it was reported
        }
    });
});

});




function changeTabColor(index) {
	var tabs = document.querySelectorAll('.page_tabs li a');
	for (var i = 0; i < tabs.length; i++) {
		tabs[i].classList.remove('active');
		tabs[i].style.borderBottom = 'none'; // 모든 탭의 하단 테두리 제거
	}
	tabs[index].classList.add('active');
	tabs[index].style.borderBottom = tabs[index].clientWidth + 'px solid red'; // 선택된 탭의 텍스트 길이만큼 하단 테두리 설정
}




function validateForm() {
	var rating = document.querySelector('input[name="reviewStar"]:checked');
	var reviewContents = document.getElementById('reviewContents').value;

	if (!rating || !reviewContents) {
		Swal.fire({
			icon: 'error',
			text: '별점과 내용은 필수 입력사항입니다.'
		});
		return false; // Prevent form submission
	}

	// If all inputs are valid, allow form submission
	return true;
}



