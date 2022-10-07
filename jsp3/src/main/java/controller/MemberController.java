package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kic.mskim.MskimRequestMapping;
import kic.mskim.RequestMapping;
//실행후 http://localhost:8088/jsp3/member/joinForm 했을때 화면정상출력되면 정상
/*
 * @WebServlet("/member/*") : url의 정보가
 * 	http://localhost:8088/jsp3/member/....
 * 	MemberController를 호출
 * 
 */
@WebServlet("/member/*")
public class MemberController extends MskimRequestMapping{
	//http://localhost:8088/jsp3/member/joinForm 요청시 호출되는 메서드
	@RequestMapping("joinForm")
	public String joinForm(HttpServletRequest request, HttpServletResponse response) {
		return "/view/member/joinForm.jsp"; //joinForm.jsp로 forward 됨 
	}
	@RequestMapping("loginForm")
	public String loginForm(HttpServletRequest request, HttpServletResponse response) {
		return "/view/member/loginForm.jsp"; //loginForm.jsp로 forward 됨 
	}

}
