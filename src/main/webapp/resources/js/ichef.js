function giveFocus(elementId) {
	var element = document.getElementById(elementId);
	if (element != null) {
		element.focus();
	}
}