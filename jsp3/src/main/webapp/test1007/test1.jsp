<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%-- src/main/webapp/test0413/test2.jsp --%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>입력된 수까지의 합 구하기</title>
<link rel="stylesheet" href="../css/main.css">
</head>
<body>
<form method="post">
  숫자:<input type="text" name="num" value="${param.num}" >
   <input type="submit" value="숫자까지의 합 구하기">  
</form>
<c:set var="sum" value="${0}" />
<c:forEach var="i" begin="1" end="${param.num}">
<c:set var="sum" value="${sum+i}" />
</c:forEach><br>
합계: ${sum} <br>


<h3>if 태그를 이용하여 합계가 짝수인지홀수 출력하기</h3>
<c:set var="sum1" value="${0}" /> 
<c:forEach var="i" begin="1" end="${param.num }"> 
	<c:if test="${i%2==0}">
	<c:set var="sum1" value="${sum1+i}" />
	</c:if>
</c:forEach>


짝수의합 : ${sum1}<br>
<h3>choose 태그를 이용하여 합계가 짝수인지홀수 출력하기</h3>
<c:choose>
	<c:when test="${sum%2==0}">
		${sum}은 짝수입니다.<br></c:when>
	<c:when test="${sum%2==1}">
		${sum}은 홀수입니다.<br></c:when>
	<c:otherwise>${sum}은 0입니다 <br></c:otherwise>
</c:choose>
</body>
</html>	