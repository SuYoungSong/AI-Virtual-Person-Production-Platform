package untitle.endproject.demonstration.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkChar {
    @Id
    private String uuid;

    private String id;
    private String char_name;
    private String image_prompt;
    private String voice_prompt;
    private String pose_num;
    private String pose_num_text;
    private String model_info;
    private String model_info_text;
    private String audio_model_info;
    private String audio_model_text;


}
