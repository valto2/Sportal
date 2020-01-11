package example.sportal.model.dto.user;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePassDTO {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}