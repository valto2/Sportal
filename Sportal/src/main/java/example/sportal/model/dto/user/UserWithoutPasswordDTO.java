package example.sportal.model.dto.user;

import example.sportal.model.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserWithoutPasswordDTO {

    private long id;
    private String username;
    private String email;



    public UserWithoutPasswordDTO(User user){
        setId(user.getId());
        setUsername(user.getUsername());
        setEmail(user.getEmail());

    }
}
