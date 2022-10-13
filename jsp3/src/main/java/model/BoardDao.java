package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	
}
