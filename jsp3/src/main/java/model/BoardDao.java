package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDao {

	public int maxnum() {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("select nvl(max(num),0) from board"); //nvl: null값일경우 0으로 출력
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return 0;
	}

	public boolean insert(Board board) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		String sql = "insert into board (num,writer,pass,subject,"
					+ "content,file1,regdate,readcnt,grp,grplevel,"
					+ "grpstep,boardid,ip) "
					+ "values (?,?,?,?,?,?,sysdate,0,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql); //sql 구문실행
			pstmt.setInt(1, board.getNum());
			pstmt.setString(2, board.getWriter()); //글쓴이등록
			pstmt.setString(3, board.getPass());
			pstmt.setString(4, board.getSubject());
			pstmt.setString(5, board.getContent()); //내용
			pstmt.setString(6, board.getFile1());	//파일이름
			pstmt.setInt(7, board.getGrp());
			pstmt.setInt(8, board.getGrplevel());
			pstmt.setInt(9, board.getGrpstep());
			pstmt.setString(10, board.getBoardid());
			pstmt.setString(11, board.getIp());
			return pstmt.executeUpdate() > 0; //등록
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, null);
		}
		return false;	//등록이 되지않을때
	}
	public int boardCount(String boardid) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		String sql = "select count(*) from board where boardid=?"; //boardid가 만약1이라면 공지사항의 게시물갯수를 구하는구문
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardid); //sql ?에 들어가는값
			rs = pstmt.executeQuery();
			rs.next();
			return rs.getInt(1); //게시판 종류별 등록된 게시물 건수	
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return 0; //오류가나면 건수 0
	}
	public List<Board> list
				(int pageInt, int limit, String boardid) {
		//pageInt:조회하고자 하는 페이지번호(현재페이지)
		//limit : 페이지당 보여지는 게시물 건수
		//boardid : 게시판의 종류. 1:공지사항, 2:자유게시판 , 3.QnA
		
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		
		/*	1.공지사항으로 등록된 게시물의 등록된순서의 역순으로 출력
		 * select * from board where board =1 
		 * oreder by grp desc , grpstep asc
		 * 
		 * 2. 1번의 
		 * select rownum rnum, a.*from
		 * rnum= 게시물등록순서
		 * 
		 * BETWEEN 
		 */
		
		
		String sql = 	" select * from (" + 
						" select rownum rnum, a.*from (" +
						" select * from board where boardid =?" +
						" order by grp desc, grpstep asc ) a)" +                  
						" where rnum BETWEEN ? and ?" ;
		ResultSet rs = null;
		List<Board> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardid);
			/*
			 * pageInt : 1 rnum:1~10
			 * pageInt : 2 rnum:110~20
			 */
			pstmt.setInt(2, (pageInt-1)*limit + 1); //1
			pstmt.setInt(3, pageInt*limit);			//10
			rs = pstmt.executeQuery(); //10개의 레코드만 조회
			while(rs.next()) {
				Board b = new Board();
				b.setNum(rs.getInt("num"));
				b.setWriter(rs.getString("writer"));
				b.setPass(rs.getString("pass"));
				b.setSubject(rs.getString("subject"));
				b.setContent(rs.getString("content"));
				b.setFile1(rs.getString("file1"));
				b.setGrp(rs.getInt("grp"));
				b.setGrpstep(rs.getInt("grpstep"));
				b.setGrplevel(rs.getInt("grplevel"));
				b.setReadcnt(rs.getInt("readcnt"));
				b.setRegdate(rs.getDate("regdate"));
				list.add(b);
			}
			return list;
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return null;
	}
	
	public Board selectOne(int num) { //게시물번호에 해당하는 정보를 조회
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		String sql = "select * from board where num=?";
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num); //2번게시물 선택했으면
			rs = pstmt.executeQuery();
			if(rs.next()) { //2번게시물에대한 내용들을 보드객체 b에넣어 리턴
				Board b = new Board();
				b.setNum(rs.getInt("num")); //num이라는 컬럼명의 정보들
				b.setBoardid(rs.getString("boardid"));
				b.setWriter(rs.getString("writer"));
				b.setPass(rs.getString("pass"));
				b.setSubject(rs.getString("subject"));
				b.setContent(rs.getString("content"));
				b.setFile1(rs.getString("file1"));
				b.setGrp(rs.getInt("grp"));
				b.setGrplevel(rs.getInt("grplevel"));
				b.setGrpstep(rs.getInt("grpstep"));
				b.setReadcnt(rs.getInt("readcnt"));
				b.setRegdate(rs.getDate("regdate"));
				return b; //b객체에 게시물번호에 해당하는 정보 담김
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return null;
	}
	//조회수 1증가
	public void readcntAdd(int num) {
		String sql = "update board set readcnt = readcnt+1 where num=?";
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, null);
		}
	}

	public boolean update(Board board) {
		String sql =
					"update board set " + " subject=?,content=?,file1=?" + " where num=?";
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt =null;
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, board.getSubject());
			pstmt.setString(2, board.getContent());
			pstmt.setString(3, board.getFile1());
			pstmt.setInt(4, board.getNum());
			return pstmt.executeUpdate()>0;
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, null);
		}
		return false;
	}

	public boolean delete(int num) {
		String sql = "delete from board where num=?";
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			return pstmt.executeUpdate()	> 0;
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, null);
		}
		return false;
	}

	public void grpStepAdd(int grp, int grpstep) {
		String sql = "update board set grpstep = grpstep + 1 "
					+ " where grp=? and grpstep > ?"; //grp가 우리가넘겨준값과 같고 grpstep이 우리가 넘겨준값보다 커야된다.
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, grp);
			pstmt.setInt(2, grpstep);
			pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, null);
		}
	}
	
	
	
}
