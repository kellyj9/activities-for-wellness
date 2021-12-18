function toggle(source, checkboxesId) {
    var dimensionCheckbox = document.getElementById(`${source}`);
    var checkboxes = document.getElementsByClassName(`${checkboxesId}`);
    if (dimensionCheckbox.checked) {
        selectAll(checkboxes);
    }
    else {
        UnSelectAll(checkboxes);
    }
}

function toggleAll(source, checkboxesId) {
    var allCheckbox = document.getElementById(`${source}`);
    var checkboxes = document.querySelectorAll('[id^="dimension"]');

    if (allCheckbox.checked) {
        selectAll(checkboxes);
    }
    else {
        UnSelectAll(checkboxes);
    }
}

function selectAll(checkboxes) {
    var items = checkboxes;
    for (var i = 0; i < items.length; i++) {
        if (items[i].type == 'checkbox' && !(items[i].disabled))
            items[i].checked = true;
    }
}

function UnSelectAll(checkboxes) {
    var items = checkboxes;
    for (var i = 0; i < items.length; i++) {
        if (items[i].type == 'checkbox')
            items[i].checked = false;
    }
}
