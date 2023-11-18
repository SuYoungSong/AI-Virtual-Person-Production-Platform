package untitle.endproject.demonstration.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Audio {
    private String userPath;
    private String text;
    private String model_name;
    private String model_name_text;

    public Audio() {
    }

    public Audio(String userPath, String text, String model_name, String model_name_text) {
        this.userPath = userPath;
        this.text = text;
        this.model_name = model_name;
        this.model_name_text = model_name_text;
    }
}
