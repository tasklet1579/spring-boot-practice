package edu.self.practice.member.domain;

import edu.self.practice.member.dto.PictureRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "FK_PICTURE_TO_PROFILE"))
    private Profile profile;

    private String path;

    private String name;

    private long size;

    private String use;

    private final LocalDateTime createDate = LocalDateTime.now();

    public Picture(Profile profile, PictureRequest request) {
        this.profile = profile;
        this.path = request.getPath();
        this.name = request.getName();
        this.size = request.getSize();
    }

    public void changeProfile(Profile target) {
        if (Objects.nonNull(profile)) {
            profile.removePicture(this);
        }
        profile = target;
        if (Objects.nonNull(profile) && !profile.containsPicture(this)) {
            List<Picture> pictures = profile.getPictures();
            pictures.add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(id, picture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", use='" + use + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
