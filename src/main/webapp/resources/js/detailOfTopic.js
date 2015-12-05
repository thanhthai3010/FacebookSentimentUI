String.format = function() {
	// The string containing the format items (e.g. "{0}")
	// will and always has to be the first argument.
	var theString = arguments[0];

	// start with the second argument (i = 1)
	for (var i = 1; i < arguments.length; i++) {
		// "gm" = RegEx options for Global search (more than one instance)
		// and for Multiline search
		var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
		theString = theString.replace(regEx, arguments[i]);
	}

	return theString;
}

var pieChart;
function drawChart(pieChart) {
	// Create the data table.
	var data = new google.visualization.arrayToDataTable(pieChart);

	// Set chart options
	var options = {
		title : 'Statistical Of Sentiment Analysis',
		width : 700,
		height : 500,
		is3D : true,
		slices : [ {
			color : '#00a300'
		}, {
			color : '#ee1111'
		}, {
			color : '#eff4ff'
		} ]
	};

	// Instantiate and draw our chart, passing in some options.
	var chart = new google.visualization.PieChart(document
			.getElementById('chart_div'));
	chart.draw(data, options);
}
function convertToColorString(intColor) {
	switch (intColor) {
	case 0:
		return '#eff4ff';
	case 1:
		return '#00a300';
	case -1:
		return '#ee1111';
	}
}

function displayChart(pie_chart) {
	// pieChart = pie_chart;
	// Load the Visualization API and the piechart package.
	google.load('visualization', '1.0', {
		'packages' : [ 'corechart' ]
	});
	// Set a callback to run when the Google Visualization API is loaded.
	google.setOnLoadCallback(drawChart);
}

function displayDetailData(data) {
	var str = "";
	for (var i = 0; i < data.length; i++) {
		str += String
				.format(
						"<p style='background-color: {0};' class = 'pieInfor' class='panel panel-default' >{1} </p>",
						convertToColorString(data[i].typeColor),
						data[i].contentData);
	}
	$('#chart').append(str);
}