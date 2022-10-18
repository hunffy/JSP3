package controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import kic.mskim.MskimRequestMapping;
import kic.mskim.RequestMapping;
import model.Member;
import model.MemberMybatisDao;
//실행후 http://localhost:8088/jsp3/member/joinForm 했을때 화면정상출력되면 정상
/*
 * @WebServlet("/member/*") : url의 정보가
 * 	http://localhost:8088/jsp3/member/....
 * 	MemberController를 호출
 * 
 * CRUD: Create, Read, Update, Delete
 */
@WebServlet("/member/*")
public class MemberController extends MskimRequestMapping{
	private MemberMybatisDao dao = new MemberMybatisDao();
	//http://localhost:8088/jsp3/member/joinForm 요청시 호출되는 메서드
	@RequestMapping("joinForm")
	public String joinForm(HttpServletRequest request, HttpServletResponse response) {
		return "/view/member/joinForm.jsp"; //joinForm.jsp로 forward 됨 
	}
	
	@RequestMapping("join")
	/*	1.파라미터값들을 Member 객체에 저장
	 * 	2.Member 객체의 내용을 db에 저장
	 *  3. 	저장성공 : 화면에 내용출력하기
	 *  	저장실패 : joinForm.jsp 페이지이동
	 */
	public String join(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//1. 파라미터값들을 Member 객체에 저장
		Member mem = new Member();
		mem.setId(request.getParameter("id"));
		mem.setPass(request.getParameter("pass"));
		mem.setName(request.getParameter("name"));
		mem.setGender(Integer.parseInt(request.getParameter("gender")));
		mem.setTel(request.getParameter("tel"));
		mem.setEmail(request.getParameter("email"));
		mem.setPicture(request.getParameter("picture"));
		
		
		//2.Member 객체의 내용을 db에 저장
		if(dao.insert(mem)) {
			//3.저장성공 : 화면에 내용출력하기
			request.setAttribute("mem", mem);
			return "/view/member/join.jsp";
		}else {
			//3.저장실패 : joinForm.jsp 페이지 이동
			return "/view/member/joinForm.jsp";
		}
	}
 	@RequestMapping("loginForm")
	public String loginForm(HttpServletRequest request, HttpServletResponse response) {
		return "/view/member/loginForm.jsp"; //loginForm.jsp로 forward 됨 
	}
 	@RequestMapping("login")
 	/*
 	 *    1. 아이디, 비밀번호 파라미터를 변수에 저장 
		  2. db 정보를 읽기. id에 해당하는 db정보를 읽어서 Member 객체에 저장 
		     Member MemberDao.selectOne(id);
		  3. 아이디와 비밀번호 검증. 
		    - 아이디가 없는 경우      
		       아이디가 없습니다. 메세지확인. loginForm.jsp 페이지 이동
		    - 아이디 존재. 비밀번호가 틀린경우   
		       비밀번호오류 메세지확인. loginForm.jsp 페이지 이동
		    - 아이디 존재. 비밀번호가 맞는경우 => 정상적인 로그인.
		      session 객체에 로그인 정보 저장.
		      main로 페이지 이동.
 	 */
 	public String login(HttpServletRequest request, HttpServletResponse response) {
 		//1. 아이디,비밀번호 파라미터를 변수에 저장
 		String id = request.getParameter("id");
 		String pass = request.getParameter("pass");
 		
 		//2. db 정보를 읽기. id에 해당하는 db정보를 읽어서 Member 객체에 저장  Member MemberDao.selectOne(id);
 		Member mem = dao.selectOne(id);
 		
 		String msg=null;
 		String url=null;
 		
 		if(mem==null) { //아이디없는경우.
 			msg = "아이디를 확인하세요";
 			url = "loginForm";
 		}else if(!pass.equals(mem.getPass())) { //비밀번호가 틀릴경우  pass:입력한비밀번호  mem.getPass():DB등록된비밀번호
 			msg = "비밀번호가 틀립니다.";
 			url = "loginForm";
 		}else {	//정상적인 로그인상태
 			//session에 login정보 등록. servlet은 session내장객체가 없어서 request.getSession()사용
 			//request.getSession(): session객체 리턴
 			request.getSession().setAttribute("login", id);
 			msg = "반갑습니다."+mem.getName() + "님";
 			url = "main";
 		}
 		request.setAttribute("msg", msg);
 		request.setAttribute("url", url);
 		return "/view/alert.jsp"; //forward 됨
 	}
 	/*
 	 * 1.로그인 여부 검증
 	 * 로그인상태 : 화면보여주기
 	 * 로그아웃상태: 로그인하세요. 메세지 출력 후 loginForm으로 이동
 	 */
 	@RequestMapping("main")
 	public String main(HttpServletRequest request, 
 			HttpServletResponse response) {
 		String login = 
 				(String)request.getSession().getAttribute("login");
 		if(login==null) {	//로그아웃상태
 			request.setAttribute("msg", "로그인 하세요");
 			request.setAttribute("url", "loginForm");
 			return "/view/alert.jsp";
 		}
 		return "/view/member/main.jsp";
 	}
 	@RequestMapping("logout")
 	public String logout(HttpServletRequest request, 
 			HttpServletResponse response) {
 		//1. session에 등록된 로그인 정보제거
 		request.getSession().invalidate();
 		//2.loginForm.jsp로 페이지이동
 		return "redirect:loginForm";
 	}
 	/*
 	 * 	1. id 파라미터값을 변수 저장하기
 	 * 	2. login 상태검증
 	 * 	로그아웃상태: 로그인하세요 메세지출력 후 loginForm 페이지 이동
 	 * 	3. login 상태 검증2
 	 * 	로그인상태: 관리자가 아닌경우 id 파라미터값과 login 정보가 다르면
 	 *			본인정보만 조회가능합니다. 메세지 출력 후 main로 페이지 이동
 	 *	4. id에 해당하는 정보를 읽어서 /view/member/info.jsp출력하기
 	 */
 	@RequestMapping("info")
 	public String info(HttpServletRequest request, 
 			HttpServletResponse response) {
 		
 		//	1. id 파라미터값을 변수 저장하기
 		String id = request.getParameter("id");
 		
 		//2. 로그인정보 로그인객체에 담기
 		String login = (String)request.getSession().getAttribute("login");
 		
 		//3. 로그아웃상태
 		if(login == null) {
 			request.setAttribute("msg", "로그인 하세요");
 			request.setAttribute("url", "loginForm");
 			return "/view/alert.jsp";
 		//3-2 관리자가 아닌경우, 본인정보조회가 아닌경우
 		}else if(!login.equals("admin") && !id.equals(login)) {
 			request.setAttribute("msg", "본인 정보만 조회 가능합니다");
 			request.setAttribute("url", "main");
 			return "/view/alert.jsp";
 		}//4.본인정보조회
 		Member mem = dao.selectOne(id);
 		request.setAttribute("mem", mem);
 		return "/view/member/info.jsp";
 	}
 	
 	@RequestMapping("updateForm")
 	public String updateForm(HttpServletRequest request, 
 			HttpServletResponse response) {
 		
 		//	1. id 파라미터값을 변수 저장하기
 		String id = request.getParameter("id");
 		
 		//2. 로그인정보 로그인객체에 담기
 		String login = (String)request.getSession().getAttribute("login");
 		
 		//3. 로그아웃상태
 		if(login == null) {
 			request.setAttribute("msg", "로그인 하세요");
 			request.setAttribute("url", "loginForm");
 			return "/view/alert.jsp";
 		//3-2 관리자가 아닌경우, 본인아이디수정 아닌경우
 		}else if(!login.equals("admin") && !id.equals(login)) {
 			request.setAttribute("msg", "본인 정보만 수정 가능합니다");
 			request.setAttribute("url", "main");
 			return "/view/alert.jsp";
 		}
 		
 		//4.본인정보수정(정상적인 조회)
 		Member mem = dao.selectOne(id);
 		request.setAttribute("mem", mem);
 		return "/view/member/updateForm.jsp";
 	}
 	/*
 	 * 	1.모든 파라미터 정보를 Member 객체에 저장
 	 * 	
 	 * 	2. 입력된 비밀번호와, db에 저장된 비밀번호 비교 => db읽기
 	 * 	관리자인 경우 관리자 비밀번호로 비교하기.
 	 * 	본인인 경우 본인의 비밀번호로 비교하기.
 	 * 	-비밀번호가 틀린 경우 : "비밀번호 오류" 메세지 출력 updateForm 페이지 이동
 	 * 
 	 * 3.비밀번호가 맞는경우
 	 * 	입력된 내용을 저장하고있는 Memeber객체를 이용하여 db정보 수저
 	 * 	boolean MemberDao.update(Member)
 	 * 	결과가 true면 수정성공 info 페이지 이동
 	 * 	결과가 false면 수정실패 메세지 출력후, updateForm 페이지 이동
 	 * => jsp2프로젝트의 update.jsp 페이지 참조
 	 */
 	@RequestMapping("update")
 	public String update(HttpServletRequest request,
 						 HttpServletResponse response) {
 		
 		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
 		
 		//1.모든 파라미터 정보를 Member 객체에 저장
 		Member mem = new Member();
 		
 		mem.setId(request.getParameter("id"));
 		mem.setPass(request.getParameter("pass"));
 		mem.setName(request.getParameter("name"));
 		mem.setGender(Integer.parseInt(request.getParameter("gender")));
 		mem.setTel(request.getParameter("tel"));
 		mem.setEmail(request.getParameter("email"));
 		mem.setPicture(request.getParameter("picture"));
 		
 		//로그인 정보 가져오기
 		String login = (String)request.getSession().getAttribute("login");
 		
     	Member dbMem = dao.selectOne(login); //dao.selectOne(login) : DB에서 login된 아이디에 대한 모든정보를 가져온다.
     	
     	String msg = "비밀번호가 틀렸습니다";
     	String url = "updateForm?id="+mem.getId();
     	
     	if(mem.getPass().equals(dbMem.getPass())) {
     		if(dao.update(mem)) {
     			msg = "회원 정보수정이 완료되었습니다.";
     			url = "info?id="+mem.getId();
     		}else {
     			msg = "회원 정보 수정시 오류 발생";
     		}
 		}
     	request.setAttribute("msg", msg);
     	request.setAttribute("url", url);
     	return"/view/alert.jsp";
 	}
 	/*
 	 * 	1. id 파라미터 저장하기
 	 * 	2. login 여부 검증하기
 	 * 		로그아웃 상태인 경우
 	 * 		로그인하세요. 메세지 출력 후  loginForm 페이지로 이동
 	 * 		관리자가 아니면서 id 파라미터 정보와 login 정보가 다른 경우
 	 * 		본인만 탈퇴 가능합니다. 메세지  출력 후 main 페이지로 이동
 	 * 	3.deleteForm.jsp 출력하기
 	 */
 	@RequestMapping("deleteForm")
 		public String deleteForm(HttpServletRequest request,
 								 HttpServletResponse response) {
 			String id = request.getParameter("id");
 			String login =(String)request.getSession().getAttribute("login");
 			
 			if(login == null) {
 				request.setAttribute("msg","로그인 하세요");
 				request.setAttribute("url","loginForm");
 				return "/view/alert.jsp";
 			}else if(!login.equals("admin") && !id.equals(login)) {
 				request.setAttribute("msg", "본인만 탈퇴 가능합니다");
 				request.setAttribute("url", "main");
 				return "/view/alert.jsp";
 			}
 			
 			// 3.deleteForm.jsp 출력하기
 			return "/view/member/deleteForm.jsp";
 		}
 	
    @RequestMapping("delete")
    public String delete(HttpServletRequest request,
          HttpServletResponse response) {
    	
       try {
    	   request.setCharacterEncoding("utf-8");
       }catch (UnsupportedEncodingException e) {
    	   e.printStackTrace();
       }
    	
    	
       String id = request.getParameter("id");
       String pass = request.getParameter("pass");
       String login = (String)request.getSession().getAttribute("login");
       
       String msg = null;
       String url = null;
       
       if(!request.getMethod().equals("POST")) {
    	  msg = "입력방식 오류입니다";
    	  url = "deleteForm";
       }else if(id.equals("admin")) { //id:탈퇴대상
          msg= "관리자는 탈퇴 불가능합니다.";
          url= "list";
       }else if(login ==null) {
          msg="로그인하세요";
          url= "loginForm";
       }else if(!login.equals("admin") && !id.equals(login)) {
    	  msg= "본인만 탈퇴 가능합니다.";
    	  url= "main";
          
          
       }else {	//정상적인 탈퇴 가능 상태
          Member mem = dao.selectOne(login);
          
          if(!pass.equals(mem.getPass())) {	//비밀번호 오류
        	  msg = "비밀번호오류";
        	  url= "deleteForm?id="+id;
          }else {//비밀번호 일치
             if(dao.delete(id)) {
            	 msg = "회원탈퇴가 완료되었습니다.";
                if(login.equals("admin")) {
                	url= "list";
                }else {//일반사용자
                   request.getSession().invalidate(); //로그아웃
                   url= "loginForm";
                }
             }else { //회원 정보 삭제시 db오류 발생 한 경우
            	 msg = "회원탈퇴를 실패했습니다.";
                if(login.equals("admin")) { //관리자
                	url= "list";
                }else {
                	url= "deleteForm?id="+id;
                }
             }
          }//비밀번호 일치 if끝
       }
       	 request.setAttribute("msg", msg);
         request.setAttribute("url", url);
         return"/view/alert.jsp";
    }
    
    /*
     * 
   		1. 로그아웃상태 : 로그인이 필요합니다. 메세지 출력 loginForm.jsp 페이지 이동
    	2. 일반사용자로 로그인 상태 : 관리자만 가능한 페이지 입니다. 메세지 출력.
    						main.jsp로 페이지 이동
    						
    	3. DB에서 모든 회원정보를 조회하여 화면에 출력하기
    	List<Member> MemberDao.list() 조회
     */
    @RequestMapping("list")
    public String list(HttpServletRequest request,
    				   HttpServletResponse response) {
    	String login = (String)request.getSession().getAttribute("login");
    	
    	if(login==null) {
    		request.setAttribute("msg", "로그인이 필요합니다");	
    		request.setAttribute("url", "loginForm");
    		return "/view/alert.jsp";
    	}
    	if(!login.equals("admin")) {
    		request.setAttribute("msg", "관리자만 유효한 페이지입니다");
    		request.setAttribute("url", "main");
    		return "/view/alert.jsp";
    	}
    	List<Member> list = dao.list();
    	request.setAttribute("list", list);
    	return "/view/member/list.jsp";
    	
    }
    @RequestMapping("memberimg")
    public String memberimg(HttpServletRequest request,
    						HttpServletResponse response) {
    	return "/view/member/memberimg.jsp";
    }
    /*
     * 	1.파일 업로드하기 : 업로드위치 => /member/picture/로 설정
     * 	2.파일의 내용을 opener에 출력하기. 현재 윈도우는 close함.
     */
    @RequestMapping("imgupload")
    public String imgupload(HttpServletRequest request,
    						HttpServletResponse response) {
    	String path = getServletContext().getRealPath("/")+"/picture/"; //파일업로드 폴더위치
    	
    	File f = new File(path);
    	if(!f.exists()) f.mkdirs(); //f.mkdirs():여러단개의 폴더를 만듬 if(!f.exists()) :폴더가 없으면?
    	String filename = null;
    	MultipartRequest multi = null;
    	try {
    		//request:파일에대한정보,이름,파라미터정보 path:파일업로드할 폴더위치 10*1024*1024:최대업로드크기 utf-8:파라미터 인코딩
			multi = new MultipartRequest //->파일 업로드 완성 , 오류우려가있어서 예외처리
							(request, path, 10*1024*1024, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	filename = multi.getFilesystemName("picture"); //type이 file인 name과 같이 picture사용 파일이름을 선택한파일의 이름으로설정
    	request.setAttribute("filename", filename);
    	return "/view/member/imgupload.jsp";
    }
    @RequestMapping("idForm")
    public String idForm(HttpServletRequest request,
    					 HttpServletResponse response ) {
    	return "/view/member/idForm.jsp";
    }
    @RequestMapping("pwForm")
    public String pwForm(HttpServletRequest request,
    					 HttpServletResponse response ) {
    	return "/view/member/pwForm.jsp";
    }
    /*
     * 	1.email,tel 파라미터 저장.
		2.db에서 email과 tel을 이용하여 id값을 리턴
			id = MemberDao.idSearch(email,tel)
		3.id값이 존재
			id값을 화면에 출력.
			opener의 id에 값을 저장. 현재 화면닫기
		4. id값이 없는 경우
			정보에 맞는 id를 찾을 수 없습니다. 메세지 출력후
			idForm.jsp 페이지로 이동
     */
   @RequestMapping("id") //idForm에서 id요청(email,tel파라미터를 가지고옴)
   public String id(HttpServletRequest request,
		   			HttpServletResponse response) {
	   String email = request.getParameter("email");
	   String tel = request.getParameter("tel");
	   String id = dao.idSearch(email, tel); //email과 tel 정보로 id검색
	   
	   //아이디가 없는경우
	   if(id==null) {
		   request.setAttribute("msg", "아이디가 존재하지않습니다.");
		   request.setAttribute("url", "idForm");
		   return "/view/alert.jsp";
	   }
	   
	 //이메일과 전화번호가 일치하는 id 찾은경우
	   request.setAttribute("id", id);
	   return "/view/member/id.jsp";
   }
   /*
    * 1. 로그아웃상태인 경우. 로그인 하세요. 메세지 출력
        opener 페이지를 loginForm.jsp 페이지 이동.
           현재페이지 닫기.
     2. pass,chgpass 파라미터 값 저장
     
     3. pass 비밀번호가 db에 저장된 비밀번호와 틀리면
        비밀번호 오류 메세지 출력. passwordForm.jsp 페이지 이동
        
     4. pass 비밀번호가 db에 저장된 비밀번호와 같으면 =>비밀번호 검증완료
        MemberDao.updatePass(login.chgpass) => 새로운 비밀번호로 수정
        비밀번호 수정 성공.
        -메세지 출력후 opener 페이지를 info.jsp 페이지 이동. 
          현재페이지 닫기
        
         비밀번호 수정 실패.
        -메세지 출력후 opener 페이지를 updateForm.jsp페이지 이동.
         현재페이지 닫기.
    */
   @RequestMapping("pw")
   public String pw(HttpServletRequest request,
		   			HttpServletResponse response) {
	  String id = request.getParameter("id");
	  String email = request.getParameter("email");
	  String tel = request.getParameter("tel");
	  
	  String pass= dao.pwSearch(id, email, tel);
	  if(pass==null) {
		  request.setAttribute("msg", "정보에 맞는 비밀번호를 찾을 수 없습니다");
		  request.setAttribute("url", "pwForm");
		  return "/view/alert.jsp";
	  }
	  //비밀번호를 조회한 경우
	  request.setAttribute("pass", pass);
	  return "/view/member/pw.jsp";
   }
   @RequestMapping("passwordForm")
   public String passwordForm(HttpServletRequest request, 
		   					  HttpServletResponse responser) {
	   return "/view/member/passwordForm.jsp";
   }
   
   /* 1. 로그아웃상태인 경우. 로그인 하세요. 메세지 출력
        opener 페이지를 loginForm.jsp 페이지 이동.
           현재페이지 닫기.
     2. pass,chgpass 파라미터 값 저장
     
     3. pass 비밀번호가 db에 저장된 비밀번호와 틀리면
        비밀번호 오류 메세지 출력. passwordForm.jsp 페이지 이동
        
     4. pass 비밀번호가 db에 저장된 비밀번호와 같으면 =>비밀번호 검증완료
        MemberDao.updatePass(login.chgpass) => 새로운 비밀번호로 수정
        비밀번호 수정 성공.
        -메세지 출력후 opener 페이지를 info 페이지 이동. 
          현재페이지 닫기
        
         비밀번호 수정 실패.
        -메세지 출력후 opener 페이지를 updateForm페이지 이동.
         현재페이지 닫기.
    * 
    */
	
   @RequestMapping("password")
   public String password(HttpServletRequest request,
		   				  HttpServletResponse response) {
	   String login = (String)request.getSession().getAttribute("login");
	   
	   boolean opener = true;
	   boolean closer = true;
	   String msg = null;
	   String url = null;
	   if(login == null) {
		   msg = "로그인 하세요";
		   url = "loginForm";
	  }else {
		  String pass = request.getParameter("pass");
		  String chgpass = request.getParameter("chgpass");
		  Member mem = dao.selectOne(login);
		  if(pass.equals(mem.getPass())) {//비밀번호일치
			  if(dao.updatePass(login, chgpass)) {//변경성공
				  msg = "비밀번호가 변경되었습니다.";
				  url = "info?id="+login;
			  }else {//변경실패
				  msg = "비밀번호 변경시 오류가 발생햇습니다.";
				  url = "updateForm?id="+login;
			  }
		  }else {//비밀번호 오류. 기존비밀번호와 등록된 비밀번호가 틀린경우
			  msg = "비밀전호가 틀렸습니다.";
			  closer=false; opener = false;
			  url = "passwordForm";
		  }
	  }
	   request.setAttribute("msg", msg);
	   request.setAttribute("url", url);
	   request.setAttribute("opener", opener);
	   request.setAttribute("closer", closer);
	   return "/view/member/password.jsp";
   }
   @RequestMapping("idchk")
   public String idchk(HttpServletRequest request,
		   			   HttpServletResponse response) {
	   String id = request.getParameter("id"); // 입력한id값을 id에 대입
	   Member mem = dao.selectOne(id);// mem함수에 db에있는 id와 입력한"id"대조
	   
	   if(mem == null) { //만약 DB에 모든id중에 값이 없다면
		   request.setAttribute("msg", "사용가능한 아이디입니다.");
		   request.setAttribute("able", true);
	   }else { //DB에 이미 아이디가 존재한다면
		   request.setAttribute("msg", "사용 중인 아이디입니다.");
		   request.setAttribute("able", false);
	   }
	   return "/view/member/idchk.jsp"; //새창의 화면을 띄워주기위해 idchk.jsp로 이동
   }
   
}

 

 	

