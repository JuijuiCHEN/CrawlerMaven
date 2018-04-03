package com.crawler.guide;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ImageUtil {

	public static byte[] imgUrlgetByte(String imgUrl) throws Exception {
		BufferedInputStream in = null;
		ByteArrayOutputStream out = null;
		URLConnection con = null;
		try { // 打開URL串流
			URL url = new URL(imgUrl);
			con = url.openConnection();
			// 打開InputStream串流
			in = new BufferedInputStream(con.getInputStream());
			byte[] guideImgContent = new byte[in.available()];
			out = new ByteArrayOutputStream();
			int readByte = 0;
			// 假如等於-1代表沒有資料了
			while ((readByte = in.read(guideImgContent)) != -1) {
				// 從緩衝區讀取buffer裡面0~length-1的位置
				out.write(guideImgContent, 0, readByte);
				if (readByte == 0) {
					break;
				}
			}
			byte[] response = out.toByteArray();
			System.out.println("完成輸出");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}

		}
	}

	public static void main(String[] args) {

		String imgUrl = "http://p1.lvpingphoto.com/images/phs/201303/13/jupiter/eb318bc4d9434ca484995ef1edc8d551.jpg";
		try {
			byte[] imgByte = ImageUtil.imgUrlgetByte(imgUrl);
			System.out.println(new String(imgByte, "utf-8"));
		} catch (Exception e) {
		}
	}

}
