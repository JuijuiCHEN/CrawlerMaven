package com.crawler.guide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

// 給一個keyword(要搜尋的字串), 從百度查圖片, 回傳圖片網址list
public class BaiduImg {
	public static List<String> getPictures(String keyword) {
		String url = "http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=" + keyword
				+ "&cg=star&pn=30&rn=30&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1";
		List<String> list = new ArrayList<String>();
		Document document;
		try {
			document = Jsoup.connect(url).data("query", "Java")
					.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)").timeout(5000).get(); // (從後到前)模擬post請求拿資料,
																														 // 如果連線和讀圖片時間超過5秒就拋出異常,
																														 // 假裝自己是瀏覽器去訪問,提交from表單,
																														 // 拿到url連線,,,,,總之就是連線進到url網頁裡
			String xmlSource = document.toString(); // 把document轉成字串
			xmlSource = StringEscapeUtils.unescapeHtml(xmlSource); // 把html轉碼成java看得懂的
			String reg = "objURL\":\"http://.+?\\.jpg"; // 正則表示法.+?\\(只要符合左邊和右邊中間隨便),抓出符合正則表示法的字串
			Pattern pattern = Pattern.compile(reg); // 做出一個正則表示法的物件

			Matcher m = pattern.matcher(xmlSource); // 調用把pattern的matcher方法,傳入xmlSource(網頁內容),會回傳Matcher
			while (m.find()) { // find()下一個的意思
				String finalURL = m.group().substring(9); // 拿到圖片網址(需把前8個字去除 objURL":")
				// System.out.println("m.group(): " + m.group());
				list.add(finalURL); // 把正確網址都放進list
			}

		} catch (IOException e) {
		}
		return list;
	}

	public static void main(String[] args) {
		// 圖片list 30
		List<String> imgUrlList = BaiduImg.getPictures("老梅綠石槽");
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
