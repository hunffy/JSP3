package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
public class MemberDao { 
	//mem 내용을 db에 추가하는함수.
	public boolean insert(Member mem) {	//true:mem에 값이 있을때 실행되는구문
		
		//1. Connection 객체 생성
		Connection conn = DBConnection.getConnection(); //db와 연결
		
		//2. Statement 객체 생성
		/*
		 *  PreparedStatement : Statement의 하위 인터페이스
		 *  					1.먼저 sql 문장을 db에 미리 전송
		 *  
		 *  					2.나중에 파라미터로 값을 전달 방식
		 */
		
		PreparedStatement pstmt = null; //데이터베이스에 값을전달할때 ?를통해 값을전달
		String sql = "insert into member (id,pass,name,gender,tel,email,picture)"
				+"values (?,?,?,?,?,?,?)"; //컬럼의 갯수와 물음표의갯수는 같아야함 , sql문장을 먼저전송
		try {
			pstmt = conn.prepareStatement(sql); //데이터베이스에 conn을통해 데이터베이스로 로드됨.
			pstmt.setString(1, mem.getId());	//1 : sql중 첫번째 물음표는 getId()
			pstmt.setString(2, mem.getPass());	//2. : 두번째 물음표
			pstmt.setString(3, mem.getName());
			pstmt.setInt(4, mem.getGender());
			pstmt.setString(5, mem.getTel());
			pstmt.setString(6, mem.getEmail());
			pstmt.setString(7, mem.getPicture());
			
			//executeUpdate() : select 외에 사용되는 메서드
			//					변경되는(수정된) 레코드 갯수 리턴
			
			
//실행 executeUpdate() : sql문장 실행됨 회원정보가 db에 insert됨 //select구문이아니기때문에 업데이트사용
			int cnt = pstmt.executeUpdate();  //데이터와 쿼리문을 실행하는구문 ->db에 데이터 추가
			if(cnt >0) return true; //데이터 변경된(수정된)값이 0보다 크면
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			//return을 만나도 반드시실행
			DBConnection.close(conn, pstmt, null);
		}
		return false;
	}
	
	
	public Member selectOne(String id) { //String id ->입력한 id값
		Connection conn = DBConnection.getConnection(); //DB와 연결
		String sql = "select * From member where id= ?"; //멤버테이블에서 모든 컬럼중 id값 조회
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); //sql = select * From member where id= ?
			pstmt.setString(1,id); // ?값 
			
			//ResultSet executeQuery() : select 구문실행
			rs = pstmt.executeQuery(); //"select * From member where id= ?"이 실행되는 부분
			if(rs.next()) { //rs.next() 참이야? -> 현재 id에 해당하는 레코드가 존재하니?
				
				Member mem = new Member(); //데이터베이스들의 값들을 멤버객체를통해 화면에 전달
				
				mem.setId(rs.getString("id")); //id값을 mem객체 id값을넣어
				mem.setPass(rs.getString("pass"));
				mem.setName(rs.getString("name"));
				mem.setGender(rs.getInt("gender"));
				mem.setTel(rs.getString("tel"));
				mem.setEmail(rs.getString("email"));
				mem.setPicture(rs.getString("picture"));
				return mem;
			}
		}catch (SQLException e)	{
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return null; //id가 존재하지않을때(레코드가없을때)
	}
	public boolean update(Member mem) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		String sql = "update member set name=?,gender=?,email=?,"+"tel=?,picture=? where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,mem.getName());
			pstmt.setInt(2,mem.getGender());
			pstmt.setString(3,mem.getEmail());
			pstmt.setString(4,mem.getTel());
			pstmt.setString(5,mem.getPicture());
			pstmt.setString(6,mem.getId());
			
			return pstmt.executeUpdate() > 0;
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, null);
		}
		return false;
	}
	
	public boolean delete(String id) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("delete from member where id=?");
			pstmt.setString(1,id);
			return pstmt.executeUpdate() > 0;
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, null);
		}
		return false;
	}
	
	public  List<Member> list() {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Member> list = new ArrayList<>(); //list 멤버객체를 여러개가질수있는 list
		
		try {
			pstmt = conn.prepareStatement("select * from member"); //member테이블의 모든 레코드,컬럼 다 조회
			rs = pstmt.executeQuery();
			while(rs.next()) { //rs.next() : 멤버가 존재하면 -> while 실행 // 멤버가3명이면 3번실행
				Member m = new Member();
				m.setId(rs.getString("id"));
				m.setPass(rs.getString("pass"));
				m.setName(rs.getString("name"));
				m.setGender(rs.getInt("gender"));
				m.setTel(rs.getString("tel"));
				m.setEmail(rs.getString("email"));
				m.setPicture(rs.getString("picture"));
				list.add(m); //list에 DB의 정보를 저장한 Member 객체추가
			}
			return list; //while문 끝나면 , member 테이블의 모든정보를 담은 list객체를 전달(리턴)해줌.
		}catch(SQLException e) { //중간에 오류가발생하면 예외처리
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return null;
	}	
	public boolean updatePass(String id, String pass) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		String sql = "update member set pass=? where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,pass);
			pstmt.setString(2,id);
			return pstmt.executeUpdate() > 0;
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, null);
		}
		return false;
	}

	public String idSearch(String email, String tel) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("select id from member where email=? and tel=?");
			pstmt.setString(1, email);
			pstmt.setString(2, tel);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return null;
	}

	public String pwSearch(String id, String email, String tel) {
		Connection conn = DBConnection.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	try {
		pstmt = conn.prepareStatement("select pass from member where id=? and email=? and tel=?");
		pstmt.setString(1,id);
		pstmt.setString(2,email);
		pstmt.setString(3,tel);
		rs = pstmt.executeQuery();
		if(rs.next()) {
			return rs.getString("pass");
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}finally {
		DBConnection.close(conn, pstmt, rs);
	}
	return null;
	}
}
