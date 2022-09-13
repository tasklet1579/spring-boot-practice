package edu.self.practice.member.domain;

import edu.self.practice.member.dto.PictureRequest;
import edu.self.practice.member.dto.ProfileRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    private String title;

    @Lob
    private String content;

    @BatchSize(size = 2)
    @OneToMany(mappedBy = "profile", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Picture> pictures = new LinkedList<>();

    private final LocalDateTime createDate = LocalDateTime.now();

    private LocalDateTime updateDate;

    public Profile(Member member, ProfileRequest request) {
        this.member = member;
        this.title = request.getTitle();
        this.content = request.getContent();
        this.pictures = createPictures(request.getPictures());
    }

    public List<Picture> createPictures(List<PictureRequest> requests) {
        if (requests.isEmpty()) {
            return Collections.emptyList();
        }

        return requests.stream()
                       .map(request -> new Picture(this, request))
                       .collect(Collectors.toList());
    }

    public void addPicture(Picture picture) {
        pictures.add(picture);
        if (picture.getProfile() != this) {
            picture.changeProfile(this);
        }
    }

    public void removePicture(Picture picture) {
        pictures.remove(picture);
    }

    public boolean containsPicture(Picture picture) {
        return pictures.contains(picture);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
