<%@page import="model.MemberDao"%>
<%@page import="model.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%--/jsp3/src/main/webapp/view/memeber/join.jsp 
    
    
    --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%--true일때 실행되는 구문 --%>
<table> 
<%--${mem.id} : mem 객체의 get프로퍼티의 이름 
	${객체이름.프로퍼티명}
--%>
	<tr><td>아이디</td><td>${mem.id}</td></tr>
	<tr><td>이름</td><td>${mem.name}</td></tr>
	<tr><td>성별</td><td>${(mem.gender==1)?"남":"여"}</td></tr>
	<tr><td>전화번호</td><td>${mem.tel}</td></tr>
	<tr><td>이메일</td><td>${mem.email}</td></tr>
</table>
<input type="button" value="로그인하기" 
onclick="location.href='loginForm'">
</body>
</html>
