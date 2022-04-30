package in.sumitkar.weather.model.openweather;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDataOpenWeather {
	private String cod;
	private Integer message;
	private Integer cnt;
	private List<WeatherDayDataOpenWeather> list;
	private CityOpenWeather city;
}
