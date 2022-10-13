<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--/jsp3/src/main/webapp/view/member/password.jsp --%>
   <script type="text/javascript">
   	alert("${msg}")           //메세지 출력
   	
   	<c:if test="$(opener)"> //opener가 true야?
   		opener.location.href="${url}" //opener에 url집어넣는다.
   	</c:if>
   		
   	<c:if test="${!opener}">
   		location.href="${url}"	//현재페이지를 url값으로 이동
   	</c:if>
   		
   	<c:if test="${closer}"> //closer가 true면 현재창닫기.
   		self.close()
   	</c:if>
   		
   </script>
   
   <%--2번째 방법
   <script type="text/javascript">
   	alert("${msg}")
   	if(${opener}) {
   		opener.location.href="${url}"
   	}else{
   		location.href="${url}"
   	}
   	if (${closer}) }
   		self.close()
   	}
   	</script>

 --%>