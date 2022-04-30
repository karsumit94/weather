package in.sumitkar.weather.model;

import java.util.List;
import lombok.Data;

@Data
public class WeatherData {

	private List<WeatherDayData> weatherDayData;
	private City city;

}
