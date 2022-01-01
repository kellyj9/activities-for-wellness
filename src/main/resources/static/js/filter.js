// Toggles the checkboxes for all of the dimensions and activities depending on
// whether the checkbox to select all activities was selected or unselected.
// Parameters:
// source is the id of the top level checkbox that selects (or unselects) all
// dimensions and activities.
// checkboxesIdStartsWith is a string for the part of the id that the
// checkboxes to toggle have in common.
function toggleAll(source, checkboxesIdStartsWith) {
    var allCheckbox = document.getElementById(`${source}`);
    var checkboxes = document.querySelectorAll(`[id^=${checkboxesIdStartsWith}]`);

    if (allCheckbox.checked) {
        selectAll(checkboxes);
    }
    else {
        unSelectAll(checkboxes);
    }

}


// Toggles the checkboxes for all of the dimension's activities depending on
// whether the checkbox to select all of the dimension's activities
// was selected or unselected.
// Parameters:
// source is the id of the Select All checkbox for a dimension.
// checkboxesClass is the class name of the dimension's activity checkboxes
// to toggle.
// checkboxSelectAllId is the id of the top level checkbox that selects
// (or unselects) all dimensions and activities.
function toggle(source, checkboxesClass, checkboxSelectAllId) {
    var dimensionCheckbox = document.getElementById(`${source}`);
    var checkboxes = document.getElementsByClassName(`${checkboxesClass}`);
    var selectAllCheckbox = document.getElementById(`${checkboxSelectAllId}`);

    if (dimensionCheckbox.checked) {
        selectAll(checkboxes);
    }
    else {
        unSelectAll(checkboxes);

        // also unselect the top level checkbox if already checked
        if (selectAllCheckbox.type == 'checkbox' && selectAllCheckbox.checked) {
            selectAllCheckbox.checked = false;
        }

    }

}


// Sets the checkboxes 'checked' attribute to true
// Parameter:
// checkboxes is the list of checkbox elements to set as checked
function selectAll(checkboxes) {
    var items = checkboxes;
    for (var i = 0; i < items.length; i++) {
        if (items[i].type == 'checkbox')
            items[i].checked = true;
    }
}


// Sets the checkboxes 'checked' attribute to false
// Parameter:
// checkboxes is the list of checkbox elements to set as NOT checked
function unSelectAll(checkboxes) {
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
// thShow is the class name of the table header row for the dimension name when it
// is set to be shown.
// thHide is the class name of the table header row for the dimension name when it
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

   // hide all elements on the page with class name tableShow
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
// thShow is the class name of the table header row for the dimension name when it
// is set to be shown.
// thHide is the class name of the table header row for the dimension name when it
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
    var checkboxes = document.querySelectorAll(`[class^=${checkboxesClassStartsWith}]`);
    var rows = document.getElementsByClassName(`${rowsToShow}`);

    for (var i = 0; i < rows.length; i++) {
        // show table header rows that were hidden
        if (rows[i].className == thHide) {
            rows[i].className = thShow;
        }
        // show rows that were hidden
        else if (rows[i].className == trHide) {
            rows[i].className = trShow;
        }
    } // end for

    // show cells that were hidden
    var filterable_td = document.querySelectorAll(`[class*=${tdHide}]`);
    for (var a = 0; a < filterable_td.length; a++) {
        filterable_td[a].classList.remove(`${tdHide}`);
        filterable_td[a].classList.add(`${tdShow}`);
    }

    // show tables that were hidden
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


// If an activity checkbox is unselected, unselects the the top level
// checkbox that is used to select all dimensions and activities and
// also unselects the dimension level checkbox
// Parameters:
// sourceId is the element id for the activity level checkbox
// allCheckboxId is the id of the top level checkbox that selects (or unselects) all
// dimensions and activities.
// dimensionCheckboxId is the element id of the next level checkbox
function setSelectAllCheckboxes(sourceId, allCheckboxId, dimensionCheckboxId) {
    activityCheckbox = document.getElementById(`${sourceId}`);
    allCheckbox = document.getElementById(`${allCheckboxId}`);
    dimensionCheckbox = document.getElementById(`${dimensionCheckboxId}`);

    // verify the elements are checkboxes and
    // if the activity checkbox was unselected but the dimension level checkbox was
    // selected, then unselect the dimension level checkbox and
    // (if checked) unselect the top level Select All checkbox that selects all
    // activities and dimensions
    if (activityCheckbox.type == 'checkbox' && dimensionCheckbox.type == 'checkbox' && allCheckbox.type == 'checkbox' && !activityCheckbox.checked && dimensionCheckbox.checked) {
        dimensionCheckbox.checked = false;
        if (allCheckbox.checked) {
            allCheckbox.checked = false;
        }
    }

}