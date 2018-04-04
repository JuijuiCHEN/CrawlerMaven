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
	public static void addGuide(String detil) {
		String[] arr = detil.split(",");
		String AREA = arr[0];
		System.out.println(AREA);
		String uri = arr[1];
		ArrayList<org.jsoup.nodes.Element> a = CrawlerPack.start().getFromHtml(uri).select("a.poiTitle");
		for (int i = 0; i < a.size(); i++) {
			String nextUrl = "http://www.tripadvisor.com.tw" + a.get(i).attr("href");
			String countryName = CrawlerPack.start().getFromHtml(nextUrl).select("div.detail_section")
					.select("span.country-name").text();
			String locality = CrawlerPack.start().getFromHtml(nextUrl).select("div.detail_section")
					.select("span.locality").text();
			String streetAddress = CrawlerPack.start().getFromHtml(nextUrl).select("div.detail_section")
					.select("span.street-address").text();
			String address = countryName + locality + streetAddress;
			System.out.println(address);
			ArrayList<org.jsoup.nodes.Element> contentArr = CrawlerPack.start().getFromHtml(nextUrl)
					.select("p.partial_entry");
			for (int index = 0; index < contentArr.size(); index++) {
				System.out.println(index);
				String guideTitle = a.get(i).text();
				String guideContent = contentArr.get(index).text().replaceAll("閱讀更多", "");
				if (guideContent.length() < 10) {
					continue;
				}
				GuideInsert guideInsert = new GuideInsert();
				guideInsert.addGuide(guideTitle, guideContent, AREA, address);
				return;
			}
		}
	}

	public static void addDBimg() {
		GuideInsert guideInsert = new GuideInsert();
		List<String> titleList = guideInsert.SELECTTITLE();
		for (int i = 0; i < titleList.size(); i++) {
			String[] arr = titleList.get(i).split(",");
			String title = arr[1];
			String area = arr[0];
			// 圖片list 30
			List<String> imgUrlList = BaiduImg.getPictures(area + title);
			// 文章idㄉlist 3
			List<String> guIdlList = guideInsert.slGuideId(title);
			for (int j = 0; j < imgUrlList.size() - (imgUrlList.size() / 1.5); j++) {
				System.out.println(title + ":第 : " + j + " 張照片");
				System.out.println(imgUrlList.get(j));
				try {
					byte[] byteImg = ImageUtil.imgUrlgetByte(imgUrlList.get(j));
					GuideInsertImg imgInsert = new GuideInsertImg();
					imgInsert.addGuideImg(guIdlList.get((j % guIdlList.size())), byteImg);
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
		// for (int i = 0; i < myList.size(); i++) {
		// addGuide(myList.get(i));
		// }
		addDBimg();
	}
}
