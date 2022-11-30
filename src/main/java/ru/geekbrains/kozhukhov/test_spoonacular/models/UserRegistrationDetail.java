package ru.geekbrains.kozhukhov.test_spoonacular.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserRegistrationDetail {

    @JsonProperty("username")
    private String username;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("email")
    private String email;

    public static class Builder{

        UserRegistrationDetail buildingClass = null;

        public Builder(){
            buildingClass = new UserRegistrationDetail();
        }

        public Builder setUsername(String username){
            buildingClass.username = username;
            return this;
        }

        public Builder setFirstName(String firstName){
            buildingClass.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName){
            buildingClass.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email){
            buildingClass.email = email;
            return this;
        }

        public UserRegistrationDetail build(){
            return buildingClass;
        }

    }

}
