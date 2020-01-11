package example.sportal.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import example.sportal.model.dto.user.RegisterUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_name")
    private String username;
    @Column(name = "user_email")
    private String email;
    @Column(name = "user_passsword")
    @JsonIgnore
    private String password;
    @Column
    private Boolean isAdmin;


    public User(RegisterUserDTO dto) {
        setUsername(dto.getUsername());
        setEmail(dto.getEmail());
        setPassword(dto.getPassword());
    }
}
