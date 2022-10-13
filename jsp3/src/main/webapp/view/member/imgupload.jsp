<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--/jsp3/src/main/webapp/view/member/imgupload.jsp--%>

<script>
	//opener 페이지(윈도우)에 id="pic"인 태그선택
	img = opener.document.querySelector("#pic"); <%--아이디속성이 pic인(jpinForm.jsp) --%>
	img.src ="../picture/${filename}";
	
	//파라미터에 파일이름 설정=>db에 저장
	opener.document.f.picture.value="${filename}"; 
	self.close()
</script>