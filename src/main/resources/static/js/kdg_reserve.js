
/*
//전체 선택, 전체선택 해제
function selectAll(selectAll) {
	const checkboxes = document.getElementsByName('checkOne');

	checkboxes.forEach((checkbox) => { checkbox.checked = selectAll.checked })
}
*/

//HTML 문서가 완전히 로드되고 나서 실행
document.addEventListener('DOMContentLoaded', function() {
	//전체 선택

	//모든 체크박스 가능한 checkAll을 가져온다
	const checkAll = document.getElementById('checkAll');

	//checkAll 이벤트가 발생
	checkAll.addEventListener('click', function() {

		//checkAll에 체크 이벤트 추가
		const isChecked = checkAll.checked;

		//checkAll 이 체크가 되어서 true라면
		if (isChecked) {
			//모든 inp_chk_02를 true로 변경
			const checkboxes = document.querySelectorAll('.inp_chk_02');

			for (const checkbox of checkboxes) {
				checkbox.checked = true;
			}
		}

		else {
			//checkAll 이 체크가 되어서 true 아니라면
			const checkboxes = document.querySelectorAll('.inp_chk_02');
			//모든 inp_chk_02를 false로 변경
			for (const checkbox of checkboxes) {
				checkbox.checked = false;
			}
		}
	})

	//부분 선택 시 모두 누르면 checkAll 버튼이 true가 된다

	//inp_chk_02를 가진 모든 요소를 선택
	const checkboxes = document.querySelectorAll('.inp_chk_02');

	//checkboxes 배열에 저장된 각 체크박스 요소에 대해 반복
	for (const checkbox of checkboxes) {
		checkbox.addEventListener('click', function() {

			const totalCnt = checkboxes.length;

			const checkedCnt = document.querySelectorAll('.inp_chk_02:checked').length;

			//체크된 체크박스의 수(checkedCnt)와 전체 체크박스의 수(totalCnt)를 비교
			if (totalCnt == checkedCnt) {
				//만일 모두 체크되었으면 checkAll에 체크 이벤트 추가
				document.querySelector('#checkAll').checked = true;
			}
			else {//만일 모두 체크되어있지 않으면 checkAll에 체크 이벤트 false
				document.querySelector('#checkAll').checked = false;
			}

		});

	}
});