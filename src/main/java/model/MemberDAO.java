package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DB;

//public class MemberDAO {
//	
//	public boolean idCheck(String id) {
//		Connection con = null;
//		PreparedStatement pre = null;
//		ResultSet res = null;
//		String sql ="select id from member id=?";
//		boolean isDupicate = false;
//		try{
//			con=DB.getInstance();
//			pre = con.prepareStatement(sql);
//			pre.setString(1, id);
//			res=pre.executeQuery(sql);
//			if(res.next()) {
//				return true;
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return isDupicate;
//	}
//
//}

	public class MemberDAO {
	    
		public boolean idCheck(String id) {
		      String sql ="select id from member where id=?";
		      boolean isDupicate = false;
		      try (Connection conn = DB.getInstance();
		         PreparedStatement pstmt = conn.prepareStatement(sql)){
		         pstmt.setString(1, id);
		         try (ResultSet rs = pstmt.executeQuery()) {
		            isDupicate = rs.next();
		         }
		      }catch(Exception e) {
		         e.printStackTrace();
		      }
		      return isDupicate;
		   }
		
		public void memberInsert(MemberDTO dto) {
			Connection con = null;
			PreparedStatement pre = null;
			
			String sql = "insert into member(ID, PWD, NICKNAME, EMAIL) values (?,?,?,?)";
			
			try {
				con=DB.getInstance();
				pre= con.prepareStatement(sql);
				pre.setString(1, dto.getId());
				pre.setString(2, dto.getPwd());
				pre.setString(3, dto.getNickname());
				pre.setString(4, dto.getEmail());
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
		}
		
		public MemberDTO login(String id) {
			Connection con = null;
			PreparedStatement pre = null;
			ResultSet res = null;
			
			String sql = "select id,pwd,nickname,email,to_char(join_date,'YYYY-MM-DD')as join_date from member where id=?";
			
			MemberDTO dto = null;
			
			try {
				con = DB.getInstance();
				pre = con.prepareStatement(sql);
				pre.setString(1, id);
				
				res=pre.executeQuery();
				if(res.next()) {
					dto = new MemberDTO();
					dto.setId(res.getString("id"));
					dto.setEmail(res.getString("Email"));
					dto.setJoin_date(res.getString("Join_date"));
					dto.setNickname(res.getString("Nickname"));
					dto.setPwd(res.getString("pwd"));
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
		}
		
	} //MemberDAO
	
	
	
	
	
	
	
	
	
	
	
	
	
