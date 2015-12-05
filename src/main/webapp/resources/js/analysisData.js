var regId = /\d+/;

function loadCurrentDate() {
	var currentDate = new Date();
	var date = currentDate.getDate();
	var month = currentDate.getMonth() + 1;
	var year = currentDate.getFullYear();
	var newDate = "0";
	var newMonth = "0";

	if (Math.floor(date / 10) == 0)
		newDate += date.toString();
	else
		newDate = date.toString();

	if (Math.floor(month / 10) == 0)
		newMonth += month.toString();
	else
		newMonth = month.toString();
	var val = year + "-" + newMonth + "-" + newDate;
	document.getElementById('date-to').value = val;

}
function loadBeforeWeek() {
	var currentDate = new Date();
	currentDate.setDate(currentDate.getDate() - 7);
	var date = currentDate.getDate();
	var month = currentDate.getMonth() + 1;
	var year = currentDate.getFullYear();
	var newDate = "0";
	var newMonth = "0";

	if (Math.floor(date / 10) == 0)
		newDate += date.toString();
	else
		newDate = date.toString();

	if (Math.floor(month / 10) == 0)
		newMonth += month.toString();
	else
		newMonth = month.toString();
	var val = year + "-" + newMonth + "-" + newDate;
	document.getElementById('date-from').value = val;
}
function setError(message) {
	document.getElementById("display-error").innerHTML = message;
}
function validate() {
	var id = document.getElementById('id-form');
	var isValueId = regId.test(id.value);
	var isDateValid = true;

	var dateFrom = new Date(document.getElementById('date-from').value);
	var dateTo = new Date(document.getElementById('date-to').value);
	var currentDate = new Date();
	if (isValueId == false) {
		setError("Page ID is invalid ");
		id.focus();
		return false;
	}
	if (dateFrom.getTime() >= currentDate.getTime()) {
		document.getElementById('date-from').focus();
		setError("Date from is not great than current date");
		isDateValid = false;
	} else if (dateFrom.getTime() > dateTo.getTime()) {
		setError("Date from is not great than date to");
		document.getElementById('date-to').focus();
		isDateValid = false;
	}

	return isDateValid;

}