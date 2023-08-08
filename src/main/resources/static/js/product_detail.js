// 진주 작성
(function () {
    var tabContents = document.querySelector('.tabcontents');
    var tabs = document.querySelectorAll('ul.page_tabs li');

    function showTabContent(tabId) {
        var tab = document.getElementById(tabId);
        if (tab) {
            tabContents.childNodes.forEach(function (node) {
                if (node.nodeType === 1) {
                    if (node.id === tabId) {
                        node.style.display = 'block';
                    } else {
                        node.style.display = 'none';
                    }
                }
            });
        }
    }

    function handleTabClick(event) {
        event.preventDefault();
        var target = event.target;
        if (target.tagName === 'A') {
            var tabId = target.getAttribute('href').substring(1);
            showTabContent(tabId);
            tabs.forEach(function (tab) {
                if (tab.querySelector('a') === target) {
                    tab.classList.add('selected');
                } else {
                    tab.classList.remove('selected');
                   }
            });
            // 선택된 탭 정보를 로컬 스토리지에 저장
            localStorage.setItem('selectedTab', tabId);
        }
    }

    function initializeTabs() {
        tabs.forEach(function (tab, index) {
            if (index === 0) {  // 첫 번째 탭을 기본값으로 보이도록 설정
                tab.classList.add('selected');
            }
            tab.addEventListener('click', handleTabClick);
        });
        showTabContent(tabs[0].querySelector('a').getAttribute('href').substring(1));
    }

    initializeTabs();
})();