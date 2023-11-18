package untitle.endproject.demonstration.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Image {
    private String modelVersion;
    private String userPath;
    private String prompt;
    private String negativePrompt;
    private String modelName;
    private String modelNameText;
    private String poseImagePath;
    private String poseImagePathText;
    private String charName;
    public Image() {
    }


    public Image(String modelVersion, String userPath, String prompt, String negativePrompt, String modelName, String poseImagePath, String charName, String modelNameText, String poseImagePathText ) {
        this.modelVersion = modelVersion;
        this.userPath = userPath;
        this.prompt = prompt;
        this.negativePrompt = negativePrompt;
        this.modelName = modelName;
        this.modelNameText = modelNameText;
        this.poseImagePath = poseImagePath;
        this.poseImagePathText = poseImagePathText;
        this.charName = charName;
    }
}
