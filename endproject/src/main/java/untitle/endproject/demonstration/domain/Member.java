package untitle.endproject.demonstration.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    private String id;

    @JsonIgnore
    private String pw;

    private String nickname;
    private String userName;
    private String phone;

    public void setPassword(String password) {
        // 비밀번호를 BCrypt를 사용하여 해시값으로 저장
        this.pw = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password) {
        // 비밀번호 검증
        return BCrypt.checkpw(password, this.pw);
    }
}