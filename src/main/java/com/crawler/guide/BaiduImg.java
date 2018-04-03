package com.crawler.guide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class BaiduImg {
	public static List<String> getPictures(String keyword) {
		String url = "http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=" + keyword
				+ "&cg=star&pn=30&rn=30&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=30";
		List<String> list = new ArrayList<String>();
		Document document;
		try {
			document = Jsoup.connect(url).data("query", "Java")
					.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)").timeout(5000).get();
			String xmlSource = document.toString();
			xmlSource = StringEscapeUtils.unescapeHtml(xmlSource);

			String reg = "objURL\":\"http://.+?\\.jpg";
			Pattern pattern = Pattern.compile(reg);

			Matcher m = pattern.matcher(xmlSource);
			while (m.find()) {
				String finalURL = m.group().substring(9);
				list.add(finalURL);
			}

		} catch (IOException e) {
		}
		return list;
	}

	public static void main(String[] args) {
		// 圖片list 30
		List<String> imgUrlList = BaiduImg.getPictures("斗六");
		for (int j = 0; j < imgUrlList.size(); j++) {
			System.out.println(imgUrlList.get(j));
			try {
				byte[] byteImg = ImageUtil.imgUrlgetByte(imgUrlList.get(j));
				System.out.println(byteImg.length);
				if (byteImg.length == 0) {
					System.out.println("11111111111111111111111111111111111111111111111111111111111111111");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
