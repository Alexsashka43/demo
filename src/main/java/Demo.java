import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Demo {
    //    String yandexAPIKey = "712f8089-7b95-40cb-8088-c27ae8e18957";
//    String idPeter = "10174";
    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Экземпляр класса Request создается через Builder (см. паттерн проектирования "Строитель")
        Request request = new Request.Builder().header("X-Yandex-API-Key", "712f8089-7b95-40cb-8088-c27ae8e18957")
//                .url("https://api.weather.yandex.ru/v2/forecast?lat=55.75396&lon=37.620393&extra=true")
                .header("Content-Type", "application/json;charset=UTF-8")
                .url("https://api.weather.yandex.ru/v2/forecast?lat=59.939099&lang=en_US&lon=30.315877&limit=5&hours=false")
                .build();

        // Получение объекта ответа от сервера
        Response response = client.newCall(request).execute();
        // Тело сообщения возвращается методом body объекта Response
        String body = response.body().string();
        System.out.println(body);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.readTree(body).at("/forecasts").asText());
        List<String> weatherList =
                objectMapper.readValue(objectMapper.readTree(body).at("/forecasts").asText(), new TypeReference<List<String>>() {
                });//

        List<WeatherResponse> weathList = new ArrayList<>();

        for (String s : weatherList) {
            WeatherResponse weatherResponse = new WeatherResponse();
            weatherResponse.setCity(objectMapper.readTree(body).at("/geo_object/locality/name").asText());
            weatherResponse.setWeatherText(objectMapper.readTree(s).at("/day/condition").asText());
            weatherResponse.setDate(objectMapper.readTree(s).at("/date").asText());
            weatherResponse.setTemprature(objectMapper.readTree(s).at("/day/temp_avg").asInt());
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

