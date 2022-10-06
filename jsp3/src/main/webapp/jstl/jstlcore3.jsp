<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--/jsp3/src/main/webapp/jstl/jstlcore3.jsp --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL core 태그 : forEach 반복관련 태그</title>
</head>
<body>
<h3>forEach 태그를 이용하여 1부터 100까지의 합 구하기</h3>
<c:set var="sum" value="${0}" /> <%--pageContext.setAttribute("sum",0)과 같은의미 --%>
<c:forEach var="i" begin="1" end="100">  <%-- i가 1부터 100까지 반복문 --%>
<c:set var="sum" value="${sum+i}" />
</c:forEach>
1부터 100까지의 합 :${sum}
<hr>
<h3>1. forEach 태그를 이용하여 1부터 100까지의 짝수 합 구하기</h3>
<c:set var="sum" value="${0}" /> 
<c:forEach var="i" begin="1" end="100"> 
	<c:if test="${i%2==0}">
	<c:set var="sum" value="${sum+i}" />
	</c:if>
</c:forEach>
1부터 100까지의 짝수합 : ${sum}
<hr>
2.1부터 100까지의 짝수의 합
<c:set var="sum" value="${0 }"/>
<c:forEach var="i" begin="2" end="100" step="2">
	<c:set var="sum" value="${sum+i}" />
</c:forEach>
1부터 100까지의 짝수의 합: ${sum}<br>

<h3>1.1부터 100까지의 홀수의 합 구하기</h3>
<c:set var="sum" value="${0}" /> 
<c:forEach var="i" begin="1" end="100"> 
	<c:if test="${i%2!=0}">
	<c:set var="sum" value="${sum+i}" />
	</c:if>
</c:forEach>
1부터 100까지의 홀수의 합: ${sum}<br>
<hr>

<h3>2.1부터 100까지의 홀수의 합 구하기</h3>
<c:set var="sum" value="${0}" /> 
<c:forEach var="i" begin="1" end="100" step="2"> 
	<c:set var="sum" value="${sum+i}" />
</c:forEach>
1부터 100까지의 홀수의 합: ${sum}<br> <%-- empty tag : 하위태그없는태그 --%>
<hr>

<%
	List<Integer> list = new ArrayList<>();
	for(int i=1; i<=10; i++) list.add(i*10);
	pageContext.setAttribute("list",list);
%>
<h3>forEach 태그를 이용하여 List 객체 출력하기</h3>
<%-- items=${list} : list이름을 가진 속성의 값 : 10,20,30....100
 list=10,20,30....100
 
--%>
<c:forEach var="i" items="${list}">
	${i} &nbsp;&nbsp;&nbsp;
</c:forEach>
<h3>List 객체 요소의 합을 출력하기</h3>

<c:set var="sum" value="${0}"/>
<c:forEach var="i" items="${list}">
	<c:set var="sum" value="${sum+i}" />
</c:forEach>
list 객체의 요소의 합: ${sum}
</body>
</html>