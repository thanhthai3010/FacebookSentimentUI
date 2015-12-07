
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.restfb.types.Post" %>
<html>
<head>
	<title>Welcome to Social Network Analysis</title>
<link rel="shortcut icon"  href="<c:url value="/resources/image/favicon.ico" />"  type='image/vnd.microsoft.icon'/>
<link rel="stylesheet"
	href="<c:url value="/resources/css/bootstrap.min.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/homePage.css" />">

<!-- jQuery library -->
<script src="<c:url value="/resources/js/jquery.js" />"></script>

<!-- Latest compiled JavaScript -->
<script
	src="<c:url value="/resources/js/bootstrap.min.js" />"></script>

<!-- Script -->
<script type="text/javascript">
$(function(){
	// View get facebook data page
	$('#getFBData').click(function() {
		var url = "./getFBData";
		window.location = url;
	});

	//View analysing data page
	$('#analysisData').click(function() {
		var url = "./analysisData";
		window.location = url;
	});

	//View analysing data page
	$('#aboutUs').click(function() {
		var url = "./aboutUs";
		window.location = url;
	});
});

</script>

</head>

<div class="row">
	<div class="col-sm-10  header-title">	
		<h1>University of Infomation Techology</h1>
		<h4>Social Network Analysis Home Page</h4>
	</div>
</div>
<div class="row">
<div class="header-contain col-sm-10">
	<div class="col-sm-4 box-info"  id="getFBData">
		<img src="<c:url value="/resources/image/data.png"/>" height=100 />
		<h2>DATA CRAWLING</h2>
		<h3>Get Social Network data for analysis.</h3>
	</div>
	<div class="col-sm-4 box-info" id="analysisData">
		<img src="<c:url value="/resources/image/analysis.png" />" height=100 />
		<h2>ANALYZING DATA</h2>
		<h3>Start analysis using available data.</h3>
	</div>
	<div class="col-sm-4 box-info" id="aboutUs">
		<img src="<c:url value="/resources/image/about.png"/>"  height=100 />
		<h2>ABOUT US</h2>
		<h3>About ours team and ours dream.</h3>
	</div>
</div>
</div>