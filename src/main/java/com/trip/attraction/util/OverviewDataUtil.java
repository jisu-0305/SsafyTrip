package com.trip.attraction.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.mypg.dto.ApiDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OverviewDataUtil {
    public static String getOverview(String contentId, String contentTypeId){
        String result="";
        HttpURLConnection conn = null;
        
        try {
            // url
            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B551011/KorService1/detailInfo1");
            urlBuilder.append("?" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("WIN", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("test", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(contentId, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode(contentTypeId, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8")+ "=" + URLEncoder.encode("3MeOfwmipzVM6lTM3AY/EJDcig4NoENXlzhqBrF5EH0+T1uT0vaQjh3l8axfWUW2b9AINhom/jP0GVGUW7N8bQ==", "UTF-8"));

            

            // connect
            URL url = new URL(urlBuilder.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");



            // 응답 데이터
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line+"\n");
            }

            // 매퍼를 통해 필요한 데이터만 구하기
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonResponse = objectMapper.readValue(sb.toString(), new TypeReference<Map<String, Object>>() {});
            Map<String, Object> body = (Map<String, Object>) ((Map<String, Object>) jsonResponse.get("response")).get("body");
            Map<String, Object> items = (Map<String, Object>) body.get("items");
            List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");


            // overview에 저장할 데이터 추출
            List<ApiDTO> tourismInfoList = new ArrayList<>();
            for (Map<String, Object> item : itemList) {
                String infoname = (String) item.get("infoname");
                String infotext = (String) item.get("infotext");
                tourismInfoList.add(new ApiDTO(infoname, infotext));
            }

            // overview에 데이터 저장하기
            for(ApiDTO apiDTO : tourismInfoList){
//                System.out.println(apiDTO.getTitle() + ":" + apiDTO.getContent());
                result += apiDTO.getTitle() + ": " + apiDTO.getContent() + "\n";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }

        return result;
    }
}
