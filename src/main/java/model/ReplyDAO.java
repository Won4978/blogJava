package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DB;

public class ReplyDAO {
	
	public int insert(ReplyDTO dto) {
		
		String sql="insert into reply(rno,bno,writer,content)"
				+ "values (seq_reply.nextval,?,?,?)";
		int rno = -1;
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			pre.setInt(1, dto.getBno());
			pre.setString(2, dto.getWriter());
			pre.setString(3, dto.getContent());
			pre.executeUpdate();
			
			try(PreparedStatement pre2 = con.prepareStatement("select seq_reply.currval from dual");
					ResultSet res = pre2.executeQuery()) {
				
				if(res.next()) {
					rno = res.getInt(1);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rno;
	}
	
	
	public List<ReplyDTO> selectList(int bno) {
		String sql ="select r.rno, r.bno, r.writer, m.nickname, r.content, to_char(r.reg_date,'YYYY-MM-DD') as reg_date\r\n"
				+ "from reply r\r\n"
				+ "join member m\r\n"
				+ "on r.writer = m.id\r\n"
				+ "where r.bno = ?\r\n"
				+ "order by r.rno asc";
		List<ReplyDTO> list = new ArrayList<ReplyDTO>();
		
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			pre.setInt(1,bno);
			try(ResultSet res = pre.executeQuery()){	
			while(res.next()) {
				ReplyDTO dto = new ReplyDTO();
				dto.setRno(res.getInt("rno"));
				dto.setBno(res.getInt("bno"));
				dto.setWriter(res.getString("writer"));
				dto.setNickName(res.getString("nickname"));
				dto.setContent(res.getString("content"));
				dto.setRegDate(res.getString("reg_date"));
				list.add(dto);				
			}
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	} //selectList
	
	public int replyDelete(int rno, String writer) {
		String sql ="delete from reply where rno=? and writer=?";
		int result = 0;
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			pre.setInt(1, rno);
			pre.setString(2, writer);
			result = pre.executeUpdate();			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	} //replyDelete
	
	public int replyUpdate(int rno, String content, String writer) {
		String sql = "update reply set content = ? where rno=? and writer=?";
		int result =0;
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			pre.setString(1, content);
			pre.setInt(2, rno);
			pre.setString(3, writer);
			result = pre.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	} //replyUpdate
	
	public List<ReplyDTO> selectListByWriter(String writer){
		
		String sql = "select r.rno, r.bno, b.title as board_title, r.content,\r\n"
				+ "        to_char(r.reg_date,'YYYY-MM-DD') as reg_date\r\n"
				+ "from reply r\r\n"
				+ "join board b\r\n"
				+ "on r.bno = b.bno\r\n"
				+ "where r.writer = ?\r\n"
				+ "order by r.bno desc";
		
		List<ReplyDTO> list = new ArrayList<ReplyDTO>();
		
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			pre.setString(1, writer);
			
			try(ResultSet res = pre.executeQuery()){
				while(res.next()) {
					ReplyDTO dto = new ReplyDTO();
					dto.setRno(res.getInt("rno"));
					dto.setBno(res.getInt("bno"));
					dto.setBoardTitle(res.getString("board_title"));
					dto.setContent(res.getString("content"));
					dto.setRegDate(res.getString("reg_date"));
					list.add(dto);
				}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
		
	} //selectListByWriter

	
	
	
	
	
}
