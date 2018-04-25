package GuideImg;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.imageio.ImageIO;

public class Imp {
	public static final String DRIVER2 = "oracle.jdbc.driver.OracleDriver";
	public static final String URL2 = "jdbc:oracle:thin:@localhost:1521:xe";
	public static final String USER2 = "AA";
	public static final String PASSWORD2 = "AA";

	// 新增圖片DAO
	private static void insert(String guideId, byte[] guideImg) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(DRIVER2);
			con = DriverManager.getConnection(URL2, USER2, PASSWORD2);
			pstmt = con.prepareStatement(
					"INSERT INTO gd_img(g_img_id,guide_id,guide_img_content)VALUES('GI'||LPAD(to_char(GUIDE_PK_SEQ.NEXTVAL), 6, '0'),?,?)");
			pstmt.setString(1, guideId);
			pstmt.setBytes(2, guideImg);
			pstmt.executeUpdate();
		} catch (Exception se) {
			throw new RuntimeException("資料庫錯誤" + se.getMessage());
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

	public static void main(String[] args) {
		try {
			File file = new File("D:\\專題圖片");
			File[] f = file.listFiles(); // 讀取資料夾全部檔案放到陣列裡, 陣列存放檔案路徑與檔名
			File oneFile;
			for (int i = 0; i < f.length; i++) { // 跑所有檔案
				// System.out.println("檔案位置: " + f[i]);
				oneFile = (f[i]); // 設定一個檔
				System.out.println("檔案名稱: " + f[i].getName());
				BufferedImage originalImage = ImageIO.read(oneFile); // 將圖片讀到bi1裡,
																	 // ImageIO會返回BufferedImage
				String guideId = oneFile.getName().substring(0, 7); // 拿到檔名前6個字, 設定為ID
				System.out.println(guideId);
				ByteArrayOutputStream baos = new ByteArrayOutputStream(); // 建立緩衝區
				ImageIO.write(originalImage, "jpg", baos); // 不管輸出什麼格式圖片，此處不需改動
				baos.flush();
				byte[] imageInByte = baos.toByteArray(); // 輸出byte
				baos.close();
				insert(guideId, imageInByte);
			}
			System.out.println("新增完畢");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
