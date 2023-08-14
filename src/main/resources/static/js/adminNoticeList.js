$(document).ready(function() {
	$(".notice-content, .notice_btn").hide(); 

	$(".notice-item").click(function() {
		// 현재 클릭된 notice-item 내의 notice-content와 notice_btn을 토글.
		$(this).find(".notice-content, .notice_btn").toggle();
	});

	// 알림창이 나타나도록 이벤트 추가
	$(document).ready(function() {
		$(document).on('click', '.del_btn', function(e) {
			e.preventDefault();
			var id = $(this).data('id');

			Swal.fire({
				title: '공지사항',
				text: "공지사항을 삭제하시겠습니까?",
				icon: 'question',
				showCancelButton: true,
				confirmButtonText: '예',
				cancelButtonText: '아니요'
			}).then((result) => {
				if (result.isConfirmed) {
					Swal.fire({
						title: '공지사항',
						text: '공지사항이 삭제되었습니다.',
						icon: 'success'
					}).then(() => {
						window.location.href = '/admin/delete/' + id;
					});
				} else if (result.dismiss === Swal.DismissReason.cancel) {
					Swal.fire({
						title: '공지사항',
						text: '삭제가 취소되었습니다.',
						icon: 'info'
					});
				}
			});
		});
	});
});