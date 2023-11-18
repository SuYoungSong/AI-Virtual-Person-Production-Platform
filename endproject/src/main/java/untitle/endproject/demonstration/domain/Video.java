package untitle.endproject.demonstration.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Video {
    private String userPath;
    private String imagePath;
    private String audioPath;

    public Video() {
    }

    public Video(String userPath, String imagePath, String audioPath) {
        this.userPath = userPath;
        this.imagePath = imagePath;
        this.audioPath = audioPath;
    }
}
