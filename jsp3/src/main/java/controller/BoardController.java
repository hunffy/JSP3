package controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import kic.mskim.MskimRequestMapping;
import kic.mskim.RequestMapping;
import model.Board;
import model.BoardDao;

//http://localhost:8088/jsp3/board/* : board 뒤에 누가오면 board호출
@WebServlet("/board/*")//브라우저에서 /board/RequestMapping("") ""부분 입력되면
public class BoardController extends MskimRequestMapping {
	private BoardDao dao = new BoardDao();
	//http://localhost:8088/jsp3/board/writeForm
	@RequestMapping("writeForm") //
	public String writeForm(HttpServletRequest request,
							HttpServletResponse response) {
		String boardid = (String) request.getSession().getAttribute("boardid");
		/*
		 * boardid =1 인경우(공지사항) 관리자가 아니면 게시글등록 못하도록 수정
		 */
		if(boardid == null) boardid="1";
		String login = (String)request.getSession().getAttribute("login");
		
		if(boardid.equals("1")) { //boardid가 1(공지사항)일때
			if(login==null || !login.equals("admin")) { //로그아웃상태이거나 관리자가아닌경우
				request.setAttribute("msg", "관리자만 작성 가능합니다");
				request.setAttribute("url", request.getContextPath()+"/board/list?boardid="+boardid);
				return "/view/alert.jsp";
			}
			
		}
		return "/view/board/writeForm.jsp";
	}
	@RequestMapping("write")
	public String write(HttpServletRequest request,
						HttpServletResponse response) {
		
		//path: 파일 업로드되는 폴더 위치를 설정
		String path = getServletContext().getRealPath("/")+"/upload/"; //파일업로드되는 위치를 upload밑으로
		
		
		String ip = request.getRemoteAddr();
		System.out.println(ip);
		File f = new File(path); //파일에 폴더위치 
		
		if(!f.exists()) f.mkdirs(); //업로드되는 폴더가없으면 폴더생성
		int size=10*1024*1024; //업로드할 파일의 크기 지정 10메가
		
		MultipartRequest multi = null; 
		try {
			/*
			 *  request : 요청정보. 파라미터,파일이름,파일의 내용을 저장
			 *  path	: 파일을 저장할 위치(업로드 파일의 폴더위치 지정)
			 *	size    : 업로드 가능한 최대 파일의 크기
			 *	UTF-8	: 파라미터값 인코딩
			 */
			multi = new MultipartRequest(request,path,size,"UTF-8");
		}catch(IOException e) {
			e.printStackTrace();
		}
		//파라미터값 저장 
		Board board = new Board(); 
		board.setWriter(multi.getParameter("writer")); //화면에 입력받은 값들
		board.setPass(multi.getParameter("pass"));
		board.setSubject(multi.getParameter("subject"));
		board.setContent(multi.getParameter("content"));
		board.setFile1(multi.getFilesystemName("file1")); //파일의 이름이여서 파라미터가아님!
		board.setIp(request.getLocalAddr());
		String boardid =
				(String)request.getSession().getAttribute("boardid");
		if(boardid==null) boardid="1"; //공지사항
		board.setBoardid(boardid);
		if(board.getFile1()==null) board.setFile1(""); //파일이 없으면 빈문자열
		
		//num : board 테이블 중 최대 num값 
		int num = dao.maxnum();
		board.setNum(++num); //board테이블의 최대 num값 +1 => 새로추가되야하니까 가장큰값에서 1증가
		board.setGrp(num);
		
		//dao.insert가 false인 경우
		String msg = "게시물 등록 실패";
		String url = request.getContextPath()+"/board/writeForm";
		
		//dao.insert가 true인 경우 => 게시물등록 성공
		if(dao.insert(board)) { //board에 파라ㅁ
			msg="게시물이 등록되었습니다";
			url = request.getContextPath()
					+"/board/list?boardid="+boardid+"&pageNum=1";
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return "/view/alert.jsp";
	}
	//http://localhost:8088/jsp3/board/list?boardid=1
	@RequestMapping("list")
	public String list(HttpServletRequest request,
					   HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpSession session = request.getSession(); //session객체생성
		
		//boardid 파라미터가 존재하는경우
		if(request.getParameter("boardid") != null) {
			//session에 boardid라는 이름으로 파라미터를 등록
			session.setAttribute("boardid", request.getParameter("boardid"));
			//session에 pagenum라는 이름으로 등록
			session.setAttribute("pagenum", "1");
		}
		String boardid = (String) session.getAttribute("boardid");
		
		//boardid ==null : session에 boardid 속성값이 없는경우
		if(boardid ==null) boardid ="1";
		int pageInt =1;	//pageInt:현재페이지 
		int limit =10;	// 한페이지당 출력되는 게시물의 건수
		
		//pageNum 파라미터값이 존재하는경우
		if(request.getParameter("pageNum") != null) {
			session.setAttribute //속성에등록
				("pageNum",request.getParameter("pageNum"));
		}
		String pageNum = (String) session.getAttribute("pageNum");
		if(pageNum==null) { //pagebnum이 없는경우
			pageNum="1";
		}
		pageInt = Integer.parseInt(pageNum); //현재페이지
/*
 * 	dao.boardCount(boardid):
 * 		board 테이블의 boardid값에 해당하는 게시물 건수 리턴			
 */
		
		//boardcount : 게시판 종류별 등록된 게시물 건수
		int boardcount = dao.boardCount(boardid); //boardid(게시판의종류) board id에 해당하는 갯수만큼만 전달받기
		
		//list: pageInt에 해당하는 등록된 게시물 목록 
		List<Board> list = dao.list(pageInt,limit,boardid); //현재페이지,페이지당 게시물건수,게시판종류
		
		//pageInt: 1 boardcount:5
		//pageInt: 2(현재페이지) boardcount:15(게시판 총 등록된 게시물갯수) boardnum:5
		//페이지가 시작되는 첫번째 게시물의 num값
		int boardnum = boardcount - (pageInt -1) * limit;
		int bottomLine = 3; //한번에 보여지는 페이지갯수 3초과면 << 3 >> 
		
		/*
		 * startPage : 화면하단에 출력되는 시작페이지번호
		 * endpage : 화면하단에 출력되는 끝페이지번호
		 * 
		 * pageInt   startPage		endPage
		 * 	 1      	1			  	3
		 * 			 (1	- 1) / 3 * 3 + 1 => 1
		 * 	 
		 * 2			1				3 
		 * 	 		 (2	- 1) / 3 * 3 + 1 => 1
		 * 	 
		 * 		
		 */
		int startPage = 
					(pageInt -1) / bottomLine * bottomLine + 1;
		int endPage = startPage + bottomLine -1;
		
		//maxPage:게시물건수에 의한 최대페이지 ex)게시물이 11개면 페이지2개
		int maxPage =
		(boardcount / limit) + (boardcount % limit == 0? 0: 1);
		
		//ex) 게시물이11개면 maxpage가 2개필요 하지만 endpage는 3이기때문에 endpage=maxpage.
		if(endPage > maxPage) endPage = maxPage;
		
		
		String boardName = "공지사항";
		switch (boardid) {
		case "3":
			boardName = "QNA"; break;
		case "2":
			boardName= "자유게시판"; break;
		}
		request.setAttribute("boardName", boardName);		//게시판이름
		request.setAttribute("pageInt", pageInt);			//현재페이지
		request.setAttribute("boardid", boardid);			//게시판의종류
		request.setAttribute("boardcount", boardcount);		//게시물등록건수
		request.setAttribute("list", list);					//해당페이지 게시물목록
		request.setAttribute("boardnum", boardnum);			//현재페이지의 게시물 시작번호
		request.setAttribute("startPage", startPage);		//페이지의 하단 시작페이지번호
		request.setAttribute("bottomLine", bottomLine);		//한 페이지의 보여질 페이지갯수
		request.setAttribute("endPage", endPage);			//페이지의 끝페이지번호
		request.setAttribute("maxPage", maxPage);			//게시물건수에 의한 최대 페이지 번호
		return "/view/board/list.jsp";
	}
	@RequestMapping("info")
	public String info(HttpServletRequest request,
					   HttpServletResponse response) {
		//num: 게시물번호. 파라미터값 저장
		int num = Integer.parseInt(request.getParameter("num"));
		
		//num에 해당하는 정보를 db에서 읽어서 Board객체에 저장
		Board b = dao.selectOne(num);
		
		//조회수를 증가
		dao.readcntAdd(num);
		
		//view에 board객체(b)객체를 전달. request객체에 속성등록
		request.setAttribute("b", b);
		return "/view/board/info.jsp";
	}
	@RequestMapping("updateForm")
	public String updateForm(HttpServletRequest request,
							 HttpServletResponse response) {
		//num : 게시물번호, 파라미터값 저장됨
		int num = Integer.parseInt(request.getParameter("num"));
		
		//num에 해당하는 정보를 DB에서 읽어서 Board객체에 저장
		Board b = dao.selectOne(num);
		request.setAttribute("b", b);
		return "/view/board/updateForm.jsp";
	}
	/*
	 * 1.파일 업로드하기 (업로드 위치설정) write부분
	 * 2.파라미터 정보를 Board 객체 저장
	 * 3.비밀번호 불일치
	 * 	비밀번호 오류 메세지 출력하고,updateForm로 페이지이동
	 * 4. 비밀번호 일치
	 * 	첨부파일의 변경이 없는경우 file2 파라미터의 내용을 file1 프로퍼티에 저장하기
	 * 	파라미터의 내용으로 해당 게시물의 내용을 수정하기.
	 * 	boolean BoardDao.update(Board)
	 * 		수정성공:수정성공 메세지 출력 후 info 페이지이동
	 * 		수정실패:수정실패 메세지 출력 후 updateForm 페이지 이동
	 * 
	 */
	@RequestMapping("update")
	public String update(HttpServletRequest request,
						 HttpServletResponse response) {
		//1. 파일업로드 (업로드위치,크기지정)
		String path = getServletContext().getRealPath("/")+"/upload/";
		int size = 10*1024*1024;
		MultipartRequest multi = null;
		try {
			multi = new MultipartRequest(request,path,size,"UTF-8");
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		//2.파라미터정보 Board에 저장
		
		Board board = new Board();
		board.setNum(Integer.parseInt(multi.getParameter("num")));
		board.setWriter(multi.getParameter("writer"));
		board.setPass(multi.getParameter("pass"));
		board.setSubject(multi.getParameter("subject"));
		board.setContent(multi.getParameter("content"));
		board.setFile1(multi.getFilesystemName("file1"));
		if(board.getFile1() == null || board.getFile1().equals("")) {
			board.setFile1(multi.getParameter("file2"));
		}
		
		//3,4 비밀번호 검증
		
		String msg = "비밀번호가 틀렸습니다";
		String url = "updateForm?num="+board.getNum();
		
		Board dbBoard = dao.selectOne(board.getNum());
		
		//비밀번호 맞은경우
		if(board.getPass().equals(dbBoard.getPass())) {
			//DB에 내용 수정
			if(dao.update(board)) {
				msg="게시물이 변경되었습니다.";
				url="info?num="+board.getNum();
			}else {//업데이트안된경우
				msg="게시물 변경 시 오류가 발생했습니다.";
			}
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return "/view/alert.jsp";
	}
	@RequestMapping("deleteForm")
	public String deleteForm(HttpServletRequest request,
							 HttpServletResponse response) {
		return "/view/board/deleteForm.jsp";  //deleteForm 요청이들어오면 실행!
	}
	@RequestMapping("delete")
	public String delete(HttpServletRequest request,
			 HttpServletResponse response) { 
		//deleteForm에서 실행을 요청하기때문에 num값과 pass값 num,pass값 필요
		int num = Integer.parseInt(request.getParameter("num"));
		String pass = request.getParameter("pass");
		
		Board board = dao.selectOne(num);
		String msg = "비밀번호가 틀립니다";
		String url = "deleteForm?num="+num;
		
		if(pass.equals(board.getPass())) {
			if(dao.delete(num)) {
				msg=board.getWriter()+"님의 게시글이 삭제되었습니다.";
			}else {
				msg = "게시글 삭제시 오류가 발생했습니다.";
			}
			url = "list?boardid="+board.getBoardid();
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return "/view/alert.jsp";
	}
	@RequestMapping("replyForm")
	public String replyForm(HttpServletRequest request,
							HttpServletResponse response) {
		int num = Integer.parseInt(request.getParameter("num"));
		Board board = dao.selectOne(num);
		request.setAttribute("board", board);
		return "/view/board/replyForm.jsp";
	}
	/*
	 * 1. 파라미터값을 Board 객체에 저장하기
	 * 	  원글정보에 해당하는 파라미터: num, boardid,grp,grplevel, grpstep
	 * 	  답글정보에 해당하는 파라미터: writer,pass,subject,content =>입력한 내용
	 * 
	 * 2. 같은 grp에 해당하는 게시물들의 grpstep을 1씩 증가하기.
	 * 	  원글의 grpstep보다 큰 grpstep만(답글들) 증가
	 * 3. 답글에대한 정보를 DB에 추가하기(insert)
	 * 	  num : maxnum + 1
	 * 	  grp : 원글과 동일하게
	 * 	  grplevel : 원글 + 1
	 *    grpstep  : 원글 + 1
	 * 4. 등록성공: 답변등록완료 메세지 추가 후 list로 페이지 이동
	 * 	  등록실패: 답변등록실패 메세지 추가 후 replyForm페이지로 이동
	 */
	@RequestMapping("reply")
	public String reply(HttpServletRequest request,
						HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//1.파라미터 값을 Board 객체에 저장하기
		Board board = new Board();
		//답글에대한 정보
		board.setWriter(request.getParameter("writer"));
		board.setPass(request.getParameter("pass"));
		board.setSubject(request.getParameter("subject"));
		board.setContent(request.getParameter("content"));
		board.setBoardid(request.getParameter("boardid"));
		board.setGrp(Integer.parseInt(request.getParameter("grp")));
		
		//답글에서 바뀌어야되는 부분
		int num = Integer.parseInt(request.getParameter("num"));
		int grp = Integer.parseInt(request.getParameter("grp"));
		int grplevel = Integer.parseInt(request.getParameter("grplevel"));
		int grpstep = Integer.parseInt(request.getParameter("grpstep"));
		
		//2 grpstep 1 증가(들어갈 자리 찾기위해)
		dao.grpStepAdd(grp,grpstep); //Dao에 코딩
		
		//3.답글 db에 insert 하는부분
		board.setNum(dao.maxnum() + 1); //게시물번호 증가
		board.setGrplevel(grplevel + 1);
		board.setGrpstep(grpstep + 1); //원글 다음자리에 출력
		board.setFile1("");
		String msg = "답변 등록시 오류가 발생했습니다.";
		String url = "replyForm?num="+num;
		if(dao.insert(board)) {
			msg="답변 등록이 완료되었습니다.";
			url = "list?boardid="+board.getBoardid();
		}
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);
		return "/view/alert.jsp";
	}
}
