<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>Display topic detail</title>
<link rel="shortcut icon"
	href="<c:url value="/resources/image/favicon.ico" />"
	type='image/vnd.microsoft.icon' />
<!--Load the AJAX API-->
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script src="<c:url value="/resources/js/jquery.js" />"></script>
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"
	href="<c:url value="/resources/css/detailOfTopic.css" />">
<script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<script src="<c:url value="/resources/js/detailOfTopic.js" />"></script>

</head>

<body>
	<div class="row">
		<div class="col-md-6">
			<div id="chart_div" style="width: 700; height: 500"></div>
		</div>
		<div class="col-md-6">
			<h3>PAGE INFO</h3>
			<table class="table">
				<thead>
					<tr>
						<th>Title</th>
						<th>Information</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Profile Picture</td>
						<td><img src="${urlImage}" alt="Page Profile Picture"></td>
					</tr>

					<tr>
						<td>About</td>
						<td>${about}</td>
					</tr>

					<tr>
						<td>Description</td>
						<td>${description}</td>
					</tr>

					<tr>
						<td>Website</td>
						<td>${website}</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

	<div class="row">
		<div class="container">
			<h2>Detail of sentiment analsis</h2>
			<div id="report_div" style="font-size: 1.15em; color: white;">

				<div class="panel with-nav-tabs panel-primary">
					<div class="panel-heading">
						<ul class="nav nav-tabs">
							<li class="active"><a href="#tabPositive" data-toggle="tab">Positive</a></li>
							<li><a href="#tabNeutral" data-toggle="tab">Neutral</a></li>
							<li><a href="#tabNegative" data-toggle="tab">Negative</a></li>
						</ul>
					</div>
					<div class="panel-body">
						<div class="tab-content">
							<div class="tab-pane fade in active" id="tabPositive"></div>

							<div class="tab-pane fade" id="tabNeutral"></div>

							<div class="tab-pane fade" id="tabNegative"></div>
						</div>
					</div>
				</div>

			</div>
		</div>
		<input type="hidden" id="topicID" value="${topicID }">

	</div>

	<script>
		$.ajax({
			url : 'https://www.google.com/jsapi?callback',
			cache : true,
			dataType : 'script',
			success : function() {
				google.load('visualization', '1', {
					packages : [ 'corechart' ],
					'callback' : function() {
						$.get("topicID?id=" + $("#topicID").val(), function(
								data) {

							var obj = JSON.parse(data);

							// draw Pie Chart first
							drawChart(JSON.parse(obj.listPieChart));

							// draw detail data
							// 							displayDetailData(JSON.parse(obj.listDetailData));
							var code = generateCodeStatus(JSON
									.parse(obj.listDetailData));
							$("#tabPositive").append(code.html1);
							$("#tabNeutral").append(code.html2);
							$("#tabNegative").append(code.html3);
						});
					}
				});
				return true;
			}
		});
	</script>
</body>
</html>