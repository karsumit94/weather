package in.sumitkar.weather.model.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.sumitkar.weather.model.Wind;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WindOpenWeather {
	private Double speed;
	private Integer deg;
	private Double gust;
}
