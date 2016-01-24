<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>View all of topics</title>
  <link rel="shortcut icon"  href="<c:url value="/resources/image/favicon.ico" />"  type='image/vnd.microsoft.icon'/>
<script src="<c:url value="/resources/js/jquery.js" />"></script>
<script src="<c:url value="/resources/js/d3.js" />"></script>
<script src="<c:url value="/resources/js/d3.layout.cloud.js" />"></script>
<script src="http://cdn.jsdelivr.net/qtip2/2.2.1/jquery.qtip.js"></script>
<!-- Latest compiled JavaScript -->
<script
	src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<script
	src="<c:url value="/resources/js/viewTopics.js" />"></script>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="<c:url value="/resources/css/bootstrap.min.css" />">
<link rel="stylesheet" href="http://qtip2.com/v/stable/jquery.qtip.css">
<link rel="stylesheet"
	href="<c:url value="/resources/css/navigation.css" />">

<link href='https://fonts.googleapis.com/css?family=Roboto+Slab:400,700&subset=latin,vietnamese,latin-ext' rel='stylesheet' type='text/css'>

<style type="text/css">
#display {
	margin: 0 auto;
	text-align: center;
	background: red;
	background: #FFF none repeat scroll 0% 0%;
	padding-top: 10px;
	box-shadow: 0px 0px 3px #6D98A7;
	margin-bottom: 15px;
}
body {
	background: #F5F5F5 none repeat scroll 0% 0%;
}

.onLine {
	float: left;
}

.breakLine {
	clear: both;
}

svg {
	transition:0.5s;
}

svg:hover {
	background-color: wheat;
	cursor: pointer;
	transition:0.5s;
	border-radius: 200px;
} 

.container h2{
  font-size: 31px;
  font-weight: 400;
  color: rgba(0, 39, 152, 0.7);
  text-align: center;
}
th, td {
    padding: 2px;
}
#searchBox {
	border: 1px solid rgb(46, 152, 255);
	padding: 5px;
	border-radius: 10px;
	box-shadow: 1px 1px 5px #ddd;
	width: 30%;
	margin-bottom: 15px;
}
</style>

</head>
<body>
<jsp:include page="navigation.jsp" ></jsp:include>
	<div class="container">
		<h2>Hottest topics of ${pageName} from ${dateFrom} to ${dateTo}</h2>
		<div align="right">
			<input type="text" placeholder="Search key words" id="searchBox" onkeypress="filterTopic(this.value);"/>
		</div>

		<div id="display"></div>
		<input type="hidden" id="pageID" value="${pageID }">
		
		<div id="table"> </div>
	</div>
</body>
</html>