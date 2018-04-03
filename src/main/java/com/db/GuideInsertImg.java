package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.crawler.guide.DbConfig;

public class GuideInsertImg {

	private static final String INSERT_STMT = "INSERT INTO gd_img(g_img_id,guide_id,guide_img_content)"
			+ "VALUES('GI'||LPAD(to_char(GUIDE_PK_SEQ.NEXTVAL), 6, '0'),?,?)";

	public void addGuideImg(String guideId, byte[] guideImgContent) {
		if (guideImgContent == null || guideImgContent.length < 10) {
			return;
		}
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(DbConfig.DRIVER);
			con = DriverManager.getConnection(DbConfig.URL, DbConfig.USER, DbConfig.PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, guideId);
			pstmt.setBytes(2, guideImgContent);
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
