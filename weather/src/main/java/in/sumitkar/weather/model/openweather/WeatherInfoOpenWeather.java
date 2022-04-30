package in.sumitkar.weather.model.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.sumitkar.weather.model.WeatherInfo;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherInfoOpenWeather {

	private Double temp;
	private Double feels_like;
	private Double temp_min;
	private Double temp_max;
	private Double pressure;
	private Double sea_level;
	private Double grnd_level;
	private Double humidity;
	private Double temp_kf;
}
