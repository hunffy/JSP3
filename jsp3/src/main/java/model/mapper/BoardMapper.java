package model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import model.Board;

public interface BoardMapper {

		@Select("select nvl(max(num),0) from board") //nvl(값,0) 값이null이면 0으로 표시
		int maxnum();

		@Insert("insert into board (num,writer,pass,subject,"
				+ "content,file1,regdate,readcnt,grp,grplevel,"
				+ "grpstep,boardid,ip)"
				+ "	values (#{num},#{writer},#{pass},#{subject},"
				+ "#{content},#{file1},sysdate,0,#{grp},#{grplevel},"        //#{} Board객체의 프로퍼티를 나타냄
				+ "#{grpstep},#{boardid},#{ip})")
		int insert(Board board);

		
		@Select({"<script>",
			"select count(*) from board where boardid=#{boardid}",
			"<if test='column != null'> and ${column} like '%${find}%'</if>",
			"</script>"})
		int boardCount(Map<String, Object> map);

		@Select({"<script>"," select * from(" +
				" select rownum rnum, a.*from ( "+
				" select * from board where boardid = #{boardid} ",
				"<if test='column != null'> and ${column} like '%${find}%'</if>",
				" order by grp desc, grpstep asc ) a) " +
				" where rnum BETWEEN #{start} and #{end} ",
				"</script>"})
		List<Board> list(Map<String, Object> map);

		@Select("select * from board where num=#{num}")
		Board selectOne(int num);

		@Update("update board set readcnt = readcnt+1 where num=#{num}")
		void readcntAdd(int num);

		@Update("update board set subject=#{subject},content=#{content},file1=#{file1} where num=#{num}")
		int update(Board board);

		@Delete("delete from board where num=#{num}")
		int delete(int num);

		@Update("update board set grpstep = #{grpstep} + 1 "	//grp가 우리가넘겨준값과 같고 grpstep이 우리가 넘겨준값보다 커야된다.
				+ " where grp=# {grp} and grpstep > #{grpstep} ")
		void grpStepAdd(Map<String, Object> map);
}
