<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
  <title>Display topic detail</title>
  <link rel="shortcut icon"  href="<c:url value="/resources/image/favicon.ico" />"  type='image/vnd.microsoft.icon'/>
    <!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
 <script src="<c:url value="/resources/js/jquery.js" />"></script>
<link rel="stylesheet"
	href="<c:url value="/resources/css/bootstrap.min.css" />">
<script
	src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<script
	src="<c:url value="/resources/js/detailOfTopic.js" />"></script>
<style>

.pieInfor{
  border: 1px solid #eeeeee;
  margin: 6px 0px 6px 0px;
  padding: 6px 10px 6px 10px;
  font-family: Arial;
  font-size: 18px;
  -webkit-border-radius: 8px;
}

</style>
  </head>

  <body>
    <div id="chart_div" style="width:700; height:500"></div>
<div id="chart" ></div>
<input type="hidden" id="topicID" value="${topicID }">

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
							displayDetailData(JSON.parse(obj.listPieData));
						});
					}
				});
				return true;
			}
		});
	</script>
</body>
</html>