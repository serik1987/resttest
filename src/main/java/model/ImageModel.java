package model;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import static io.restassured.RestAssured.given;

/**
 * Числится в документации как 'Image model'
 */
public class ImageModel {

    public String id;
    public String title;
    public String description;
    public int datetime;
    public String type;
    public boolean animated;
    public int width;
    public int height;
    public int size;
    public int views;
    public long bandwidth;
    public String deletehash;
    public String name;
    public String section;
    public String link;
    public String gifv;
    public String mp4;
    public Integer mp4_size;
    public Boolean looping;
    public boolean favorite;
    public boolean nsfw;
    public String vote;
    public boolean in_gallery;

    /**
     * Создание модели Image
     *
     * @param source объект, который возвращает JsonPath
     */
    public ImageModel(LinkedHashMap<String, Object> source){
        setupValue(source, "id", String.class);
        setupValue(source, "title", String.class);
        setupValue(source, "description", String.class);
        setupValue(source, "datetime", Integer.class);
        setupValue(source, "type", String.class);
        setupValue(source, "animated", Boolean.class);
        setupValue(source, "width", Integer.class);
        setupValue(source, "height", Integer.class);
        setupValue(source, "size", Integer.class);
        setupValue(source, "views", Integer.class);
        setupValue(source, "bandwidth", Integer.class);
        setupValue(source, "deletehash", String.class);
        setupValue(source, "name", String.class);
        setupValue(source, "section", String.class);
        setupValue(source, "link", String.class);
        setupValue(source, "gifv", String.class);
        setupValue(source, "mp4", String.class);
        setupValue(source, "mp4_size", Integer.class);
        setupValue(source, "looping", Boolean.class);
        setupValue(source, "favorite", Boolean.class);
        setupValue(source, "nsfw", Boolean.class);
        setupValue(source, "vote", String.class);
        setupValue(source, "in_gallery", Boolean.class);
    }

    private <T> void setupValue(LinkedHashMap<String, Object> source, String name, Class<T> valueType){
        Field declaredField = null;
        try {
            declaredField = ImageModel.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("The following field is not found in the ImageModel: " + name);
        }
        Object value = null;
        value = source.get(name);
        try {
            declaredField.set(this, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("The following field is not accessible in the ImageModel: " + name);
        }
    }

    public void assertLink(){
        given()
                .get(link)
                .then().statusCode(200)
                .header("Content-Type", type)
                .header("Content-Length", String.valueOf(size));
    }



}
