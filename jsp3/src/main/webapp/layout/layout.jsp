<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE html><html>
<head>
<meta charset="UTF-8">
<title><sitemesh:write property='title'/></title>
<link rel="stylesheet" href="${path}/css/main.css">
<sitemesh:write property='head'/>
</head><body>
<table><tr><td colspan="3" style="text-align:right">
<c:if test="${empty sessionScope.login}">
	<a href="${path}/member/loginForm">로그인</a>
	<a href="${path}/member/joinForm">회원가입</a>
</c:if>
<c:if test="${!empty sessionScope.login }">
	${login}님. <a href="${path}/member/logout">로그아웃</a>
</c:if>
</td></tr>
<tr><td width="15%" valign="top">
	<a href="${path}/member/main">회원관리</a><br>
	<a href="${path}/board/list?boardid=1">공지사항</a><br>
	<a href="${path}/board/list?boardid=2">자유게시판</a><br>
	<a href="${path}/board/list?boardid=3">QnA</a><br>
</td><td colspan="2" style="text-align: left; vertical-align: top">
<sitemesh:write property="body" /></td></tr>
<tr><td colspan="3">KIC캠퍼스</td></tr></table></body></html>