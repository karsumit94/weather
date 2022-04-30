package in.sumitkar.weather.model.openweather;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.sumitkar.weather.model.Weather;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherOpenWeather {

	private Long id;
	private String main;
	private String description;
	private String icon;

}
