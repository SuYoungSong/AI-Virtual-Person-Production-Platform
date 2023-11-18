package untitle.endproject.demonstration.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkVocal {
    @Id
    private String uuid;

    private String id;
    private String vocal_name;
    private String ready;


}