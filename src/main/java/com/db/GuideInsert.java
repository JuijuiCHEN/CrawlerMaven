package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.crawler.guide.DbConfig;

public class GuideInsert {

	private static final String INSERT_STMT = "INSERT INTO guide(guide_id,mem_id,guide_title,guide_content,guide_read_size,guide_comm_size,guide_vote_size,guide_status,guide_area,guide_map)"
			+ "VALUES('G'||LPAD(to_char(GUIDE_PK_SEQ.NEXTVAL), 6, '0'),?,?,?,?,?,?,?,?,?)";

	private static final String SELECT_TITLE = "select DISTINCT GUIDE_TITLE, GUIDE_AREA  FROM GUIDE";
	private static final String SELECT_ADD = "select GUIDE_ID,GUIDE_MAP  FROM GUIDE WHERE GUIDE_LAT_LNG IS NULL";

	private static final String SELECT_guide_id = "select guide_id  FROM GUIDE WHERE GUIDE_TITLE = ?";

	private static final String UPDATE_GUIDE_LAT_LNG = "UPDATE guide set GUIDE_LAT_LNG=? where guide_id=?";
	// 以下為留言
	private static final String INSERT_COMM = "INSERT INTO guide_comm(comm_id,guide_id,mem_id,commnet_content,comm_status) VALUES('GC'||LPAD(to_char(GUIDE_PK_SEQ.NEXTVAL), 6, '0'),?,?,?,?)";
	private static final String SELECT_GUIDE_COMM_SIZE = "SELECT GUIDE_COMM_SIZE FROM GUIDE WHERE GUIDE_ID=?";
	private static final String UPDATE_GUIDE_COMM_SIZE = "UPDATE guide set GUIDE_COMM_SIZE=? where guide_id = ?";

	public List<Integer> selectGuideComm(String guideId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Integer> list = new ArrayList<Integer>();
		try {
			Class.forName(DbConfig.DRIVER);
			con = DriverManager.getConnection(DbConfig.URL, DbConfig.USER, DbConfig.PASSWORD);
			pstmt = con.prepareStatement(SELECT_GUIDE_COMM_SIZE);
			pstmt.setString(1, guideId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getInt("GUIDE_COMM_SIZE"));
			}
			// Handle any SQL errors
		} catch (Exception se) {
			throw new RuntimeException("資料庫錯誤" + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public void insertComm(String guideId, String commContent) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(DbConfig.DRIVER);
			con = DriverManager.getConnection(DbConfig.URL, DbConfig.USER, DbConfig.PASSWORD);
			pstmt = con.prepareStatement(INSERT_COMM);
			String memId = "M00000" + (int) ((Math.random() * 9) + 1);
			System.out.println(memId);
			pstmt.setString(1, guideId);
			pstmt.setString(2, memId);
			pstmt.setString(3, commContent);
			pstmt.setInt(4, 2);
			pstmt.executeUpdate();
			pstmt = con.prepareStatement("Update guide set GUIDE_COMM_SIZE = GUIDE_COMM_SIZE+1 where GUIDE_ID=?");
			pstmt.setString(1, guideId);
			pstmt.executeUpdate();
			// Handle any SQL errors
		} catch (Exception se) {
			throw new RuntimeException("錯誤" + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	public List<Map> SELECT_ADD() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Map> list = new ArrayList<Map>();
		try {
			Class.forName(DbConfig.DRIVER);
			con = DriverManager.getConnection(DbConfig.URL, DbConfig.USER, DbConfig.PASSWORD);
			pstmt = con.prepareStatement(SELECT_ADD);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Map map = new TreeMap<String, String>();
				map.put("GUIDE_ID", rs.getString("GUIDE_ID"));
				map.put("GUIDE_MAP", rs.getString("GUIDE_MAP"));
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					System.out.println(se);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException se) {
					System.out.println(se);
				}
			}
		}
	}

	public List<String> slGuideId(String title) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			Class.forName(DbConfig.DRIVER);
			con = DriverManager.getConnection(DbConfig.URL, DbConfig.USER, DbConfig.PASSWORD);
			pstmt = con.prepareStatement(SELECT_guide_id);
			pstmt.setString(1, title);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("guide_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<String> SELECTTITLE() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			Class.forName(DbConfig.DRIVER);
			con = DriverManager.getConnection(DbConfig.URL, DbConfig.USER, DbConfig.PASSWORD);
			pstmt = con.prepareStatement(SELECT_TITLE);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("GUIDE_AREA") + "," + rs.getString("GUIDE_TITLE"));
			}
		} catch (ClassNotFoundException ce) {
			System.out.println(ce);
		} catch (SQLException se) {
			System.out.println(se);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					System.out.println(se);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException se) {
					System.out.println(se);
				}
			}
		}
		return list;
	}

	public void addGuide(String guideTitle, String guideContent, String area, String address) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(DbConfig.DRIVER);
			con = DriverManager.getConnection(DbConfig.URL, DbConfig.USER, DbConfig.PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);
			String menId = "M00000" + (int) ((Math.random() * 9) + 1);
			System.out.println(menId);
			pstmt.setString(1, menId);
			pstmt.setString(2, guideTitle);
			pstmt.setString(3, guideContent);
			pstmt.setInt(4, (int) ((Math.random() * 2018) + 1));
			pstmt.setInt(5, 0);
			pstmt.setInt(6, (int) ((Math.random() * 200) + 1));
			pstmt.setInt(7, 2);
			pstmt.setString(8, area);
			pstmt.setString(9, address);

			pstmt.executeUpdate();

		} catch (ClassNotFoundException ce) {
			System.out.println(ce);
		} catch (SQLException se) {
			System.out.println(se);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					System.out.println(se);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException se) {
					System.out.println(se);
				}
			}
		}
	}

	public void update_LAT_LNG(String guideLatLng, String guideId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(DbConfig.DRIVER);
			con = DriverManager.getConnection(DbConfig.URL, DbConfig.USER, DbConfig.PASSWORD);
			pstmt = con.prepareStatement(UPDATE_GUIDE_LAT_LNG);
			pstmt.setString(1, guideLatLng);
			pstmt.setString(2, guideId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					System.out.println(se);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException se) {
					System.out.println(se);
				}
			}
		}

	}
}
