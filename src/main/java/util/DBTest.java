package util;

import java.sql.Connection;

public class DBTest {

	public static void main(String[] args) {
		try {
			Connection con = DB.getInstance();
			if(con!=null) {
				System.out.println("성공");
			}else {
				System.out.println("실패");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}
