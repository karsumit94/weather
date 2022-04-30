package in.sumitkar.weather.model;

import lombok.Data;
@Data
public class City {

	private Long id;
	private String name;
	private Coordinates coord;
	private String country;
	private Integer population;
	private Integer timezone;
	private Long sunriseTime;
	private Long sunsetTime;

}
