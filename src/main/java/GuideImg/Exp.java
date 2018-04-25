package GuideImg;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.crawler.guide.DbConfig;

public class Exp {
	// 拿到所有圖片DAO
	private static List<ArrayList<Object>> getAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ArrayList<Object>> imgList = new ArrayList<ArrayList<Object>>();
		try {
			Class.forName(DbConfig.DRIVER);
			con = DriverManager.getConnection(DbConfig.URL, DbConfig.USER, DbConfig.PASSWORD);
			pstmt = con.prepareStatement("SELECT * FROM gd_img");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ArrayList<Object> dtlList = new ArrayList<Object>();
				System.out.println(rs.getString("guide_id"));
				dtlList.add(rs.getString("guide_id"));
				dtlList.add(rs.getBytes("guide_img_content"));
				imgList.add(dtlList);
			}
		} catch (Exception se) {
			throw new RuntimeException("資料庫錯誤" + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
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
		return imgList;
	}

	public static void main(String[] args) {
		List<ArrayList<Object>> imgList = getAll(); // 查詢所有文章id和圖片, 放到imgList
		System.out.println("總共" + imgList.size());
		for (int i = 0; i < imgList.size(); i++) { // 跑imgList全部資料
			System.out.println("i:" + i);
			try {
				String guideId = (String) imgList.get(i).get(0); // 拿到imgList第i個的第0個值(guideId),
				byte[] bytes1 = (byte[]) imgList.get(i).get(1); // 同上以此類推
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes1); // 建立緩衝區
				BufferedImage bi1 = ImageIO.read(bais); // 將圖片讀到bi1裡, ImageIO會返回BufferedImage
				File file = new File("D:\\專題圖片\\" + guideId + "COUNT" + i + ".jpg");// 設定名稱
				ImageIO.write(bi1, "jpg", file); // 不管輸出什麼格式圖片，此處不需改動
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("整個完畢");
	}
}
