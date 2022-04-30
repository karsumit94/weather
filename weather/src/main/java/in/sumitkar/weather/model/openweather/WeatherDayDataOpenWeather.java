package in.sumitkar.weather.model.openweather;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import in.sumitkar.weather.model.WeatherDayData;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDayDataOpenWeather {
	private Long dt;
	private WeatherInfoOpenWeather main;
	private List<WeatherOpenWeather> weather;
	private CloudsOpenWeather clouds;
	private WindOpenWeather wind;
	private Integer visibility;
	private Integer pop;
	private SysOpenWeather sys;
	private String dt_txt;
}
