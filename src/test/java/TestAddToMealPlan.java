import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.geekbrains.kozhukhov.test_spoonacular.models.AddToMealPlanInfo;
import ru.geekbrains.kozhukhov.test_spoonacular.models.AddToMealResult;
import ru.geekbrains.kozhukhov.test_spoonacular.models.UserDetailResponse;
import ru.geekbrains.kozhukhov.test_spoonacular.models.UserRegistrationDetail;
import ru.geekbrains.kozhukhov.test_spoonacular.utils.RandomGenerator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class TestAddToMealPlan{

    public static final String BASE_URI = "https://api.spoonacular.com";
    public static final String API_KEY = "12cb02505e8040f0979ef7f1460ed3fa";

    private static RequestSpecification requestSpecification;
    private static ResponseSpecification responseSpecification;

    private static UserDetailResponse userDetails;

    @BeforeAll
    static void setUpClass(){
        setUpSpecifications();
        setUpUser();
    }

    @ParameterizedTest
    @MethodSource("addToMealPlanPositiveProvider")
    @DisplayName("Add to meal endpoint: positive tests")
    void addToMealPlanPositive(AddToMealPlanInfo info){
        AddToMealResult result = mealPlannerSpecification(true)
                .body(info)
                .post("/mealplanner/{username}/items")
                .getBody()
                .as(AddToMealResult.class);

        MatcherAssert.assertThat(result.getId(), Matchers.greaterThanOrEqualTo(0));
    }

    private static void setUpSpecifications(){
        RestAssured.baseURI = BASE_URI;

        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", API_KEY)
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.STATUS)
                .expectStatusCode(Matchers.allOf(
                        Matchers.greaterThanOrEqualTo(200),
                        Matchers.lessThanOrEqualTo(299)
                ))
                .expectContentType(ContentType.JSON)
                .expectBody("status", Matchers.equalTo("success"))
                .build();

    }

    static void setUpUser(){

        UserRegistrationDetail userRegistrationDetail = new UserRegistrationDetail.Builder()
                .setUsername(RandomGenerator.username())
                .setFirstName(RandomGenerator.username())
                .setLastName(RandomGenerator.username())
                .setEmail("serik1987@gmail.com")
                .build();

        userDetails = given()
                .spec(requestSpecification)
                .body(userRegistrationDetail)
                .then()
                .spec(responseSpecification)
                .when()
                .post("/users/connect")
                .body()
                .as(UserDetailResponse.class);
    }

    private static RequestSpecification mealPlannerSpecification(boolean debug){
        ResponseSpecification spec = given()
                .spec(requestSpecification)
                .pathParam("username", userDetails.getUsername())
                .queryParam("hash", userDetails.getHash())
                .then()
                .spec(responseSpecification);
        if (debug){
            spec = spec.log().all();
        }
        return spec.given();
    }

    private static Stream<AddToMealPlanInfo> addToMealPlanPositiveProvider(){
        return Stream.of(

                new AddToMealPlanInfo.Builder()
                        .date(1984, 1, 1)
                        .slot(AddToMealPlanInfo.Slot.BREAKFAST)
                        .position(0)
                        .type(AddToMealPlanInfo.Type.PRODUCT)
                        .value(new AddToMealPlanInfo.MealValue.Builder()
                                .id(1)
                                .servings(0)
                                .title("")
                                .imageType(AddToMealPlanInfo.MealValue.ImageType.JPG)
                                .build())
                        .build(),

                new AddToMealPlanInfo.Builder()
                        .date()
                        .slot(AddToMealPlanInfo.Slot.LUNCH)
                        .position(1)
                        .type(AddToMealPlanInfo.Type.MENU_ITEM)
                        .value(new AddToMealPlanInfo.MealValue.Builder()
                                .id(19028)
                                .servings(0)
                                .title("x")
                                .imageType(AddToMealPlanInfo.MealValue.ImageType.PNG)
                                .build()
                        )
                        .build(),

                new AddToMealPlanInfo.Builder()
                        .date(2024, 1, 1)
                        .slot(AddToMealPlanInfo.Slot.DINNER)
                        .position(-1)
                        .type(AddToMealPlanInfo.Type.RECIPE)
                        .value(new AddToMealPlanInfo.MealValue.Builder()
                                .id(34904)
                                .servings(0)
                                .title("==========")
                                .build())
                        .build()
        );
    }

}
