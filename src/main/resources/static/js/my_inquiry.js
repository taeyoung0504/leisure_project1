$(document).ready(function () {
   $(".inquiry-item").click(function (event) {
      if (!$(event.target).hasClass("content-toggle")) {
         var content = $(this).find(".inquiry-content, .answer-content");
         var image = $(this).find(".toggle-image");

         content.slideToggle(function () {

            if (content.is(":visible")) {
               image.attr("src", "/img/key/up.png");
            } else {
               image.attr("src", "/img/key/down.png");
            }
         });
      }
   });
   
   

   $(".content-toggle").click(function (event) {
            event.stopPropagation();
            $(this).closest(".inquiry-item").find(".button-container").toggle();
         });

        $(".delete-button").click(function (event) {
    event.stopPropagation();
    event.preventDefault();
    var href = $(this).attr('href');

    Swal.fire({
        title: '정말 삭제하시겠습니까?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니요'
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 완료',
                text: '삭제 되었습니다.',
                icon: 'success'
            }).then(() => {
                window.location.href = href;
            });
        } else {
            Swal.fire({
                title: '삭제 취소',
                text: '삭제가 취소되었습니다.',
                icon: 'info'
            });
        }
    });
});
});