<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--/jsp2/src/main/webapp/layout/layout.jsp 
	1.http://localhost:8088/jsp2/model1/member/loginForm.jsp 요청
		/* 에 속한 url 요청
	2.<sitemesh:write property='title'/> : 
		loginForm.jsp 페이지의 title태그의 내용을 적용
		
	3.<sitemesh:write property='head'/> :
		loginForm.jsp 페이지의 head태그의 내용을 적용. title 태그 제외
		<script>,<style>...등 내용을 안에다가 대입
		
	4.<sitemesh:write property="body" /> :
		loginForm.jsp 페이지의 body태그의 내용을 적용.
--%>
<% String path = request.getContextPath(); // /jsp2프로젝트 이름을 가져옴.
   String login = (String)session.getAttribute("login");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><sitemesh:write property='title'/></title>
<link rel="stylesheet" href="<%=path%>/css/main.css">
<sitemesh:write property='head'/>
</head><body>
<table><tr><td colspan="3" style="text-align:right">
<% if(login == null){%>
	<a href="<%=path%>/model1/member/loginForm.jsp">로그인</a>
	<a href="<%=path%>/model1/member/joinForm.jsp">회원가입</a>
<%}else{ %>
	<%=login %>님.
	<a href="<%=path%>/model1/member/logout.jsp">로그아웃</a>
<%} %>
</td></tr>
<tr><td width="15%" valign="top">
	<a href="<%=path%>/model1/member/main.jsp">회원관리</a><br>
</td><td colspan="2" style="text-align: left; vertical-align: top">
<sitemesh:write property="body" /></td></tr>
<tr><td colspan="3">KIC캠퍼스</td></tr></table></body></html>