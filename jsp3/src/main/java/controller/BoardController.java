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
	@RequestMapping("list")
	public String list(HttpServletRequest request,
					   HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpSession session = request.getSession();
		if(request.getParameter("boardid") != null) {
			session.setAttribute("boardid", request.getParameter("boardid"));
			session.setAttribute("pagenum", "1");
		}
		String boardid = (String) session.getAttribute("boardid");
		if(boardid ==null) boardid ="1";
		int pageInt =1;
		int limit =10;
		if(request.getParameter("pageNum") != null) {
			session.setAttribute
				("pageNum",request.getParameter("pageNum"));
		}
		String pageNum = (String) session.getAttribute("pageNum");
		if(pageNum==null) {
			pageNum="1";
		}
		pageInt = Integer.parseInt(pageNum);
/*
 * 	dao.boardCount(boardid):
 * 		board 테이블의 boardid값에 해당하는 게시물 건수 리턴			
 */
//		int boardcount = dao.boardCount(boardid);
//		List<Board> list = dao.list(pageInt,limit,boardid);
		int boardcount = 0;
		List<Board> list = null;
		
		int boardnum = boardcount - (pageInt -1) * limit;
		int bottomLine = 3;
		int startPage =
					(pageInt -1) / bottomLine * bottomLine + 1;
		int endPage = startPage + bottomLine -1;
		int maxPage =
		(boardcount / limit) + (boardcount % limit == 0? 0: 1);
		if(endPage > maxPage) endPage = maxPage;
		String boardName = "공지사항";
		switch (boardid) {
		case "3":
			boardName = "QNA"; break;
		case "2":
			boardName= "자유게시판"; break;
		}
		request.setAttribute("boardName", boardName);
		request.setAttribute("pageInt", pageInt);
		request.setAttribute("boardid", boardid);
		request.setAttribute("boardcount", boardcount);
		request.setAttribute("list", list);
		request.setAttribute("boardnum", boardnum);
		request.setAttribute("startPage", startPage);
		request.setAttribute("bottomLine", bottomLine);
		request.setAttribute("endPage", endPage);
		request.setAttribute("maxPage", maxPage);
		return "/view/board/list.jsp";
	}
	
}
