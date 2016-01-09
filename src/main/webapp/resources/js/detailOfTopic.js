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
		is3D : false,
		slices : [ {
			color : '#168616'
		}, {
			color : '#E2241E'
		}, {
			color : '#f0ad4e'
		} ]
	};

	// Instantiate and draw our chart, passing in some options.
	var chart = new google.visualization.PieChart(document
			.getElementById('chart_div'));
	chart.draw(data, options);
	
	// hide loding screen
//	$('.spinner').css("display", "none");
	$('.spinner').hide();
	$('#chart_div').show();
	
}
function convertToColorString(intColor) {
	switch (intColor) {
	case 1:
		return '#168616';
	case 0:
		return '#f0ad4e';
	case -1:
		return '#E2241E';
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
 * 
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
 * 
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
 * 
 * @param reportObj
 */
// function displayDetailData(reportObj) {
// for ( var i in reportObj) {
//
// var strGroup = "";
// strGroup = ' <div class="panel-group" >\
// <div class="panel '
// + getPanelColor(reportObj[i].statusData.typeColor)
// + '">\
// <div class="panel-heading">\
// <h3 class="panel-title">\
// <a data-toggle="collapse" href="#collapse'
// + i
// + '">'
// + reportObj[i].statusData.contentData
// + '</a>\
// </h3>\
// </div>\
// <div class="panel-collapse collapse" id="collapse'
// + i + '">';
//
// for ( var j in reportObj[i].listCommentData) {
// strGroup += '<div class="panel-body '
// + getLabelColor(reportObj[i].listCommentData[j].typeColor)
// + ' ">' + reportObj[i].listCommentData[j].contentData
// + '</div>';
// }
// strGroup+='<div class="panel-footer panel-primary">End of status</div>\
// </div>\
// </div>\
// </div>';
// $('#report_div').append(strGroup);
// }
// }
function generateCodeComment(listCommentData) {
	var html = "";

	for (j in listCommentData) {

		html += '<div class="panel-body '
				+ getLabelColor(listCommentData[j].typeColor) + ' ">'
				+ listCommentData[j].contentData + '</div>';
	}
	return html;
}

function generateCodeStatus(listDetailData) {
	this.html1 = "";
	this.html2 = "";
	this.html3 = "";

	for (i in listDetailData) {

		switch (listDetailData[i].statusData.typeColor) {
		case 1:
			var html = '<div class="panel-group">\
     <div class="panel panel-success">\
       <div class="panel-heading">\
      <h4 class="panel-title">\
        <a data-toggle="collapse" href="#collapse'
					+ i
					+ '">'
					+ listDetailData[i].statusData.contentData
					+ '</a>\
      </h4>\
       </div>\
       <div id="collapse'
					+ i
					+ '" class="panel-collapse collapse" style="margin-left: 20px;">\
      '
					+ generateCodeComment(listDetailData[i].listCommentData)
					+ '\
      <div class="panel-footer">End of status</div>\
       </div>\
     </div>\
      </div>';
			this.html1 += html
			break;
		case 0:
			var html = '<div class="panel-group">\
     <div class="panel panel-warning">\
       <div class="panel-heading">\
      <h4 class="panel-title">\
        <a data-toggle="collapse" href="#collapse'
					+ i
					+ '">'
					+ listDetailData[i].statusData.contentData
					+ '</a>\
      </h4>\
       </div>\
       <div id="collapse'
					+ i
					+ '" class="panel-collapse collapse" style="margin-left: 20px;">\
      '
					+ generateCodeComment(listDetailData[i].listCommentData)
					+ '\
      <div class="panel-footer">End of status</div>\
       </div>\
     </div>\
      </div>';
			this.html2 += html;
			break;
		case -1:
			var html = '<div class="panel-group">\
     <div class="panel panel-danger">\
       <div class="panel-heading">\
      <h4 class="panel-title">\
        <a data-toggle="collapse" href="#collapse'
					+ i
					+ '">'
					+ listDetailData[i].statusData.contentData
					+ '</a>\
      </h4>\
       </div>\
       <div id="collapse'
					+ i
					+ '" class="panel-collapse collapse" style="margin-left: 20px;">\
      '
					+ generateCodeComment(listDetailData[i].listCommentData)
					+ '\
      <div class="panel-footer">End of status</div>\
       </div>\
     </div>\
      </div>';
			this.html3 += html;
			break;
		}
	}
	return this;
}