function selectAll(selectAll) {
    const checkboxes = document.getElementsByName('checkOne');

    checkboxes.forEach((checkbox) => { checkbox.checked = selectAll.checked })
}



