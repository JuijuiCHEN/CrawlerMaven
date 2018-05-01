package com.db;

import java.util.ArrayList;
import java.util.List;

import com.crawler.guide.BaiduImg;
import com.crawler.guide.ImageUtil;
import com.github.abola.crawler.CrawlerPack;

/**********************************/
/*      新增文章和圖片的爬蟲             */
/*     要別的的話需要改縣市和網址     */
/**********************************/
public class Guide {
	public static void addGuide(String detil) { // 需傳入字串為: "地區,網址"
		String[] arr = detil.split(","); // 逗號切割地區跟網址, 放到陣列裡
		String AREA = arr[0]; // 設定陣列第0個為AREA
		System.out.println(AREA);
		String url = arr[1]; // 設定陣列第1個為url
		ArrayList<org.jsoup.nodes.Element> a = CrawlerPack.start().getFromHtml(url).select("a.poiTitle"); // 開啟爬蟲,進入url,查詢a.poiTitle,
																											 // 爬到的a.poiTitle資料會回傳Elements物件,
																											 // 因我們必須用ArrayList接它,所以用父類別的ArrayList<Element>宣告型態
		for (int i = 0; i < a.size(); i++) { // 跑所有a.poiTitle
			String nextUrl = "http://www.tripadvisor.com.tw" + a.get(i).attr("href"); // 拿到單一景點網址
			String countryName = CrawlerPack.start().getFromHtml(nextUrl).select("div.detail_section")
					.select("span.country-name").text(); // 開啟爬蟲, 進入單一景點(nextUrl),
														 // 拿到div.detail_section裡span.country-name的文字,
														 // ex.台灣
			String locality = CrawlerPack.start().getFromHtml(nextUrl).select("div.detail_section")
					.select("span.locality").text(); // 開啟爬蟲, 進入單一景點(nextUrl),
													 // 拿到div.detail_section裡span.locality的文字, ex.台中
			String streetAddress = CrawlerPack.start().getFromHtml(nextUrl).select("div.detail_section")
					.select("span.street-address").text(); // 開啟爬蟲, 進入單一景點(nextUrl),
															 // 拿到div.detail_section裡span.street-address的文字,
															 // ex.武陵路3-1號
			String address = countryName + locality + streetAddress; // 串接上面文字為address,
																	 // ex.台灣台中武陵路3-1號
			System.out.println(address);

			String guideTitle = a.get(i).text(); // 第i個a.poiTitle的內容(就是標題)
			ArrayList<org.jsoup.nodes.Element> contentArr = CrawlerPack.start().getFromHtml(nextUrl)
					.select("p.partial_entry"); // 同20行意思
			for (int index = 0; index < contentArr.size(); index++) {
				System.out.println(index);
				String guideContent = contentArr.get(index).text().replaceAll("閱讀更多", ""); // 拿到一篇文章,
																							 // 並把"閱讀更多"這些字換成空字串
				if (guideContent.length() < 10) {
					continue; // 如果長度小於10, 直接跳出迴圈,進入for迴圈下一個數字
				}
				GuideInsert guideInsert = new GuideInsert();
				guideInsert.addGuide(guideTitle, guideContent, AREA, address);
				return; // 因為只要拿到一個標題一篇文章,所以新增一篇文章之後結束這個方法(addGuide)
			}
		}
	}

	public static void addDBimg() {
		GuideInsert guideInsert = new GuideInsert();
		List<String> titleList = guideInsert.SELECTTITLE(); // 拿到所有不重複的文章標題和該地區
		for (int i = 0; i < titleList.size(); i++) {
			String[] arr = titleList.get(i).split(","); // 切割拿到的標題和地區
			String title = arr[1]; // 標題
			String area = arr[0]; // 地區
			// 圖片list 30
			List<String> imgUrlList = BaiduImg.getPictures(area + title);// BaiduImg.getPictures是static可以直接調用方法,
																			// 傳入要搜尋的字串,會回傳圖片網址的list
			// 文章idㄉlist 3
			List<String> guIdlList = guideInsert.slGuideId(title); // 用標題查詢該指南id, 回傳list
			for (int j = 0; j < imgUrlList.size() - (imgUrlList.size() / 1.5); j++) {
				System.out.println(title + ":第 : " + j + " 張照片");
				System.out.println(imgUrlList.get(j));
				try {
					byte[] byteImg = ImageUtil.imgUrlgetByte(imgUrlList.get(j)); // 給一個url,
																				 // 回傳一個byte[]
					GuideInsertImg imgInsert = new GuideInsertImg();
					imgInsert.addGuideImg(guIdlList.get((j % guIdlList.size())), byteImg); // 新增圖片
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	public static void main(String[] args) {
		ArrayList<String> myList = new ArrayList<String>();
		myList.add("台中,https://www.tripadvisor.com.tw/Attractions-g297910-Activities-Taichung.html");
		myList.add("高雄,https://www.tripadvisor.com.tw/Attractions-g297908-Activities-Kaohsiung.html");
		myList.add("台北,https://www.tripadvisor.com.tw/Attractions-g293913-Activities-Taipei.html");
		myList.add("馬公,https://www.tripadvisor.com.tw/Attractions-g12420759-Activities-Magong_Penghu.html");
		myList.add("台東,https://www.tripadvisor.com.tw/Attractions-g13806910-Activities-Taitung_City_Taitung.html");
		myList.add("花蓮,https://www.tripadvisor.com.tw/Attractions-g13806634-Activities-Hualien_City_Hualien.html");
		myList.add("新北,https://www.tripadvisor.com.tw/Attractions-g1432365-Activities-New_Taipei.html");
		myList.add("彰化,https://www.tripadvisor.com.tw/Attractions-g13811256-Activities-Changhua_City_Changhua.html");
		myList.add("台南,https://www.tripadvisor.com.tw/Attractions-g293912-Activities-Tainan.html");
		myList.add("宜蘭,https://www.tripadvisor.com.tw/Attractions-g13808605-Activities-Yilan_City_Yilan.html");
		myList.add("屏東,https://www.tripadvisor.com.tw/Attractions-g13806783-Activities-Pingtung_City_Pingtung.html");
		myList.add("竹北,https://www.tripadvisor.com.tw/Attractions-g12200518-Activities-Zhubei_Hsinchu_County.html");
		myList.add("南投,https://www.tripadvisor.com.tw/Attractions-g13806757-Activities-Nantou_City_Nantou.html");
		myList.add("苗栗,https://www.tripadvisor.com.tw/Attractions-g13806741-Activities-Miaoli_City_Miaoli.html");
		myList.add("桃園,https://www.tripadvisor.com.tw/Attractions-g297912-Activities-Taoyuan.html");
		myList.add("員林,https://www.tripadvisor.com.tw/Attractions-g12421151-Activities-Yuanlin_Changhua.html");
		myList.add("斗六,https://www.tripadvisor.com.tw/Attractions-g13806490-Activities-Douliu_Yunlin.html");
		myList.add("新竹,https://www.tripadvisor.com.tw/Attractions-g297906-Activities-Hsinchu.html");
		for (int i = 0; i < myList.size(); i++) {
			Comm.addComm(myList.get(i));
			// addGuide(myList.get(i));
		}
		// addDBimg();
	}
}
