package edu.self.practice.member.dto;

import edu.self.practice.member.validator.annotation.Phone;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    @Email(message = "올바른 이메일을 입력해 주세요.")
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @Phone
    private String phone;
}
