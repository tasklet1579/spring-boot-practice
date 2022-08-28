package edu.self.practice.member.constant;

import java.util.Arrays;

public enum Gender implements EntityEnumerable {
    MALE("1", "남성", "성(性)의 측면에서 남자를 이르는 말. 특히, 성년(成年)이 된 남자를 이른다."),
    FEMALE("2", "여성", "성(性)의 측면에서 여자를 이르는 말. 특히, 성년(成年)이 된 여자를 이른다.");

    private final String code;
    private final String value;
    private final String comment;

    Gender(String code, String value, String comment) {
        this.code = code;
        this.value = value;
        this.comment = comment;
    }

    public static Gender findByName(String name) {
        return Arrays.stream(values())
                     .filter(gender -> gender.name().equals(name))
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getComment() {
        return comment;
    }


    @javax.persistence.Converter(autoApply = true)
    public static class GenderConverter extends EntityEnumerableConverter<Gender> {
        private static final String ENUM_NAME = "성별";

        public GenderConverter() {
            super(Gender.class, ENUM_NAME);
        }
    }
}
