package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
	
	public static Connection getInstance() {
		Connection con =null;
		String driver= "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String id = "ai2gi";
		String pw = "1234";
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,id,pw);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	

}
