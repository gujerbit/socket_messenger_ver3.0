package dao;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import util.JDBCUtil;
import vo.UserVO;

public class UserDAO {

	private static UserDAO instance = new UserDAO();
	private String salt = null;
			
	public static UserDAO getInstance() { //싱글톤
		return instance;
	}
	
	public boolean duplicateCheck(String id) {
		String sql = "SELECT COUNT(id) FROM member WHERE id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;
		
		try {
			con = JDBCUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cnt = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(con, pstmt, rs);
		}
		
		if(cnt > 0) {
			return false;
		} else {
			return true;
		}
	}
	
	private String createSalt() { //무작위 salt값 생성
		String temp = "";
		byte[] temp2 = null;
		
		for(int i = 0; i < 20; i++) {
			Random rnd = new Random();
			String rndChar = String.valueOf((char)((int) rnd.nextInt(26) + 97));
			temp += rndChar;
		}
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(temp.getBytes());
			temp2 = md.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		salt = byteToString(temp2);
		
		return salt;
	}
	
	public String hashing(String password) { //비밀번호 + salt값을 합쳐 해쉬
		byte[] hashPassword = null;
		byte[] saltingPassword = null;
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(password.getBytes());
			hashPassword = md.digest();
			
			String temp = byteToString(hashPassword) + salt;
			md.update(temp.getBytes());
			saltingPassword = md.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return byteToString(saltingPassword);
	}
	
	public String byteToString(byte[] password) { //byte형을 string형으로 변환
		StringBuffer sb = new StringBuffer();
		
		for(byte value : password) {
			sb.append(String.format("%02x", value));
		}
		
		return sb.toString();
	}
	
	public int register(String id, String password, String name) { //회원가입
		int result = -1;
		String sql = "INSERT INTO member VALUES(?, ?, ?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = JDBCUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, createSalt());
			pstmt.setString(3, hashing(password));
			pstmt.setString(4, name);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(con, pstmt, null);
		}
		
		return result;
	}
	
	public void getSalt(String id) {
		String sql = "SELECT salt FROM member WHERE id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = JDBCUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) salt = rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(con, pstmt, rs);
		}
	}
	
	public UserVO login(String id, String password) {
		String sql = "SELECT id, name FROM member WHERE id = ? AND password = ?";
		UserVO vo = new UserVO();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = JDBCUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			getSalt(id);
			pstmt.setString(1, id);
			pstmt.setString(2, hashing(password));
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				vo.setId(rs.getString(1));
				vo.setName(rs.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(con, pstmt, rs);
		}
		
		return vo;
	}
	
}
