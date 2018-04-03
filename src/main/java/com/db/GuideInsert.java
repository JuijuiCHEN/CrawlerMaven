package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.crawler.guide.DbConfig;

public class GuideInsert {

	private static final String INSERT_STMT = "INSERT INTO guide(guide_id,mem_id,guide_title,guide_content,guide_read_size,guide_comm_size,guide_vote_size,guide_status,guide_area,guide_map)"
			+ "VALUES('G'||LPAD(to_char(GUIDE_PK_SEQ.NEXTVAL), 6, '0'),?,?,?,?,?,?,?,?,?)";

	private static final String SELECT_TITLE = "select DISTINCT GUIDE_TITLE, GUIDE_AREA  FROM GUIDE";

	private static final String SELECT_guide_id = "select guide_id  FROM GUIDE WHERE GUIDE_TITLE = ?";

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

}
