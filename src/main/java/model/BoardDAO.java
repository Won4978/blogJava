package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardUpLeftHandler;

import util.DB;

public class BoardDAO {
	
	public void insert(BoardDTO dto) {
		Connection con = null;
		PreparedStatement pre = null;
		
		String sql = "insert into board (bno,title,content,writer,img_path) values(seq_board.nextval,?,?,?,?)";
			
		try {
			con=DB.getInstance();
			pre=con.prepareStatement(sql);
			pre.setString(1, dto.getTitle());
			pre.setString(2, dto.getContent());
			pre.setString(3, dto.getWriter());
			pre.setString(4, dto.getImgPath());
			pre.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pre!=null)pre.close();
				if(con!=null)con.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	} //insert
	
	// 검색 조건에 맞는 where 절 만들기 (selectCount, selectList 둘 다 같은 조건을 써야 해서 분리)
	private String buildWhere(String searchtype, String keyword) {
		if(keyword == null || keyword.trim().isEmpty()) {
			return "";
		}
		
			if("title".equals(searchtype)){
				return " where b.title like ? ";
			}else if("content".equals(searchtype)) {
				return " where b.content like ? ";
			}else if("writer".equals(searchtype)) {
				return " where m.nickname like ? ";
			}
			
			return "";
	}
	
	// 검색 조건에 맞는 전체 글 갯수
	public int selectCount(String searchtype, String keyword) {

	    String where = buildWhere(searchtype, keyword);

	    String sql = "select count(*) from board b join member m on "
	            + "b.writer = m.id" + where;

	    try(Connection con = DB.getInstance();
	        PreparedStatement pre = con.prepareStatement(sql)) {

	        if(!where.isEmpty()) {
	            pre.setString(1, "%" + keyword + "%");
	        }

	        try(ResultSet res = pre.executeQuery()) {
	            if(res.next()) {
	                return res.getInt(1);
	            }
	        }

	    } catch(Exception e) {
	        e.printStackTrace();
	    }

	    return 0;
	}
	
	// 게시글 목록 조회 (검색 + 페이징)
	// startRow 가져올 시작 순번(1번)
	// endRow 가져올 끝 번호 
	// 한페이지 9개일때 2페이지 -> start = 10, end = 18
	
	public List<BoardDTO> selectList(String searchtype, String keyword, int startRow, int endRow) {
		
		String where = buildWhere(searchtype, keyword);
		
		Connection con = null;
		PreparedStatement pre = null;
		ResultSet res = null;
		
		// StringBuilder : 문자열의 내용을 계속 변경할 수 있는 객체
		String sql ="select * from ( \r\n"
				+ "    select rownum as rn, t.* from (\r\n"
				+ "        select b.bno, b.title, b.writer, m.nickname as writer_nickname, to_char(b.reg_date, 'YYYY-MM-DD') as reg_date,\r\n"
				+ "                b.view_cnt, b.like_cnt, b.img_path\r\n"
				+ "        from board b\r\n"
				+ "        join member m\r\n"
				+ "        on b.writer = m.id \r\n"
				+ 		   where
				+ "        order by b.bno desc\r\n"
				+ "        ) t where rownum <= ?\r\n"
				+ "    ) where rn >= ?";
				

		
		List<BoardDTO> list = new ArrayList<BoardDTO>();

		try {
			con = DB.getInstance();
			pre = con.prepareStatement(sql);
			
			// ? 대입
			int idx = 1;
			if(!where.isEmpty()) {
				pre.setString(idx++,"%"+keyword+"%");
			}
			pre.setInt(idx++, endRow);
			pre.setInt(idx, startRow);

			res = pre.executeQuery();
			while(res.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setBno(res.getInt("bno"));
				dto.setTitle(res.getString("title"));
				dto.setWriter(res.getString("writer"));
				dto.setWriterNickname(res.getString("writer_nickname"));
				dto.setRegDate(res.getString("reg_date"));
				dto.setViewCnt(res.getInt("view_cnt"));
				dto.setLikeCnt(res.getInt("like_cnt"));
				dto.setImgPath(res.getString("img_path"));
				list.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(res!=null)res.close();
				if(pre!=null)pre.close();
				if(con!=null)con.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	} // selectList
	
	
	public BoardDTO selectOne(int bno) {
		Connection con = null;
		PreparedStatement pre = null;
		ResultSet res = null;
		
		String sql = "select b.bno, b.title, b.content , b.writer , m.nickname as writer_nickname, to_char(b.reg_date,'YYYY-MM-DD')as reg_date, \r\n"
				+ "        b.view_cnt, b.like_cnt , b.img_path\r\n"
				+ "from board b\r\n"
				+ "join member m\r\n"
				+ "on b.writer = m.id\r\n"
				+ "where bno=?";
		BoardDTO dto = null;
		
		try {
			con = DB.getInstance();
			pre = con.prepareStatement(sql);
			pre.setInt(1, bno);
			res = pre.executeQuery();
			while(res.next()) {
				dto = new BoardDTO();
				dto.setBno(res.getInt("bno"));
				dto.setTitle(res.getString("title"));
				dto.setContent(res.getString("content"));
				dto.setWriter(res.getString("writer"));
				dto.setWriterNickname(res.getString("writer_nickname"));
				dto.setRegDate(res.getString("reg_date"));
				dto.setViewCnt(res.getInt("view_cnt"));
				dto.setLikeCnt(res.getInt("like_cnt"));
				dto.setImgPath(res.getString("img_path"));				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(res!=null)res.close();
				if(pre!=null)pre.close();
				if(con!=null)con.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return dto;
	} // selectOne
	
//	public void increViewCnt(int bno) {
//		Connection con = null;
//		PreparedStatement pre = null;
//		
//		String sql = "update board set view_cnt = view_cnt +1 where bno =?";
//		
//		try {
//			con = DB.getInstance();
//			pre = con.prepareStatement(sql);
//			pre.setInt(1, bno);
//			pre.executeUpdate();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				if(pre!=null)pre.close();
//				if(con!=null)con.close();
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//		}
//	} // increViewCnt
	
	public void increViewCnt(int bno) {
	    Connection con = null;
	    PreparedStatement pre = null;

	    String sql = "update board set view_cnt = view_cnt + 1 where bno = ?";

	    try {
	        con = DB.getInstance();
	        System.out.println("1. DB 연결");

	        pre = con.prepareStatement(sql);
	        pre.setInt(1, bno);

	        System.out.println("2. UPDATE 실행 직전");

	        pre.executeUpdate();

	        System.out.println("3. UPDATE 실행 완료");

	    } catch(Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if(pre != null) pre.close();
	            if(con != null) con.close();
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public String delete(int bno, String writer) {
	      String sql2 = "select img_path from board where bno=? and writer = ?";
	      String sql="delete from board where bno=? and writer = ?";
	      String imgPath = null;
	      try(Connection conn = DB.getInstance()) {
	         try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
	            pstmt.setInt(1, bno);
	            pstmt.setString(2, writer);
	            try (ResultSet rs = pstmt.executeQuery()) {
	               if(rs.next()) {
	                  imgPath = rs.getString("img_path");
	               }else {
	                  return null;
	               }
	            }
	         }
	         try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, bno);
	            pstmt.setString(2, writer);
	            pstmt.executeUpdate();
	         }
	      }catch(Exception e) {
	         e.printStackTrace();
	      }
	      return imgPath;
	   } //delete
	
	public void update(BoardDTO dto) {
		String sql ="update board set title=?, content=?, img_path=?, mod_date=SYSDATE  where bno=? and writer=?";
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			pre.setString(1, dto.getTitle());
			pre.setString(2, dto.getContent());
			pre.setString(3, dto.getImgPath());
			pre.setInt(4, dto.getBno());
			pre.setString(5, dto.getWriter());
			pre.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	} //update
	
	
	public List<BoardDTO> selectPopularList(){
		
		String sql = "select * from \r\n"
				+ "    ( select b.bno, b.title, m.nickname as writer_nickname,\r\n"
				+ "             to_char(b.reg_date,'YYYY-MM-DD') as reg_date,\r\n"
				+ "             b.view_cnt, b.like_cnt, b.img_path\r\n"
				+ "    from board b\r\n"
				+ "    join member m\r\n"
				+ "    on b.writer = m.id\r\n"
				+ "    order by b.like_cnt desc, b.view_cnt desc, b.bno desc )\r\n"
				+ "where rownum <=6";
		
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			
			try(ResultSet res = pre.executeQuery()){
				while(res.next()) {
					BoardDTO dto = new BoardDTO();
					dto.setBno(res.getInt("bno"));
					dto.setTitle(res.getString("title"));
					dto.setWriterNickname(res.getString("writer_nickname"));
					dto.setRegDate(res.getString("reg_date"));
					dto.setViewCnt(res.getInt("view_cnt"));
					dto.setLikeCnt(res.getInt("like_cnt"));
					dto.setImgPath(res.getString("img_path"));
					list.add(dto);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	} //selectPopularList
	
	public List<BoardDTO> selectPopularHomeList(){
		
		String sql = "select * from \r\n"
				+ "    ( select b.bno, b.title, m.nickname as writer_nickname,\r\n"
				+ "             to_char(b.reg_date,'YYYY-MM-DD') as reg_date,\r\n"
				+ "             b.view_cnt, b.like_cnt, b.img_path\r\n"
				+ "    from board b\r\n"
				+ "    join member m\r\n"
				+ "    on b.writer = m.id\r\n"
				+ "    order by b.like_cnt desc, b.view_cnt desc, b.bno desc )\r\n"
				+ "where rownum <=3";
		
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			
			try(ResultSet res = pre.executeQuery()){
				while(res.next()) {
					BoardDTO dto = new BoardDTO();
					dto.setBno(res.getInt("bno"));
					dto.setTitle(res.getString("title"));
					dto.setWriterNickname(res.getString("writer_nickname"));
					dto.setRegDate(res.getString("reg_date"));
					dto.setViewCnt(res.getInt("view_cnt"));
					dto.setLikeCnt(res.getInt("like_cnt"));
					dto.setImgPath(res.getString("img_path"));
					list.add(dto);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	} //selectPopularHomeList
	
	public List<BoardDTO> selectRecentHomeList(){
		
		String sql = "select * from \r\n"
				+ "    ( select b.bno, b.title, m.nickname as writer_nickname,\r\n"
				+ "             to_char(b.reg_date,'YYYY-MM-DD') as reg_date,\r\n"
				+ "             b.view_cnt, b.like_cnt, b.img_path\r\n"
				+ "    from board b\r\n"
				+ "    join member m\r\n"
				+ "    on b.writer = m.id\r\n"
				+ "    order by b.bno desc )\r\n"
				+ "where rownum <=3";
		
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			
			
			try(ResultSet res = pre.executeQuery()){
				while(res.next()) {
					BoardDTO dto = new BoardDTO();
					dto.setBno(res.getInt("bno"));
					dto.setTitle(res.getString("title"));
					dto.setWriterNickname(res.getString("writer_nickname"));
					dto.setRegDate(res.getString("reg_date"));
					dto.setViewCnt(res.getInt("view_cnt"));
					dto.setLikeCnt(res.getInt("like_cnt"));
					dto.setImgPath(res.getString("img_path"));
					list.add(dto);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	} //selectRecentHomeList
	
	public List<BoardDTO> selectListByWriter(String writer){
		
		String sql = "select bno, title, to_char(reg_date,'YYYY-MM-DD') as reg_date, \r\n"
				+ "        view_cnt, like_cnt, img_path\r\n"
				+ "from board\r\n"
				+ "where writer = ?\r\n"
				+ "order by bno desc";
		
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			pre.setString(1, writer);
			
			try(ResultSet res = pre.executeQuery()){
				while(res.next()) {
					BoardDTO dto = new BoardDTO();
					dto.setBno(res.getInt("bno"));
					dto.setTitle(res.getString("title"));
					dto.setRegDate(res.getString("reg_date"));
					dto.setViewCnt(res.getInt("view_cnt"));
					dto.setLikeCnt(res.getInt("like_cnt"));
					dto.setImgPath(res.getString("img_path"));
					list.add(dto);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	} //selectListByWriter
	
	public List<BoardDTO> selectLikedList(String writer){
		
		String sql = "select b.bno, b.title, m.nickname as writer_nickname, b.like_cnt, b.img_path\r\n"
				+ "from board_like l\r\n"
				+ "join board b\r\n"
				+ "on l.bno = b.bno\r\n"
				+ "join member m\r\n"
				+ "on b.writer = m.id\r\n"
				+ "where l.member_id = ?\r\n"
				+ "order by l.like_date desc";
		
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			pre.setString(1, writer);
			
			try(ResultSet res = pre.executeQuery()){
				while(res.next()) {
					BoardDTO dto = new BoardDTO();
					dto.setBno(res.getInt("bno"));
					dto.setTitle(res.getString("title"));
					dto.setWriterNickname(res.getString("writer_nickname"));
					dto.setLikeCnt(res.getInt("like_cnt"));
					dto.setImgPath(res.getString("img_path"));
					list.add(dto);
				}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	} //selectLikedList
	


} //BoardDAO
