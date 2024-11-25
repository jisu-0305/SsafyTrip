package com.trip.weather.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {
    @Value("${service.key}")
    private String serviceKey;

    public String getWeatherForecast(double latitude, double longitude) {
        String response = "";
        try {
            // 위도/경도를 nx, ny로 변환하는 로직 (임시로 고정된 값 사용)
            int[] grid = convertLatLonToGrid(latitude, longitude);
            String nx = String.valueOf(grid[0]);
            String ny = String.valueOf(grid[1]);
            System.out.println("nx: "+nx+", ny: "+ny);
            String pageUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

            // 공공데이터포털 API 요청 URL 생성
            StringBuilder urlBuilder = new StringBuilder(pageUrl);
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" +  URLEncoder.encode(serviceKey, "UTF-8")); // Service Key
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); // 페이지 번호
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); // 한 페이지 결과 수
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); // 응답 형식
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(getTodayDate(), "UTF-8")); // 기준 날짜
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode("0200", "UTF-8")); // 기준 시간
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); // X 좌표
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); // Y 좌표

            // HTTP 연결
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            // 응답 코드 출력
            System.out.println("Response code: " + conn.getResponseCode());

            // 응답 데이터 읽기
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            response = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            response = "Error: " + e.getMessage();
        }
        return response;
    }

    private String getTodayDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return today.format(formatter);
    }


    private int[] convertLatLonToGrid(double lat, double lon) {
        final double RE = 6371.00877; // 지구 반경(km)
        final double GRID = 5.0; // 격자 간격(km)
        final double SLAT1 = 30.0; // 투영 위도1(degree)
        final double SLAT2 = 60.0; // 투영 위도2(degree)
        final double OLON = 126.0; // 기준점 경도(degree)
        final double OLAT = 38.0; // 기준점 위도(degree)
        final double XO = 43; // 기준점 X 좌표 (격자)
        final double YO = 136; // 기준점 Y 좌표 (격자)

        double DEGRAD = Math.PI / 180.0;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        double ra = Math.tan(Math.PI * 0.25 + lat * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = lon * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;

        int x = (int) Math.floor(ra * Math.sin(theta) + XO + 0.5);
        int y = (int) Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        return new int[]{x, y};
    }
}
