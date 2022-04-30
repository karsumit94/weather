package in.sumitkar.weather.model.openweather;
import in.sumitkar.weather.model.City;
import lombok.Data;
@Data
public class CityOpenWeather {

	private Long id;
	private String name;
	private CoordinatesOpenWeather coord;
	private String country;
	private Integer population;
	private Integer timezone;
	private Long sunrise;
	private Long sunset;

}
