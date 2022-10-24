package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/select")
//http://localhost:8088/jsp3/select
// kiclayout.jsp 에서 ajax로 요청되는 페이지.
public class SelectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String si;
	private String gu;
    public SelectServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		//BufferedReader : 필터 입력 스트림
		//				   입력스트림에 버퍼를 할당함. 성능 향상 스트림
		// 				   readLine() 메서드를 멤버로 가짐.
		//FileReader : 파일에서 문자형 데이터를 읽기위한 스트림
		//getServletContext().getRealPath("/") : webapp 폴더경로
		BufferedReader fr = new BufferedReader
		(new FileReader(request.getServletContext().getRealPath("/")+"file/sido.txt"));
		
		si = request.getParameter("si");
		gu = request.getParameter("gu");
		
		//LinkedHashSet : 추가한 순서를 유지, 중복불가
		Set<String> set = new LinkedHashSet<>(); //[서울특별시]
		String data = null;
		if(si == null && gu == null) {
			//data : 서울특별시 종로구 종로구
			//2번째 data : 서울특별시 종로구 청운동
			while((data = fr.readLine()) != null) {
				
				// \\s+ : 정규화 표현식. 공백문자 1개이상을 의미
				// \\s  : 공백을 정규식으로 표현한것.
				//  +   : 1개이상이라는 의미. 
				// arr[0] : 서울특별시		서울특별시(중복이기때문에 추가x)
				// arr[1] : 종로구		종로구
				// arr[2] : 종로구  		청운동
				String[] arr = data.split("\\s+");
				if(arr.length >= 3 ) set.add(arr[0].trim()); //[0]번지 시만추가
			}
		} else if ( gu == null){ //si 파라미터 존재하는경우. 시가 선택된경우
			si = si.trim();
			while((data=fr.readLine()) != null) {
				String[] arr= data.split("\\s+");
				
				//si : 서울특별시. (선택한 시 )
				if(arr.length >= 3 && arr[0].equals(si) && 
						!arr[0].equals(arr[1])) {
					set.add(arr[1].trim()); //서울특별시에 해당하는 구를 arr[1]에 추가
				}
			}
		}else { //si,gu 파라미터 둘다 존재하는경우 (시,구 선택된경우)
			si = si.trim();
			gu = gu.trim();
			while((data=fr.readLine()) != null) {
				String[] arr = data.split("\\s+");
				if(arr.length >= 3 && arr[0].equals(si) && 
						arr[1].equals(gu) && !arr[0].equals(arr[1]) &&
					   !arr[1].equals(arr[2])) {
					if(arr.length > 3) arr[2] += " " + arr[3];
					set.add(arr[2].trim()); //동정보 저장
				}
			}
		}
		//text/plain : 순수 문자열로 인식 
		//charset=utf-8 : 브라우저가 한글을 인식할수있도록 인코딩
		response.setContentType("text/plain; charset=utf-8");
		List<String> list = new ArrayList<>(set);
		Collections.sort(list); //정렬 
		response.getWriter().println(list);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
