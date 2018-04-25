package com.crawler.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.util.HttpUtils;

public class AddToLet {
	public static final String KEY = "AIzaSyBuS1-gsXN4HrHQaqjuaFaMxIqcayPPaoY";
	public static final String GOOGLEAPI = "https://maps.googleapis.com/maps/api/geocode/json";

	public static String googleApi(String add) throws Exception {
		String letJson = HttpUtils.sendGet(GOOGLEAPI + "?address=" + add + "&key=" + KEY);
		JSONObject j = JSON.parseObject(letJson);
		String let = JSON.parseObject(j.getJSONArray("results").get(0).toString()).getJSONObject("geometry")
				.get("location").toString();
		return let;
	}

	public static void main(String[] args) {
		// try {
		// GuideInsert dao = new GuideInsert();
		// List<Map> list = dao.SELECT_ADD();
		// for (int i = 0; i < list.size(); i++) {
		// String guideId = list.get(i).get("GUIDE_ID").toString();
		// String let = AddToLet.googleApi(list.get(i).get("GUIDE_MAP").toString());
		// System.out.println("單號 : " + guideId);
		// System.out.println("地址 : " + list.get(i).get("GUIDE_MAP"));
		// System.out.println(let);
		// dao.update_LAT_LNG(let, guideId);
		// TimeUnit.SECONDS.sleep(1);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		String let;
		try {
			let = AddToLet.googleApi("台中");
			System.out.println(let);
		} catch (Exception e) {
		}
	}

}
