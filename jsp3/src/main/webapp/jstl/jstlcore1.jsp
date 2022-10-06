<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--/jsp3/src/main/webapp/jstl/jstlcore1.jsp 
 taglib 는 uri를 찾아서 c라는 태그를 정의한것. uri= c.tld라는 파일을찾음(경로)
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL core 태그</title>
</head>
<body>
<h3>속성 관련 태그 : set, remove, out 태그</h3>
<%--
	session.setAttribute("test","Hello JSTL"); 과 같은 기능
	scope="session"이 생략되는경우 pageContext 객체가됨.
	
	test라는 변수에 Hello JSTL 값을 넣고  session객체에 넣은것. 
 --%>
 
 <%--session.getAttribute("test") 와 같은기능 --%>
<c:set var="test" value="${'Hello JSTL' }" scope="session" /> 
test 속성 :${sessionScope.test }<br>
test 속성 : <c:out value="${test }" /><br>
test 속성 : ${test }><br>

<%--session.removeAttribute("test"); 와 같은기능 --%>
<c:remove var="test" />
test 속성 :${sessionScope.test }<br>
test 속성 : <c:out value="${test }" /><br>
test 속성 : ${test}<br>
</body>
</html>