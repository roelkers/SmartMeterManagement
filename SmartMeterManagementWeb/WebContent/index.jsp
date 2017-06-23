<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ page import="com.google.gson.Gson" %>
<%@ page import="de.tub.as.smm.models.SmartMeter" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Smart Meter Management</title>
<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
	<input id="username" type="text" placeholder="Enter a User Name"><button id="login">Log In User</button>
	<hr>
	<!-- Navigation -->
	<div id="main">
		<nav>
			<ul>
				<li id="home">Overview</li>
				<li id="new-meter">Add Smart Meter</li>
			</ul>
		</nav>
		<!-- Ansichten (Übersicht & Detail) -->
		<main>
			<h1> SmartMeter Web App</h1>
			<article id="content">
			</article>
		</main>
	</div>
</body>
<script src="jquery-3.2.1.min.js"></script>
<script src="script.js"></script>
</body>
</html>