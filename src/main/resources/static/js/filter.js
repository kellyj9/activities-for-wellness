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


// Hides elements on the page based on checkboxes not selected.
// Hides the button that activates the filter and
// shows the button to remove the filter.
// Note: The checkboxes for the dimensions and activities must have a class
// name that starts with the string in param checkboxesClassNameStartsWith.
// Parameters:
// checkboxClassStartsWith is the class name of the checkboxes
// associated with the dimension names and activity descriptions.
// rowsToShowOrHide is the class name contained in the element (which is a table
// heading or table row) to show or hide for a dimension name or activity description.
// thShow is the class name of the table header for the dimension name when it
// is set to be shown.
// thHide is the class name of the table header for the dimension name when it
// is to set to be hidden.
// trShow is the class name of the table header for the activity description when it
// is set to be shown.
// trHide is the class name of the table header for the activity description when it
// is set to be hidden.
// tdShow is the class name for the td when it is set to be shown.
// tdHide is the class name for the td when it is set to be hidden.
// tableShow is the class name for the table when it is set to be shown.
// tableHide is the class name for the table when it is set to be hidden.
// unfilter is the id of the button used to unfilter.
// unfilterShow is the class name of the button used to unfilter when it is set to be
// shown.
// unfilterHide is the class name of the button used to unfilter when it is set to be
// hidden.
// filter is the id of the button used to filter.
// filterShow is the class name of the button used to filter when it is set to be shown.
// filterHide is the class name of the button used to filter when it is set to be hidden.
function filterSelection(checkboxesClassStartsWith, rowsToShowOrHide, thShow, thHide, trShow, trHide, tdShow, tdHide, tableShow, tableHide, unfilter, unfilterShow, unfilterHide, filter, filterShow, filterHide) {

    // get elements where the class name starts with...
    //var checkboxes = document.querySelectorAll('[class^="dimension"]');
    var checkboxes = document.querySelectorAll(`[class^=${checkboxesClassStartsWith}]`);

    var rows = document.getElementsByClassName(`${rowsToShowOrHide}`);

    // counter to indicate if there are any activities selected for a dimension
    var counter = -1;
    // will hold the index of the current table header
    var j = -1;

    // traverse the list of checkboxes.  only set an activity row to show if the
    // corresponding checkbox was selected.
    // also, only show dimension header rows if they have at least one activity
    // selected.
     for (var i = 0; i < rows.length; i++) {
        if (rows[i].className == thShow) {
            counter = 0;
            j = i;  // save the index of the current table header
            while (((i+1) < rows.length) && (rows[i+1].className == trShow)) {
                i++; // increase the index for a table row

                // hide the row
                rows[i].className = trHide;

                // if there was a checkbox selected for the activity, then
                // re-show the row
                if (checkboxes[i].checked) {
                    rows[i].className = trShow;
                    // increase the counter to indicate that an
                    // activity checkbox for the dimension was selected
                    counter++;
                }
            } // end while

            // if counter is still 0, hide the dimension table header row
            // because there were no selected activities for the dimension
            if (counter == 0) {
                rows[j].className = thHide;
            }

        } // end if
    } // end for

    // hide unneeded elements currently showing on the page

    // hide all elements on the page with the class name tdShow
    var a, b;
    var filterable_td = document.querySelectorAll(`[class*=${tdShow}]`);
    for (a = 0; a < filterable_td.length; a++) {
        filterable_td[a].classList.remove(`${tdShow}`);
        filterable_td[a].classList.add(`${tdHide}`);
    }

   // hide all the elements on the page with classname tableShow
   var filterable_table = document.querySelectorAll(`[class*=${tableShow}]`);
   for (b = 0; b < filterable_table.length; b++) {
        filterable_table[b].classList.remove(`${tableShow}`);
        filterable_table[b].classList.add(`${tableHide}`);
   }

    // show the button that removes the filter
   var unfilter_button = document.getElementById(`${unfilter}`);
   unfilter_button.classList.remove(`${unfilterHide}`);
   unfilter_button.classList.add(`${unfilterShow}`);

   // hide the button that activates the filter
  var filter_button = document.getElementById(`${filter}`);
  filter_button.classList.remove(`${filterShow}`);
  filter_button.classList.add(`${filterHide}`);
}


// Shows the elements on the page that were hidden and
// hides the button that removes the filter.
// Parameters:
// checkboxClassStartsWith is the class name of the checkboxes
// associated with the dimension names and activity descriptions.
// rowsToShow is the class name contained in the element (which is a table heading
// or table row) to show for a dimension name or activity description.
// thShow is the class name of the table header for the dimension name when it
// is set to be shown.
// thHide is the class name of the table header for the dimension name when it
// is to set to be hidden.
// trShow is the class name of the table header for the activity description when it
// is set to be shown.
// trHide is the class name of the table header for the activity description when it
// is set to be hidden.
// tdShow is the class name for the td when it is set to be shown.
// tdHide is the class name for the td when it is set to be hidden.
// tableShow is the class name for the table when it is set to be shown.
// tableHide is the class name for the table when it is set to be hidden.
// unfilter is the id of the button used to unfilter.
// unfilterShow is the class name of the button used to unfilter when it is set to be
// shown.
// unfilterHide is the class name of the button used to unfilter when it is set to be
// hidden.
// filter is the id of the button used to filter.
// filterShow is the class name of the button used to filter when it is set to be shown.
// filterHide is the class name of the button used to filter when it is set to be hidden.
function unfilterSelection(checkboxesClassStartsWith, rowsToShow, thShow, thHide, trShow, trHide, tdShow, tdHide, tableShow, tableHide, unfilter, unfilterShow, unfilterHide, filter, filterShow, filterHide) {
    // get elements where the class name starts with...
    var checkboxes = document.querySelectorAll(`[class^=${checkboxesClassStartsWith}]`);
    var rows = document.getElementsByClassName(`${rowsToShow}`);
    for (var i = 0; i < rows.length; i++) {
        // show any hidden table headers
        if (rows[i].className == thHide) {
            rows[i].className = thShow;
        }
        // show any hidden rows
        else if (rows[i].className == trHide) {
            rows[i].className = trShow;
        }
    } // end for

    // add elements to add back to view

    var filterable_td = document.querySelectorAll(`[class*=${tdHide}]`);
    for (var a = 0; a < filterable_td.length; a++) {
        filterable_td[a].classList.remove(`${tdHide}`);
        filterable_td[a].classList.add(`${tdShow}`);
    }

   var filterable_table = document.querySelectorAll(`[class*=${tableHide}]`);
   for (var b = 0; b < filterable_table.length; b++) {
        filterable_table[b].classList.remove(`${tableHide}`);
        filterable_table[b].classList.add(`${tableShow}`);
   }

       // hide the button that removes the filter
      var unfilter_button = document.getElementById(`${unfilter}`);
      unfilter_button.classList.remove(`${unfilterShow}`);
      unfilter_button.classList.add(`${unfilterHide}`);

      // show the button that activates the filter
     var filter_button = document.getElementById(`${filter}`);
     filter_button.classList.remove(`${filterHide}`);
     filter_button.classList.add(`${filterShow}`);
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