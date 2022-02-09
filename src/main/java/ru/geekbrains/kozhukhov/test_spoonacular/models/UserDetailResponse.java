package ru.geekbrains.kozhukhov.test_spoonacular.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserDetailResponse extends CommonResponse{

    @JsonProperty("username")
    private String username;

    @JsonProperty("spoonacularPassword")
    private String spoonacularPassword;

    @JsonProperty("hash")
    private String hash;

}
