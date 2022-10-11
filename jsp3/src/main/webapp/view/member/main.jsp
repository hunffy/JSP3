<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%--/jsp3/src/main/webapp/view/member/main.jsp--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원관리</title>
</head>
<body>
<h3>${sessionScope.login}로 로그인 되었습니다.</h3> <%--${login}으로 해도되지만 login정보는 중요하기때문에 ${sessionScope.login}사용 --%>
<h3><a href="logout">로그아웃</a></h3>
<h3><a href="info?id=${sessionScope.login}">회원정보보기</a></h3> <%-- id= <%=login%> 아이디입력값 --%>
<c:if test="${sessionScope.login == 'admin'}">
<h3><a href="list">회원목록보기</a></h3>
</c:if>
</body>
</html> 

