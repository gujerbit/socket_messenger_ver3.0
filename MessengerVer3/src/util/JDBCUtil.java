package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCUtil {
	
	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jun?useSSL=false", "jun", "elsh1122!");
		
		return con;
	}
	
	public static void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
		if(rs != null) { try { rs.close(); } catch(Exception e) { e.printStackTrace(); } }
		if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { e.printStackTrace(); } }
		if(con != null) { try { con.close(); } catch(Exception e) { e.printStackTrace(); } }
	}
	
}
