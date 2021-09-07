package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.JDBCUtil;

public class RoomDAO {
	
	private static RoomDAO instance = new RoomDAO();
	
	public static RoomDAO getInstance() {
		return instance;
	}
	
	public int recordMessage(String roomId, String id, String message) {
		String sql = "INSERT INTO room VALUES(?, ?, ?)";
		int result = -1;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = JDBCUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, roomId);
			pstmt.setString(2, id);
			pstmt.setString(3, message);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(con, pstmt, null);
		}
		
		return result;
	}
	
	public int connectUser(String roomId, String id) {
		String sql = "INSERT INTO connect VALUES(?, ?)";
		int result = -1;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = JDBCUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, roomId);
			pstmt.setString(2, id);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(con, pstmt, null);
		}
		
		return result;
	}
	
	public int disconnectUser(String roomId, String id) {
		String sql = "DELETE FROM connect WHERE room = ? AND id = ?";
		int result = -1;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = JDBCUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, roomId);
			pstmt.setString(2, id);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(con, pstmt, null);
		}
		
		return result;
	}
	
	public boolean checkUser(String roomId, String id) {
		String sql = "SELECT COUNT(id) FROM connect WHERE room = ? AND id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;
		
		try {
			con = JDBCUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, roomId);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				cnt = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(con, pstmt, rs);
		}
		System.out.println(cnt);
		if(cnt > 0) {
			return false;
		} else {
			return true;
		}
	}

}
