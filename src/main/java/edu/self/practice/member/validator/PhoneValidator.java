package edu.self.practice.member.validator;

import edu.self.practice.member.repository.MemberRepository;
import edu.self.practice.member.validator.annotation.Phone;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private static final Pattern pattern = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void initialize(Phone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isNull(value) || !isMatched(value)) {
            return false;
        }

        return !memberRepository.existsByEmail(value);
    }

    private boolean isNull(String value) {
        return value == null;
    }

    private boolean isMatched(String value) {
        return pattern.matcher(value)
                      .matches();
    }
}
