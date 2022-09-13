package edu.self.practice.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProfileRequest {
    private String title;
    private String content;
    private List<PictureRequest> pictures = new LinkedList<>();

    public ProfileRequest() {
    }
}
