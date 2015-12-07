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
			color : '#228B22'
		}, {
			color : '#FA8072'
		}, {
			color : '#FFE4C4'
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
		return '#FFE4C4';
	case 1:
		return '#228B22';
	case -1:
		return '#FA8072';
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

// draw report data

/**
 * get color for panel
 * @param typeColor
 * @returns {String}
 */
function getPanelColor(typeColor) {
	var panelColor = "";
	switch (typeColor) {
	case -1:
		panelColor = "panel-danger";
		break;
	case 0:
		panelColor = "panel-warning";
		break;
	case 1:
		panelColor = "panel-success";
		break;
	}
	return panelColor;
}

/**
 * get color for label
 * @param typeColor
 * @returns {String}
 */
function getLabelColor(typeColor) {
	var labelColor = "";
	switch (typeColor) {
	case -1:
		labelColor = "label-danger";
		break;
	case 0:
		labelColor = "label-warning";
		break;
	case 1:
		labelColor = "label-success";
		break;
	}
	return labelColor;
}

/**
 * draw a list detail for report
 * @param reportObj
 */
function displayDetailData(reportObj) {
	for ( var i in reportObj) {

		var strGroup = "";
		strGroup = '	<div class="panel-group" >\
			<div class="panel '
				+ getPanelColor(reportObj[i].statusData.typeColor)
				+ '">\
				<div class="panel-heading">\
					<h3 class="panel-title">\
						<a data-toggle="collapse" href="#collapse'
				+ i
				+ '">'
				+ reportObj[i].statusData.contentData
				+ '</a>\
					</h3>\
				</div>\
				<div class="panel-collapse collapse" id="collapse'
				+ i + '">';

		for ( var j in reportObj[i].listCommentData) {
			strGroup += '<div class="panel-body '
					+ getLabelColor(reportObj[i].listCommentData[j].typeColor)
					+ ' ">' + reportObj[i].listCommentData[j].contentData
					+ '</div>';
		}
		strGroup+='<div class="panel-footer panel-primary">End of status</div>\
			</div>\
			</div>\
		</div>';
		$('#report_div').append(strGroup);
	}
}