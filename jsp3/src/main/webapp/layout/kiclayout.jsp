<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<%--/jsp3/src/main/webapp/layout/kiclayout.jsp --%>
<!DOCTYPE html>
<html lang="en">
<head>
<title><sitemesh:write property='title'/></title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<sitemesh:write property='head'/>
</head>
<style>
#center {
	text-align: center;
}

#half {
	width: 45%;
}
</style>
<body>

	<nav class="navbar navbar-expand-sm bg-light navbar-light">
		<!-- Brand -->
		<a class="navbar-brand" href="#">
		<img src="${path}/image/logo.png" width="80%"></a>

		<!-- Links -->
		<ul class="navbar-nav ">
		  <c:if test="${empty sessionScope.login }">
			<li class="nav-item"><a class="nav-link" 
				href="${path}/member/joinForm">회원가입</a></li>
			<li class="nav-item"><a class="nav-link" 
				href="${path}/member/loginForm">로그인</a></li>
		 </c:if>
		 
		 <c:if test="${!empty sessionScope.login }">
			<li class="nav-item"><a class="nav-link" 
				href="${path}/member/main">${sessionScope.login}님</a></li>
			<li class="nav-item"><a class="nav-link" 
				href="${path}/member/logout">로그아웃</a></li>
		 </c:if>
		 
		 
			<!-- Dropdown -->
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" href="#" id="navbardrop"
				data-toggle="dropdown"> 게시판 </a>
				<div class="dropdown-menu">
				<a class="dropdown-item" 
					href="${path}/board/list?boardid=1">공지사항</a> 
				<a class="dropdown-item" 
					href="${path}/board/list?boardid=2">자유게시판</a> 
				<a class="dropdown-item" 
					href="${path}/board/list?boardid=3">QnA</a>
				</div></li>
			
		</ul>
	</nav>
	<hr>
	<sitemesh:write property="body" />
	<hr>

	<div class="jumbotron text-center" style="margin-bottom: 0">
		<p>Footer</p>
<%-- 시군구 선택 --%>
<br>
<span id="si">
	<select name="si" onchange="getText('si')"> <%--onchange : 값이 바뀌면 getText()함수 실행 --%>
		<option value="">시도를 선택하세요</option>
	</select>
</span>
<span id="gu">
	<select name="gu" onchange="getText('gu')">
		<option value="">구군을 선택하세요</option>
	</select>
</span>
<span id="dong">
	<select name="dong">
		<option value="">동리를 선택하세요</option>
	</select>
</span>
</div>
<script type="text/javascript"
src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"> //jquery ajax사용위해 필수
</script>
<script type="text/javascript">
let divid
let si

//window.onload = $(fucntion) 윈도우가 준비가 완료되면..!
$(function(){
	$.ajax({ //select 서버 갔다와.
		url : "${path}/select",  //${path} : 프로젝트이름 , select servlet요청됨
		success : function(data) {
			//data : 시,도 목록을 저장하고있는 데이터
			//[서울특별시,부산광역시, ... 제주특별자치도]
			console.log(data)
			
			//arr : 시도 목록을 배열로 저장
			let arr = data.substring
			(data.indexOf('[')+1, data.indexOf(']')).split(",") //[+1 서부터 ]까지 문자열로받고 ,로 분류
			
			//$.each : ajax함수 jquery 반복문
			//i : 인덱스값
			//item : 해당요소의 값.
			$.each(arr,function(i,item){
				//append: 태그를 추가
				$("select[name='si']").append(function(){
					return "<option>"+item +"</option>"
				})
			})
		}
	})
})
function getText(name){
	//name : si => 두번째 select 값 설정
	//name : gu => 세번째 select 값 설정
	let disname
	if(name=='si'){ //시 선택했을때
		
		//$("select[name='si']") : select 태그 중 name='si'인 태그 선택
		//val() : value 속성값을 의미
		//$("select[name='si']").val(); : select 태그의 선택된 값을 의미 ex)서울특별시
		params = "si=" + $("select[name='si']").val(); //파라미터 설정
		disname = "gu" // 두번째 select 태그 선택
		firstname = '구군을'
	} else if(name=='gu'){
		params = "si=" + $("select[name='si']").val()
				+"&gu="+ $("select[name='gu']").val();
		disname = "dong" //세번째 select 태그 선택
			firstname = '동리를'
	}
	$.ajax({
		url: "${path}/select",
		type: "POST",
		data: params, //파라미터값
		success : function(data) {
			let arr = data.substring
			(data.indexOf('[')+1,data.indexOf(']')).split(",")
			
			//출력할 select 태그를 초기화
			$("select[name='"+disname+"']").html
			("<option value=''>"+firstname+" 선택하세요</option>")
			
			
			$.each(arr,function(i,item){
			$("select[name='"+disname+"']").append(function(){
				return "<option>" + item + "</option>"
			})
			})
			
		}
	})
}
</script>
</body>
</html>

