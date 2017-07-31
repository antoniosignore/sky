package asignore.parental.control.service;


import java.util.HashMap;
import java.util.Map;

public enum RatingEnum {
    U(0, "U"), PG(1, "PG"), VM_12(2, "12"), VM_15(3, "15"), VM_18(4, "18");

    public final static Map<String, RatingEnum> ratingMap = new HashMap<>();

    static {
        for (RatingEnum rating : RatingEnum.values()) {
            ratingMap.put(rating.stringValue, rating);
        }
    }

    private final int value;
    private final String stringValue;

    RatingEnum(int intValue, String stringValue) {
        this.value = intValue;
        this.stringValue = stringValue;
    }

    public static RatingEnum getByString(String rating) {
        return ratingMap.get(rating);
    }

    public int getValue() {
        return value;
    }

}
