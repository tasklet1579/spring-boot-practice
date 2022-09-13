package edu.self.practice.member.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PictureRequest {
    private String path;
    private String name;
    private long size;
}
