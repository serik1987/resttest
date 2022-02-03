package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.ImageModel;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

/**
 * Является непосредственным аналогом RestAssured, однако, в отличие от него
 */
public class ImgurAssured {

    public static final String BASE_URL = "https://api.imgur.com";

    private static final String _ENDPOINT_PREFIX = "/3/";
    public static final String ENDPOINT_IMAGE = _ENDPOINT_PREFIX + "image/";

    private static final ObjectMapper mapper = new ObjectMapper();

    RequestSpecification requestSpecification;
    Response responseSpecification = null;
    LinkedHashMap<String, Object> responseAnalyzed = null;

    public static void setUpClass(){
        RestAssured.baseURI = BASE_URL;
    }

    /**
     * Инициализирует imgur-assured, а также инициализирует ResponseSpecification от rest-assured,
     * настраивая общие для всех эндпоинтов параметры
     *
     * @param clientId Ваш clientID, который Вам должны были дать при регистрации приложения
     */
    public ImgurAssured(String clientId){
        requestSpecification = given()
                .header("Authorization", "Client-ID " +clientId)
                .log().all();
    }

    /**
     * Выполняет процедуру поиска нужного изображения. Является методом отправки запроса.
     *
     * @param imageId идентификатор изображения
     * @return экземпляр объекта ImgurAssured
     */
    public ImgurAssured getImage(String imageId){
        if (responseSpecification != null || responseAnalyzed != null){
            throw new RuntimeException("Вы попытались применить метод отправки запроса дважды. Не делайте так: " +
                    "он - одноразовый");
        }

        responseSpecification = requestSpecification.get(ENDPOINT_IMAGE + imageId);
        return this;
    }

    /**
     * Проверяет наличие необходимых параметров в ответе, после чего возвращает содержимое поля 'data' этого
     * самого ответа в виде строки.
     *
     * @return Данный объект
     */
    public ImgurAssured analyze(){
        if (responseSpecification == null){
            throw new RuntimeException("Перед тем, как выполнять assertResponse, Вы должны хотя бы один раз" +
                    "запустить метод отправки запроса");
        }
        if (responseAnalyzed != null){
            throw new RuntimeException("Один и тот же ответ нельзя проанализировать дважды");
        }

        String fullResponse = responseSpecification
                .then().statusCode(200)
                .header("Content-Type", "application/json")
                .log().all()
                .body("status", equalTo(200))
                .body("success", equalTo(true))
                .body("$", hasKey("data"))
                .extract().asString();

        responseAnalyzed = new JsonPath(fullResponse).get("data");

        return this;
    }

    /**
     * Проверяет, вернул ли ответ страницу 404 или нет
     *
     * @return Данный объект
     */
    public ImgurAssured analyzeError(){
        if (responseSpecification == null){
            throw new RuntimeException("Перед тем, как выполнять assertResponse, Вы должны хотя бы один раз" +
                    "запустить метод отправки запроса");
        }
        if (responseAnalyzed != null){
            throw new RuntimeException("Один и тот же ответ нельзя проанализировать дважды");
        }

        try{
            responseSpecification.then().statusCode(404);
        } catch (AssertionError e){
            responseSpecification.then().statusCode(400);
        }

        return this;
    }

    /**
     * Преобразует поле data тела ответа в модель image.
     *
     * @return модель image
     */
    public ImageModel toImageModel() throws JsonProcessingException {
        return new ImageModel(responseAnalyzed);
    }



}
