package in.sumitkar.weather.controller;

import in.sumitkar.weather.model.WeatherDayData;
import in.sumitkar.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class WeatherController {
    Logger logger = LoggerFactory.getLogger(WeatherController.class);
    @Autowired
    WeatherService weatherService;

    @GetMapping("weather")
    public Map<String, WeatherDayData> getWeatherforCity(@RequestParam String city) {
        Map<String, WeatherDayData> m = new HashMap<>();
        logger.info("Trying to fetch data for : "+ city);
        try {
            m = weatherService.getWeatherAPIResponse(city);
        } catch (Exception e) {
            System.out.println("Error fetching data");
        }
        return m;
    }

}
