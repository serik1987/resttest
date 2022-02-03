package ru.geekbrains.imgur;

import com.fasterxml.jackson.core.JsonProcessingException;
import controller.ImgurAssured;
import model.ImageModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.stream.Stream;

public class TestImgur {

    private static final String CLIENT_ID = "8c7fd5520451b67";
    private static final String CLIENT_SECRET = "db214bba706d9a7791d223e327964b7694815e9a";

    @BeforeAll
    static void setUpClass(){
        ImgurAssured.setUpClass();
    }

    @ParameterizedTest
    @MethodSource("providePositiveImages")
    void testGetImagePositive(String imageId, String imageType){
        ImageModel image = null;
        try {
            image = new ImgurAssured(CLIENT_ID)
                    .getImage(imageId)
                    .analyze()
                    .toImageModel();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Assertions.fail("JSON processing exception");
        }

        Assertions.assertEquals(imageId, image.id);
        Assertions.assertEquals(imageType, image.type);
        image.assertLink();
    }

    static Stream<Arguments> providePositiveImages(){
        return Stream.of(
                Arguments.of("gqYqQml", "image/jpeg"),
                Arguments.of("2QocJcW", "image/jpeg"),
                Arguments.of("gHtLZCA", "image/jpeg"),
                Arguments.of("tLbI6YH", "video/mp4"),
                Arguments.of("zZhh4sN", "video/mp4"),
                Arguments.of("cZfw9ae", "image/jpeg"),
                Arguments.of("Mt9or9Z", "image/jpeg"),
                Arguments.of("H8G4BYl", "image/jpeg")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"dfkjuvhkidf", "вдшилоит"})
    void testGetImageNegative(String imageId){
        new ImgurAssured(CLIENT_ID).getImage(imageId).analyzeError();
    }



}
