<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib  prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="path" value="pageContext.request.contextPath" />
<%--/jsp3/src/main/webapp/view/board/list.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>
<script type="text/javascript">
	function listsubmit(page) {
		f = document.sf;
		f.pageNum.value=page;
		f.submit();
	}
</script>
</head>
<body>
<!-- table list start -->
	<div class="container">
	<div  class="container justify-content-end" >
		<form class="form-inline" action="list?boardid=${boardid}"
			method="post" name="sf">
			<input type="hidden" name="pageNum" value="1">
		<select class="form-control" name="column">
			<option value="writer">글쓴이</option>
			<option value="subject">제목</option>
			<option value="content">내용</option>
		</select>
		<script type="text/javascript">
			if('${param.column}' != '')
				document.sf.column.value = '${param.column}'
		</script>
			<input class="form-control mr-sm-2" type="text"
				placeholder="Search" name="find" value="${param.find}">
			<button class="btn btn-success" type="submit">Search</button>
		</form>
		</div>
		<h2  id="center">${boardName}</h2>
		<p align="right">
		 <c:if test="${boardcount >0}">글개수:${boardcount}</c:if>
		 <c:if test="${boardcount == 0}">등록된 게시물이 없습니다.</c:if>
		<table class="table table-hover">
			<thead>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>작성자</th>
					<th>등록일</th>
					<th>파일</th>
					<th>조회수</th>

				</tr>
			</thead>
			<tbody>
			<c:forEach var="b" items="${list}">
				<tr>
					<td>${boardnum}</td>
					<c:set var="boardnum" value="${boardnum -1}"/>
					<td>
					<c:if test="${b.grplevel > 0}">
					<img src="../imgage/level.gif" 
						width="${20*(b.grplevel-1)}">
					<img src="../image/re.gif">
					</c:if>
				<a href="info?num=${b.num}">${b.subject}</a>
				</td> <%--게시글제목 클릭할때 실행 --%>
					<td>${b.writer}</td>
					
<%-- 오늘 등록한 게시물 : HH:mm:ss
	 이전일에 등록한 게시물 : yyyy-MM-dd HH:mm 출력하기
 --%>
					<td><fmt:formatDate value="${today}" pattern="yyyyMMdd" var="t"/>
						<fmt:formatDate value="${b.regdate}" pattern="yyyyMMdd" var="r"/>
						<c:if test="${t == r}">
							<fmt:formatDate value="${b.regdate}" pattern="HH:mm:ss"/>
						</c:if>
						<c:if test="${t != r}">
							<fmt:formatDate value="${b.regdate}" pattern="yyyy-MM-dd HH:mm"/>
						</c:if>
					</td>
					<td><a href="../upload/${b.file1}">${b.file1}</a></td>
					<td>${b.readcnt}</td>  <%--조회수 --%>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
<%--공지사항일 경우 관리자만 게시글입력 --%>
<c:if
	test="${(param.boardid != 1) || (sessionScope.login == 'admin') }">  
		<p align="right"><a href="writeForm">게시글입력</a></p>
</c:if>

<%-- 페이징 부분 --%>
		<div class="container"  >
		<ul class="pagination justify-content-center"  >
   <li class="page-item 
  	 <c:if test='${startPage <= bottomLine}'>disabled</c:if>">
   <%--<a class="page-link" href="list?pageNum=${startPage-bottomLine}">--%>
   <a class="page-link" href="javascript:listsubmit(${startPage-bottomLine})">
   Previous</a></li>  <%-- 페이지 넘기는부분  --%>
   
  
   <c:forEach var="i" begin="${startPage}" end="${endPage}">
  <li class="page-item <c:if test='${i==pageInt}'> active</c:if>">
 <%-- <a class="page-link" href="list?pageNum=${i}">${i}</a> --%>
 	<a class="page-link" href="javascript:listsubmit(${i})">${i}</a>
  </li></c:forEach>
  <li class="page-item
  	<c:if test='${endPage >= maxPage}'>disabled</c:if>">
  <%-- <a class="page-link" href="list?pageNum=${startPage+bottomLine}"> --%>
  <a class="page-link" href="javascript:listsubmit(${startPage+bottomLine})">
  Next</a></li>
</ul> </div>
	</div>
	<!-- table list end -->
</body>
</html>