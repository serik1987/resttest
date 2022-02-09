package ru.geekbrains.kozhukhov.test_spoonacular.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AddToMealPlanInfo {

    @JsonProperty("date")
    private Integer date;

    @JsonProperty("slot")
    private Integer slot;

    @JsonProperty("position")
    private Integer position;

    @JsonProperty("type")
    private String type;

    @JsonProperty("value")
    private MealValue value;

    private AddToMealPlanInfo() {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public static class MealValue{

        @JsonProperty("id")
        private Integer id;

        @JsonProperty("servings")
        private Integer servings;

        @JsonProperty("title")
        private String title;

        @JsonProperty("imageType")
        private String imageType;

        private MealValue(){}

        public enum ImageType{
            JPG("jpg"),
            PNG("png"),
            GIF("gif"),
            SVG("svg");

            public final String value;

            ImageType(String value){
                this.value = value;
            }
        }

        public static class Builder{

            private final MealValue mealValue;

            public Builder(){
                mealValue = new MealValue();
            }

            public Builder id(int id){
                mealValue.id = id;
                return this;
            }

            public Builder servings(int servings){
                mealValue.servings = servings;
                return this;
            }

            public Builder title(String title){
                mealValue.title = title;
                return this;
            }

            public Builder imageType(String imageType){
                mealValue.imageType = imageType;
                return this;
            }

            public Builder imageType(ImageType imageType){
                mealValue.imageType = imageType.value;
                return this;
            }

            public MealValue build(){
                return mealValue;
            }

        }

    }

    public enum Slot{
        BREAKFAST(1),
        DINNER(2),
        LUNCH(3);

        public final Integer value;

        Slot(int value){
            this.value = value;
        }
    }

    public enum Type{
        INGREDIENTS("INGREDIENTS"),
        PRODUCT("PRODUCT"),
        MENU_ITEM("MENU_ITEM"),
        RECIPE("RECIPE"),
        CUSTOM_FOOD("CUSTOM_FOOD");

        public final String value;

        Type(String value){
            this.value = value;
        }
    }

    public static class Builder{

        private final AddToMealPlanInfo info;

        public Builder(){
            info = new AddToMealPlanInfo();
        }

        /**
         * Добавляет дату
         *
         * @param timestamp временная метка UNIX
         * @return экземпляр класса
         */
        public Builder date(int timestamp){
            info.date = timestamp;
            return this;
        }

        /**
         * Добавляет дату
         *
         * @param date дата, которую надо добавить
         * @return экземпляр класса
         */
        public Builder date(Date date){
            info.date = (int)(date.getTime() / 1000);
            return this;
        }

        /**
         * Добавляет сегодняшнюю дату
         *
         * @return экземпляр класса
         */
        public Builder date(){
            return date(new Date());
        }

        /**
         * Добавляет произвольную дату
         *
         * @param year год
         * @param month месяц
         * @param day день
         * @return экземпляр класса
         */
        public Builder date(int year, int month, int day){
            Calendar cal = new GregorianCalendar();
            cal.set(year, month, day);
            return date(cal.getTime());
        }

        public Builder slot(int slot){
            info.slot = slot;
            return this;
        }

        public Builder slot(Slot slot){
            return slot(slot.value);
        }

        public Builder position(int position){
            info.position = position;
            return this;
        }

        public Builder type(String type){
            info.type = type;
            return this;
        }

        public Builder type(Type type){
            return type(type.value);
        }

        public Builder value(MealValue value){
            info.value = value;
            return this;
        }

        public AddToMealPlanInfo build(){
            return info;
        }

    }

}
