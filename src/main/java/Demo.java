import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Demo {

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder().header("X-Yandex-API-Key", "712f8089-7b95-40cb-8088-c27ae8e18957")
                .header("Content-Type", "application/json;charset=UTF-8")
                .url("https://api.weather.yandex.ru/v2/forecast?lat=55.753215&lang=en_US&lon=37.622504&limit=5&hours=false")
                .build();


        Response response = client.newCall(request).execute();
        String body = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        List<WeatherResponse> weathList = new ArrayList<>();


        JsonNode arrNode = new ObjectMapper().readTree(body).get("forecasts");
        for (JsonNode objNode : arrNode) {

                WeatherResponse weatherResponse = new WeatherResponse();
                weatherResponse.setCity(objectMapper.readTree(body).at("/geo_object/locality/name").asText());
                weatherResponse.setWeatherText(objectMapper.writeValueAsString(objNode.at("/parts/day/condition")));
                weatherResponse.setDate(objectMapper.writeValueAsString(objNode.at("/date")));
                weatherResponse.setTemprature(objectMapper.writeValueAsString(objNode.at("/parts/day/temp_avg")));
                weathList.add(weatherResponse);
            }

            for (WeatherResponse weatherResponse : weathList) {
                System.out.println("CITY: " + weatherResponse.getCity());
                System.out.println("DATE: " + weatherResponse.getDate());
                System.out.println("WEATHER_TEXT: " + weatherResponse.getWeatherText());
                System.out.println("TEMPERATURE: " + weatherResponse.getTemprature() + "C");

        }
    }
}


