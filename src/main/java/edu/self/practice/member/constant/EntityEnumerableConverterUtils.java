package edu.self.practice.member.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.EnumSet;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntityEnumerableConverterUtils {
    public static <T extends Enum<T> & EntityEnumerable> String getCode(T enumValue) {
        return enumValue.getCode();
    }

    public static <T extends Enum<T> & EntityEnumerable> T ofCode(Class<T> enumClass, String dbData) {
        return EnumSet.allOf(enumClass)
                      .stream()
                      .filter(t -> t.getCode().equals(dbData))
                      .findAny()
                      .orElseThrow(IllegalArgumentException::new);
    }
}
