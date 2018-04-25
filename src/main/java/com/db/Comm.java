package com.db;

import java.util.ArrayList;
import java.util.List;

import com.github.abola.crawler.CrawlerPack;

public class Comm {

	public static void addComm(String detil) {
		String[] arr = detil.split(","); // 以分號切割
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
			for (int index = 1; index < contentArr.size(); index++) {
				System.out.println(index);
				String guideTitle = a.get(i).text();
				String commContent = contentArr.get(index).text().replaceAll("閱讀更多", "").replaceAll("更多", "");
				if (commContent.length() < 10) {
					continue;
				}
				// 新增留言
				GuideInsert guideInsert = new GuideInsert();
				List<String> idList = guideInsert.slGuideId(guideTitle);
				for (int j = 0; j < idList.size(); j++) {
					guideInsert.insertComm(idList.get(j), commContent);
				}

			}
		}
	}

}
