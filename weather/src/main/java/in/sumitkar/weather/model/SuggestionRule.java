package in.sumitkar.weather.model;

import lombok.Data;

@Data
public class SuggestionRule {
    String field;
    String condition;
    String value;
    String suggestion;
}
