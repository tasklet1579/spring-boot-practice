package edu.self.practice.member.constant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EntityEnumerableConverter<T extends Enum<T> & EntityEnumerable> implements AttributeConverter<T, String> {
    private final Class<T> enumClass;
    private final String enumName;

    public EntityEnumerableConverter(Class<T> enumClass, String enumName) {
        this.enumClass = enumClass;
        this.enumName = enumName;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException();
        }

        return EntityEnumerableConverterUtils.getCode(attribute);
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s", enumName));
        }

        return EntityEnumerableConverterUtils.ofCode(enumClass, dbData);
    }
}
