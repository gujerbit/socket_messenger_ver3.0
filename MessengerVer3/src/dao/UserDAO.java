package dao;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import util.JDBCUtil;

public class UserDAO {

	private static UserDAO instance = new UserDAO();
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private byte[] salt = null;
	
	public static UserDAO getInstance() {
		return instance;
	}
	
	private byte[] createSalt() {
		String temp = "";
		
		for(int i = 0; i < 20; i++) {
			Random rnd = new Random();
			String rndChar = String.valueOf((char)((int) rnd.nextInt(26) + 97));
			temp += rndChar;
		}
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(temp.getBytes());
			salt = md.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return salt;
	}
	
	public String hashing(String password) {
		byte[] hashPassword = null;
		byte[] saltingPassword = null;
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(password.getBytes());
			hashPassword = md.digest();
			
			String temp = byteToString(hashPassword) + byteToString(salt);
			md.update(temp.getBytes());
			saltingPassword = md.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return byteToString(saltingPassword);
	}
	
	public int register(String id, String password, String name) {
		int result = -1;
		String sql = "INSERT INTO member VALUES(?, ?, ?, ?)";
		
		try {
			createSalt();
			con = JDBCUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, byteToString(salt));
			pstmt.setString(3, hashing(password));
			pstmt.setString(4, name);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(con, pstmt, rs);
		}
		
		return result;
	}
	
	public String byteToString(byte[] password) {
		StringBuffer sb = new StringBuffer();
		
		for(byte value : password) {
			sb.append(String.format("%02x", value));
		}
		
		return sb.toString();
	}
	
}
