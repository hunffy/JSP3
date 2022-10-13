<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--/jsp3/src/main/webapp/view/board/writeForm.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성하기</title>
</head>
<body>
<div class="container">
<form name="f" action="write" enctype="multipart/form-data" 
	method="post">                                                <%--enctype="multipart/form-data" : 파일업로드때문에 --%>
	<h2 id="center">게시판 입력</h2>
	<div class="form-group">
	 <label>작성자:</label>
		<input type="text" class="form-control" name="writer" >  <%--form-control은 kiclayout에 맞게하려고 사용 --%>
	 <label>비밀번호:</label>
		<input type="password" class="form-control" name="pass">
	 <label>제목:</label>
		<input type="text" class="form-control" name="subject">
	</div>
	<div class="form-group">
	 <label>내용</label>
		<textarea rows="10" cols="50" class="form-control" 
		name="content"></textarea>
	</div>
	<div class="form-group">
	 <label>파일저장:</label>
		<input type="file" class="form-control" name="file1">
	</div>
	<div id="center" style="padding:3px;">
		<button type="submit" class="btn btn-dark">입력</button>
	</div>
	</form></div></body></html>