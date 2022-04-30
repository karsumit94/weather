package in.sumitkar.weather.model;


import lombok.Data;


@Data

public class WeatherInfo {

	private Double temp;
	private Double feelsLike;
	private Double tempMin;
	private Double tempMax;
	private Double wind;
	private String suggestion;
	private Boolean thunderstorm = false;
	private Boolean rain = false;
}
