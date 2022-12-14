<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="model.Member"%>
<%@page import="model.MemberDao"%>
    <%--/jsp3/src/main/webapp/view/member/joinForm.jsp --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="../css/main.css">
<script type="text/javascript">
function check(f) {
	if(f.id.value == ''){
		alert("아이디를 입력하세요")
		f.id.focus()
		return false	//submit 버튼클릭했을때 join.jsp로 제어가 넘어가지않기위해 false사용 -> submit 버튼클릭의 기능 중지
	}
	if(f.pass.value == ''){
		alert("비밀번호를 입력하세요")
		f.pass.focus()
		return false         
	}
	if(f.name.value == ''){
		alert("이름을 입력하세요")
		f.name.focus()
		return false
	}
}

function win_open() {
	let op = "width=300, height=300, left=50, top=150"
	open("memberimg","",op)
}
function idchk(){
	if(document.f.id.value ==''){ //아이디를 입력하지않은경우 
		alert("아이디를 입력하세요")
		f.id.focus()
	}else{ //아이디를 입력한경우 새창을오픈 idchk(MemberController로 이동)
		let op = "width=500, height=200, left=50, top=150"
		open("idchk?id="+document.f.id.value, "",op)
	}
}
</script> 
</head>
<body>
<form action="join" method="post" name='f'
	onsubmit="return check(this)"> <%--this : form  check(this) 아이디 비번 이름 등록됐는지 확인--%>
	
	<%-- type="hidden" : 값은존재하지만 화면에는 나타나지않음. 파라미터값으로 서버에 전달 --%> 
<input type="hidden" name="picture" value="">
<table>
	<caption>회원 가입</caption>
	<tr><td rowspan="4" align="center" valign="bottom">
		<img src="" width="100" height="120" id="pic"><br>
		<font size="1"><a href="javascript:win_open()">사진등록</a>
		</font></td>
	<td>아이디</td>
		<td><input type="text" name="id" style="width:70%;">
		<input type="button" value="중복검색" onclick="idchk()"></td>
	</tr>
	<tr><td>비밀번호</td>
  <td><input type="password" name="pass"></td></tr>
  
  	<tr><td>이름</td>
  	<td><input type="text" name="name"></td></tr>
  	
  	<tr><td>성별</td>
  	<td><input type="radio" name="gender" value="1" checked>남
  	<input type="radio" name="gender" value="2">여</td></tr>
  	
   <tr><td>전화번호</td>
<td colspan="2"><input type="text" name="tel"></td></tr>
	<tr><td>이메일</td>
<td colspan="2"><input type="text" name="email"></td></tr>

	<tr><td colspan="3" align="center">
	<input type="submit" value="회원가입">
	
	<%--type="reset" : 화면을 초기화면으로 설정. 입력된 내용 전부 초기화 --%>
	<input type="reset" value="다시작성">
	</td></tr>
</table>
</form>	
</body>
</html>