package in.sumitkar.weather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.sumitkar.weather.client.OpenWeatherMapApiClient;
import in.sumitkar.weather.model.SuggestionRule;
import in.sumitkar.weather.model.WeatherData;
import in.sumitkar.weather.model.WeatherDayData;
import in.sumitkar.weather.model.WeatherInfo;
import in.sumitkar.weather.model.openweather.WeatherDataOpenWeather;
import in.sumitkar.weather.model.openweather.WeatherDayDataOpenWeather;
import in.sumitkar.weather.model.openweather.WeatherOpenWeather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class WeatherService {
    @Value("${openweather-api-key}")
    private String openWeatherApiKey;
    @Value("${no-of-days}")
    private String noOfDays;
    private final OpenWeatherMapApiClient openWeatherMapApiClient;
    Logger logger = LoggerFactory.getLogger(WeatherService.class);
    @Value("${suggestion-rule}")
    private String suggestionRule;

    @Autowired
    public WeatherService(OpenWeatherMapApiClient openWeatherMapApiClient) {
        this.openWeatherMapApiClient = openWeatherMapApiClient;
    }

    public Map<String, WeatherDayData> getWeatherAPIResponse(String city) {
        WeatherData wd = new WeatherData();
        List<WeatherDayData> weatherDayData = new ArrayList<WeatherDayData>();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM");
        WeatherDataOpenWeather weatherResponse = openWeatherMapApiClient.getWeatherResponse(city, openWeatherApiKey,
                Integer.toString((Integer.parseInt(noOfDays) + 1) * 8), "metric");
        logger.info("Fetched data :"+weatherResponse);
        List<WeatherDayDataOpenWeather> weatherDayDataOpenWeatherList = weatherResponse.getList();
        Map<String, WeatherDayData> dataMap = createDataMap();
        for (WeatherDayDataOpenWeather day : weatherDayDataOpenWeatherList) {
            Long date = day.getDt() * 1000;
            Date d = new Date(date);
            String dateStr = dateFormat.format(d);
            if (dataMap.get(dateStr) != null) {
                WeatherDayData newWeatherDayData = dataMap.get(dateStr);
                WeatherInfo weatherInfo = newWeatherDayData.getWeatherInfo();
                if (weatherInfo != null) {
                    weatherInfo.setSuggestion("");
                    if (weatherInfo.getTempMax() != null) {
                        Double avgTemp = Double
                                .valueOf(Math.round((weatherInfo.getTempMax() + day.getMain().getTemp_max()) / 2));
                        weatherInfo.setTempMax(avgTemp);
                        newWeatherDayData.setWeatherInfo(weatherInfo);
                    } else {
                        weatherInfo.setTempMax(day.getMain().getTemp_max());
                        newWeatherDayData.setWeatherInfo(weatherInfo);
                    }
                    if (weatherInfo.getTempMin() != null) {
                        Double avgTemp = Double
                                .valueOf(Math.round((weatherInfo.getTempMin() + day.getMain().getTemp_min()) / 2));
                        weatherInfo.setTempMin(avgTemp);
                        newWeatherDayData.setWeatherInfo(weatherInfo);
                    } else {
                        weatherInfo.setTempMin(day.getMain().getTemp_min());
                        newWeatherDayData.setWeatherInfo(weatherInfo);
                    }
                    if (weatherInfo.getTemp() != null) {
                        Double avgTemp = Double
                                .valueOf(Math.round((weatherInfo.getTemp() + day.getMain().getTemp()) / 2));
                        weatherInfo.setTemp(avgTemp);
                        newWeatherDayData.setWeatherInfo(weatherInfo);
                    } else {
                        weatherInfo.setTemp(day.getMain().getTemp());
                        newWeatherDayData.setWeatherInfo(weatherInfo);
                    }
                    if (weatherInfo.getFeelsLike() != null) {
                        Double avgTemp = Double
                                .valueOf(Math.round((weatherInfo.getFeelsLike() + day.getMain().getFeels_like()) / 2));
                        weatherInfo.setFeelsLike(avgTemp);
                        newWeatherDayData.setWeatherInfo(weatherInfo);
                    } else {
                        weatherInfo.setFeelsLike(day.getMain().getFeels_like());
                        newWeatherDayData.setWeatherInfo(weatherInfo);
                    }
                    if (weatherInfo.getWind() != null) {
                        Double avgSpeed = Double
                                .valueOf(Math.round((weatherInfo.getWind() + day.getWind().getSpeed()) / 2));
                        weatherInfo.setWind(avgSpeed);
                        newWeatherDayData.setWeatherInfo(weatherInfo);
                    } else {
                        weatherInfo.setWind(day.getWind().getSpeed());
                        newWeatherDayData.setWeatherInfo(weatherInfo);
                    }
                    List<WeatherOpenWeather> weatherList = day.getWeather();
                    if (!weatherInfo.getThunderstorm()) {
                        for (WeatherOpenWeather w : weatherList) {
                            if (w.getIcon().equals("11n") || w.getIcon().equals("11d")) {
                                weatherInfo.setThunderstorm(true);
                                break;
                            }
                        }
                        newWeatherDayData.setWeatherInfo(weatherInfo);
                    }
                    if (!weatherInfo.getRain()) {
                        for (WeatherOpenWeather w : weatherList) {
                            if (w.getIcon().equals("10d") || w.getIcon().equals("10n") ||
                                    w.getIcon().equals("09d") || w.getIcon().equals("09n")) {
                                weatherInfo.setRain(true);
                                break;
                            }
                        }
                        newWeatherDayData.setWeatherInfo(weatherInfo);
                    }
                }
            }
        }
        return processSuggestion(dataMap);
    }

    private Map<String, WeatherDayData> createDataMap() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM");
        Map<String, WeatherDayData> dataMap = new HashMap<>();
        Date todayDate = new Date();

        for (int count = 0; count <= Integer.parseInt(noOfDays); count++) {
            Instant instant = todayDate.toInstant();
            Date futureDate = Date.from(instant.plus(count, ChronoUnit.DAYS));
            WeatherDayData wd = new WeatherDayData();
            WeatherInfo info = new WeatherInfo();
            wd.setWeatherInfo(info);
            dataMap.put(dateFormat.format(futureDate), wd);
        }
        return dataMap;
    }

    private Map<String, WeatherDayData> processSuggestion(Map<String, WeatherDayData> dataMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<SuggestionRule> rules = new ArrayList<>();
        try {
            rules = objectMapper.readValue(suggestionRule, new TypeReference<List<SuggestionRule>>() {
            });
        } catch (JsonProcessingException e) {
            System.out.println("Error parsing the rules");
        }
        for (Map.Entry<String, WeatherDayData> entry : dataMap.entrySet()) {
            String suggestion = "";
            for (SuggestionRule rule : rules) {
                WeatherDayData data = entry.getValue();
                WeatherInfo weatherInfo = data.getWeatherInfo();
                String field = rule.getField();
                switch (field) {
                    case "temp":
                        if (rule.getCondition().equals(">")
                                && weatherInfo.getTemp() > Double.parseDouble(rule.getValue())) {
                            suggestion += " " + rule.getSuggestion();
                        }
                        if (rule.getCondition().equals("==")
                                && weatherInfo.getTemp() == Double.parseDouble(rule.getValue())) {
                            suggestion += " " + rule.getSuggestion();
                        }
                        if (rule.getCondition().equals("<")
                                && weatherInfo.getTemp() < Double.parseDouble(rule.getValue())) {
                            suggestion += " " + rule.getSuggestion();
                        }
                        break;
                    case "wind":
                        if (rule.getCondition().equals(">")
                                && weatherInfo.getWind() > Double.parseDouble(rule.getValue())) {
                            suggestion += " " + rule.getSuggestion();
                        }
                        if (rule.getCondition().equals("==")
                                && weatherInfo.getWind() == Double.parseDouble(rule.getValue())) {
                            suggestion += " " + rule.getSuggestion();
                        }
                        if (rule.getCondition().equals("<")
                                && weatherInfo.getWind() < Double.parseDouble(rule.getValue())) {
                            suggestion += " " + rule.getSuggestion();
                        }
                        break;
                    case "rain":
                        if (rule.getCondition().equals("==")
                                && (weatherInfo.getRain() == Boolean.parseBoolean(rule.getValue()))) {
                            suggestion += " " + rule.getSuggestion();
                        }
                        break;
                    case "thunderstorm":
                        if (rule.getCondition().equals("==")
                                && (weatherInfo.getThunderstorm() == Boolean.parseBoolean(rule.getValue()))) {
                            suggestion += " " + rule.getSuggestion();
                        }
                        break;
                }
                weatherInfo.setSuggestion(suggestion.trim());
                data.setWeatherInfo(weatherInfo);
                dataMap.put(entry.getKey(), data);
            }
        }
        return dataMap;
    }
}
