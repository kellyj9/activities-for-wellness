// source is the element id
// checkboxesClassName is the element(s) classname
// checkboxSelectAllId is the id of the top level checkbox
function toggle(source, checkboxesClassName, checkboxSelectAllId) {
    var selectAllCheckbox = document.getElementById(`${checkboxSelectAllId}`);
    var dimensionCheckbox = document.getElementById(`${source}`);
    var checkboxes = document.getElementsByClassName(`${checkboxesClassName}`);
    if (dimensionCheckbox.checked) {
        selectAll(checkboxes);
    }
    else {
        UnSelectAll(checkboxes);
        // unselect the top level checkbox
        if (selectAllCheckbox.type == 'checkbox' && selectAllCheckbox.checked) {
            selectAllCheckbox.checked = false;
        }
    }
}

// source is the element id
// checkboxesClassName is the element(s) attribute value with wild card
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

// checkboxes is the element(s)
function selectAll(checkboxes) {
    var items = checkboxes;
    for (var i = 0; i < items.length; i++) {
        if (items[i].type == 'checkbox')
            items[i].checked = true;
    }
}

// checkboxes is the element(s)
function UnSelectAll(checkboxes) {
    var items = checkboxes;
    for (var i = 0; i < items.length; i++) {
        if (items[i].type == 'checkbox')
            items[i].checked = false;
    }
}

// NOTE:
// the number of checkboxes must match the number of items for the class name
function filterSelection() {
    var x, i;

    // get elements where the class name starts with...
    var checkboxes = document.querySelectorAll('[class^="dimension"]');

    x = document.getElementsByClassName("filter1");

     // counter to check if there are any activities selected for a dimension
     var counter = -1;
     var j = -1; // will hold the index of the current table header

    // traverse the list of checkboxes and only show selected table headers that
    // have selected child rows
     for (i = 0; i < x.length; i++) {
        if (x[i].className == "filter1 th show") {
            counter = 0;
            j = i; // save the index of the current table header
            while (((i+1) < x.length) && (x[i+1].className == "filter1 tr show")) {
                i++; // increase the index for a table row

                // hide the row
                x[i].className = "filter1 tr hide";

                // if there is a selected checkbox for the child row of the table header
                // re-show the row
                if (checkboxes[i].checked) {
                    x[i].className = "filter1 tr show";
                    counter++;
                }
            } // end while

            // if counter is still 0, hide the table header row because there were no
            // selected child rows
            if (counter == 0) {
                x[j].className = "filter1 th hide";
            }

        } // end if
    } // end for

    // get elements to remove from view
    var a, b;
    var filterable_td = document.querySelectorAll('[class*="tdshow"]');
    for (a = 0; a < filterable_td.length; a++) {
        filterable_td[a].classList.remove('tdshow');
        filterable_td[a].classList.add('tdhide');
    }

   var filterable_table = document.querySelectorAll('[class*="tableshow"]');
   for (b = 0; b < filterable_table.length; b++) {
        filterable_table[b].classList.remove('tableshow');
        filterable_table[b].classList.add('tablehide');
   }

    // show the button that removes the filter
   var unfilter_button = document.getElementById('unfilter');
   unfilter_button.classList.remove('unfilterhide');
   unfilter_button.classList.add('unfiltershow');

   // hide the button that activates the filter
  var filter_button = document.getElementById('filter');
  filter_button.classList.remove('filtershow');
  filter_button.classList.add('filterhide');
}

function unfilterSelection() {
    var x, i;
    // get elements where the class name starts with...
    var checkboxes = document.querySelectorAll('[class^="dimension"]');
    x = document.getElementsByClassName("filter1");
    for (i = 0; i < x.length; i++) {
        // show any hidden table headers
        if (x[i].className == "filter1 th hide") {
            x[i].className = "filter1 th show";
        }
        // show any hidden rows
        else if (x[i].className == "filter1 tr hide") {
            x[i].className = "filter1 tr show";
        }
    } // end for

    // get elements to add back to view
    var a, b;
    var filterable_td = document.querySelectorAll('[class*="tdhide"]');
    for (a = 0; a < filterable_td.length; a++) {
        filterable_td[a].classList.remove('tdhide');
        filterable_td[a].classList.add('tdshow');
    }

   var filterable_table = document.querySelectorAll('[class*="tablehide"]');
   for (b = 0; b < filterable_table.length; b++) {
        filterable_table[b].classList.remove('tablehide');
        filterable_table[b].classList.add('tableshow');
   }

       // hide the button that removes the filter
      var unfilter_button = document.getElementById('unfilter');
      unfilter_button.classList.remove('unfiltershow');
      unfilter_button.classList.add('unfilterhide');

      // show the button that activates the filter
     var filter_button = document.getElementById('filter');
     filter_button.classList.remove('filterhide');
     filter_button.classList.add('filtershow');
}

// unselects a checkbox if other checkboxes are unselected
// allCheckbox in the element id of the top level checkbox
// dimensionCheckbox is the element id of the next level checkbox
// activityCheckbox is an element id for a third level checkbox
function setSelectAll(allCheckbox, dimensionCheckbox, activityCheckbox) {
    x_allCheckbox = document.getElementById(`${allCheckbox}`);
    y_dimensionCheckbox = document.getElementById(`${dimensionCheckbox}`);
    z_activityCheckbox = document.getElementById(`${activityCheckbox}`);

    if (z_activityCheckbox.type == 'checkbox' && y_dimensionCheckbox.type == 'checkbox' && x_allCheckbox.type == 'checkbox' && !z_activityCheckbox.checked && y_dimensionCheckbox.checked) {
        y_dimensionCheckbox.checked = false;
        if (x_allCheckbox.checked) {
            x_allCheckbox.checked = false;
        }
    }
}