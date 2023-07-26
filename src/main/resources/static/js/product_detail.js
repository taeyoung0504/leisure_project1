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
        tabs.forEach(function (tab) {
            tab.addEventListener('click', handleTabClick);
        });
        var selectedTab = localStorage.getItem('selectedTab');
        if (selectedTab) {
            var tabLink = document.querySelector('ul.page_tabs li a[href="#' + selectedTab + '"]');
            if (tabLink) {
                var tabId = tabLink.getAttribute('href').substring(1);
                showTabContent(tabId);
                tabLink.parentNode.classList.add('selected');
            }
        }
    }

    initializeTabs();
})();