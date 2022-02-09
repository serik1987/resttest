package ru.geekbrains.kozhukhov.test_spoonacular.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AddToMealResult extends CommonResponse{

    @JsonProperty("id")
    private Integer id;

}
