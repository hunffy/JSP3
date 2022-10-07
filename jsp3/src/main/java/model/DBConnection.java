package model;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {
	private DBConnection() {} // 생성자(클래스와 이름이같다,리턴타입이없음) , 접근제한자가 private이여서 객체생성 불가.
	//DBConnction.getConnection()
	public static Connection getConnection() { //데이터베이스와 연결해주는 역할
		Connection conn = null;
		try {
			//Class.forName("문자열") 문자열로 지정한 클래스를 읽어서 메모리에 로드. 해당클래스가없으면 예외발생
			Class.forName("oracle.jdbc.driver.OracleDriver"); //오라클드라이버와 연결
			
			conn = DriverManager.getConnection //오라클데이터베이스와 프로그램연결
					("jdbc:oracle:thin:@localhost:1521:xe","kic","1234"); //url 이나 사용자 이름,비번등이 잘못되면 예외발생
			
		}catch(Exception e) { //
			e.printStackTrace(); //printStackTrace(): 콘솔창에 에러가난 이유와 위치를 모두 출력해줌 
		}
		return conn; //오류나면 conn 값을 null로 리턴
	}
	
	
	public static void close
		(Connection conn, Statement stmt, ResultSet rs) { //stmt: select문장을 실행할수있게해주는,, rs:select문장 결과값
	 try {
		if(rs != null) rs.close(); //close()는 반드시 예외처리를 해줘야하는 메서드
		if(stmt != null) stmt.close();
		if(conn != null) conn.close();
	}catch(Exception e) {
		e.printStackTrace();
		}
	}
}
