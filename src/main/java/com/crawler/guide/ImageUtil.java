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
		try {
			URL url = new URL(imgUrl); // 打開URL串流
			con = url.openConnection();
			con.setConnectTimeout(15000);
			con.setReadTimeout(15000);
			// 打開InputStream串流
			in = new BufferedInputStream(con.getInputStream()); // BufferedInputStream的size為8192,超過就會跑2次.3次.4次...
			System.out.println("-------------------" + in.available());
			byte[] guideImgContent = new byte[in.available()]; // new in大小的byte[]
			out = new ByteArrayOutputStream();
			int readByte = 0;
			// 假如等於-1代表沒有資料了
			while ((readByte = in.read(guideImgContent)) != -1) { // 怕imgUrl超過byte大小所以需跑迴圈到最後一個
				out.write(guideImgContent, 0, readByte); // 從緩衝區讀完buffer裡面的位置
				System.out.println(readByte);
				if (readByte == 0) {
					break;
				}
			}
			byte[] response = out.toByteArray(); // 把ByteArrayOutputStream轉成byte[]
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

		String imgUrl = "http://jd.asean168.com/uploadfile/2015/0519/20150519025705769.jpg";
		try {
			byte[] imgByte = ImageUtil.imgUrlgetByte(imgUrl);
			// System.out.println(new String(imgByte, "utf-8"));
		} catch (Exception e) {
		}
	}

}
