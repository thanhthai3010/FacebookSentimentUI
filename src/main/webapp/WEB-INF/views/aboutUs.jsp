<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>About us</title>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="<c:url value="/resources/js/jquery.js" />"></script>
<script
	src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/getFBData.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/screenLoading.css" />">
<link rel="stylesheet"
	href="<c:url value="/resources/css/navigation.css" />">

</head>

<body>
<jsp:include page="navigation.jsp" ></jsp:include>
<!-- Loading screen -->
	<div id="loader-wrapper">
		<div id="box-light"></div>
		<div id="loader"></div>
	</div>

	<div class="getData">
		<div class="heading">
			<h2>
				Topic modeling and sentiment analysis system
			</h2>
			<h3>This project is for thesis of University of Information Technology</h3>
			<h3>Author: Thai Thanh & Quan Tran</h3>
		</div>
	</div>
</body>
</html>