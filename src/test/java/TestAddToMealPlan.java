import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.geekbrains.kozhukhov.test_spoonacular.models.AddToMealPlanInfo;
import ru.geekbrains.kozhukhov.test_spoonacular.models.UserDetailResponse;
import ru.geekbrains.kozhukhov.test_spoonacular.models.UserRegistrationDetail;

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

    @Test
    void addToMealPlan(){
        AddToMealPlanInfo info = new AddToMealPlanInfo.Builder()
                .date()
                .slot(AddToMealPlanInfo.Slot.BREAKFAST)
                .position(0)
                .type(AddToMealPlanInfo.Type.PRODUCT)
                .value(new AddToMealPlanInfo.MealValue.Builder()
                        .id(183433)
                        .servings(1)
                        .title("Ahold Lasagna with Meat Sauce")
                        .imageType(AddToMealPlanInfo.MealValue.ImageType.JPG)
                        .build()
                )
                .build();

        mealPlannerSpecification(true)
                .body(info)
                .post("/mealplanner/{username}/items");
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
                .setUsername("sergei")
                .setFirstName("Сергей")
                .setLastName("Кожухов")
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

}
