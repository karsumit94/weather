package in.sumitkar.weather.client;

import in.sumitkar.weather.model.openweather.WeatherDataOpenWeather;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "openWeatherMapApi", url = "${openweather-base-url}")
public interface OpenWeatherMapApiClient {
    @GetMapping(path = "/data/2.5/forecast")
    WeatherDataOpenWeather getWeatherResponse(@RequestParam("q")String city,
                                              @RequestParam("appid")String key,
                                              @RequestParam(value = "cnt", required = false, defaultValue ="")String count,
                                              @RequestParam(value="units", required = false, defaultValue ="metric")String units);
}
