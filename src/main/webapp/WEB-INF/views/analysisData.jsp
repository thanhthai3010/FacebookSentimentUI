<!DOCTYPE HTML>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>Social Analysis Process</title>
<link rel="shortcut icon"  href="<c:url value="/resources/image/favicon.ico" />"  type='image/vnd.microsoft.icon'/>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script
	src="<c:url value="/resources/js/jquery.js" />"></script>
<script
	src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<script
	src="<c:url value="/resources/js/analysisData.js" />"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/analysisData.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/screenLoading.css" />">
<style>
#display-error {
	color: red;
	font-size: 1.5em;
	margin-top: 15px;
}
</style>

</head>

<body>

<!-- Loading screen -->
	<div id="loader-wrapper">
		<div id="box-light"></div>
		<div id="loader"></div>
	</div>

	<div class="analysis">
		<div class="heading">
			<h2>
				Welcome to <br> Social Analysis
			</h2>
			<form action="startAnalysis" role="form" data-toggle="validator" method="post">

				<div class="input-group input-group-lg">
					<span class="input-group-addon"> <i class="glyphicon glyphicon-th-list"></i></span>
					
					<select class="form-control"  name="pageID">
					<c:forEach var="pageInfo" items="${listPageInfo}">
						<option value="${pageInfo.pageID }">${pageInfo.pageName }</option>
					</c:forEach>
					</select>
				</div>

				<div class="input-group input-group-lg">
					<span class="input-group-addon">
					<i class="glyphicon glyphicon-calendar"></i>
					</span>
					<input type="date" class="form-control"  id="date-from" name="dateFrom" >
					<input type="date" class="form-control"  id="date-to" name="dateTo" >
				</div>

				<button type="submit" class="float" onclick="return validate();">Start Analysis</button>
				<div id="display-error">${error}</div>
			</form>
		</div>
	</div>

	<script>
		loadCurrentDate();
		loadBeforeWeek();
	</script>

</body>
</html>