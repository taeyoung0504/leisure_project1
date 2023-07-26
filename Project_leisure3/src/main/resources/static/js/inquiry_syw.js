// 새 문의 작성하기 버튼 클릭 이벤트 핸들러
    document.getElementById('new-inquiry-button').addEventListener('click', function() {
      var newInquiryForm = document.getElementById('new-inquiry-form');
      var myInquiryContainer = document.getElementById('my-inquiry-container');
      
      newInquiryForm.style.display = 'block';
      myInquiryContainer.style.display = 'none';
    });

    // 내 문의 보기 버튼 클릭 이벤트 핸들러
    document.getElementById('view-inquiry-button').addEventListener('click', function() {
      var newInquiryForm = document.getElementById('new-inquiry-form');
      var myInquiryContainer = document.getElementById('my-inquiry-container');

      newInquiryForm.style.display = 'none';
      myInquiryContainer.style.display = 'block';
    });

    // 문의 작성 폼 제출 이벤트 핸들러
    document.getElementById('new-inquiry-form').addEventListener('submit', function(event) {
      event.preventDefault(); // 폼 제출 기본 동작 막기

      // 입력한 값 가져오기
      var category = document.getElementById('category').value;
      var type = document.getElementById('type').value;
      var title = document.getElementById('inquiry-title-input').value;
      var message = document.getElementById('message').value;

      // 새로운 문의 생성
      var newInquiry = document.createElement('div');
      newInquiry.classList.add('inquiry');
      
      var inquiryTitle = document.createElement('div');
      inquiryTitle.classList.add('inquiry-title');
      inquiryTitle.innerText = title; // 제목 표시
      
      var inquiryDate = document.createElement('div');
      inquiryDate.classList.add('inquiry-date');
      inquiryDate.innerText = new Date().toLocaleString();
      
      var inquiryContent = document.createElement('div');
      inquiryContent.classList.add('inquiry-content');
      inquiryContent.style.display = 'none';
      inquiryContent.innerHTML = '<p>' + message + '</p>';

      newInquiry.appendChild(inquiryTitle);
      newInquiry.appendChild(inquiryDate);
      newInquiry.appendChild(inquiryContent);

      // 문의 리스트에 추가
      var inquiryList = document.getElementById('my-inquiry');
      inquiryList.appendChild(newInquiry);
      
      // 내 문의 내역 클릭 이벤트 핸들러 연결
      newInquiry.addEventListener('click', function() {
        inquiryContent.style.display = (inquiryContent.style.display === 'none') ? 'block' : 'none';
      });

      // 폼 초기화
      document.getElementById('category').value = '';
      document.getElementById('type').value = '';
      document.getElementById('inquiry-title-input').value = '';
      document.getElementById('message').value = '';
    });