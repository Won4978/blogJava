package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DB;

public class LikeDAO {
	
	public boolean isLiked(int bno, String memberId) {
		
		String sql ="select 1 from board_like where bno=? and member_id=?";
		
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			pre.setInt(1, bno);
			pre.setString(2, memberId);
			
			try(ResultSet res = pre.executeQuery()){
				return res.next();
			}		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;	
	}

	public void toggle(int bno, String memberId) {
		String checkSql = "select 1 from board_like where bno=? and member_id=?";		
		String insertSql = "insert into board_like (bno,member_id) values (?,?)";
		String deleteSql = "delete from board_like where bno=? and member_id=?";
		String plusSql = "update board set like_cnt=like_cnt+1 where bno=?";
		String minusSql = "update board set like_cnt=like_cnt-1 where bno=?";
		
		try(Connection con = DB.getInstance()){
			con.setAutoCommit(false);
			try {
				//  1)눌려져 있는지 확인
				boolean exists;
				try(PreparedStatement pre = con.prepareStatement(checkSql)){
					pre.setInt(1, bno);
					pre.setString(2, memberId);
					try(ResultSet res = pre.executeQuery()){
						exists = res.next();
					}
				}
				// 2) 눌렀으면 delete+갯수 -1 , 안 눌렀으면 insert+갯수 +1
				try(PreparedStatement pre =con.prepareStatement(exists ? deleteSql: insertSql)){
					pre.setInt(1, bno);
					pre.setString(2, memberId);
					pre.executeUpdate();
				}
				try(PreparedStatement pre =con.prepareStatement(exists ? minusSql : plusSql)){
					pre.setInt(1, bno);
					pre.executeUpdate();
				}
				con.commit();	// 위에 try두개가 다 성공해야만 실행
			}catch(SQLException e) {
				con.rollback();	// 하나라도 실패하면 실행
				throw e;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	} // toggle
	
	// 현재 좋아요 갯수 조회
	
	public int selectLikeCnt(int bno) {
		
		String sql = "select like_cnt from board where bno=?";
		
		try(Connection con = DB.getInstance();
			PreparedStatement pre = con.prepareStatement(sql)){
			
			pre .setInt(1, bno);
			try(ResultSet res = pre.executeQuery()){
				if(res.next()) {
					return res.getInt("like_cnt");
				}
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
