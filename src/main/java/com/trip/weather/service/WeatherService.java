package com.trip.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.weather.dto.WeatherRequestDto;
import com.trip.weather.dto.WeatherResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    @Value("${service.key}")
    private String serviceKey;

    public List<WeatherResponseDto> getWeatherForecast(List<WeatherRequestDto> weatherRequests) {
        List<WeatherResponseDto> responseList = new ArrayList<>();

        try {
            for (WeatherRequestDto request : weatherRequests) {
                double latitude = request.getLatitude();
                double longitude = request.getLongitude();
                String date = request.getDate();

                // 위도와 경도를 격자 좌표로 변환
                int[] grid = convertLatLonToGrid(latitude, longitude);
                String nx = String.valueOf(grid[0]);
                String ny = String.valueOf(grid[1]);

                // API 호출
                String response = fetchWeatherData(nx, ny, date);

                // JSON 응답 파싱
                List<WeatherResponseDto> parsedData = parseWeatherResponse(response, date);

                // 결과 리스트에 추가
                if (!parsedData.isEmpty()) {
                    responseList.addAll(parsedData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseList;
    }

    private String fetchWeatherData(String nx, String ny, String baseDate) throws Exception {
        String pageUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        StringBuilder urlBuilder = new StringBuilder(pageUrl);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode("0200", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

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
        return sb.toString();
    }
    private List<WeatherResponseDto> parseWeatherResponse(String response, String date) {
        List<WeatherResponseDto> weatherList = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            JsonNode items = rootNode.path("response").path("body").path("items").path("item");

            String weather = null;
            String temperature = null;

            for (JsonNode item : items) {
                String category = item.path("category").asText();
                String fcstValue = item.path("fcstValue").asText();
                String fcstDate = item.path("fcstDate").asText();
                String fcstTime = item.path("fcstTime").asText();

                // 오전 6시 (0600) 데이터만 선택
                if (fcstDate.equals(date) && fcstTime.equals("0600")) {
                    if (category.equals("TMP")) { // 기온 데이터
                        temperature = fcstValue + "°C";
                    } else if (category.equals("SKY")) { // 하늘 상태 데이터
                        weather = interpretSkyCondition(fcstValue);
                    }
                }
            }

            // 모든 데이터를 처리한 후, 결과가 있다면 추가
            if (weather != null && temperature != null) {
                weatherList.add(new WeatherResponseDto(date, weather, temperature));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return weatherList;
    }

    private String interpretSkyCondition(String value) {
        switch (value) {
            case "1":
                return "맑음";
            case "3":
                return "구름 많음";
            case "4":
                return "흐림";
            default:
                return "알 수 없음";
        }
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
