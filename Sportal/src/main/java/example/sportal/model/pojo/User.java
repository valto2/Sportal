package example.sportal.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import example.sportal.model.dto.RegisterUserDTO;
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
        setPassword(dto.getPassword());//TODO BCRYPT!
    }
}



//    public User(String username, String email, String password) {
//        this.username = username;
//        this.email = email;
//        this.password = password;
//    }
//
//    public User(Integer id, String username, String password, String email, Boolean isAdmin) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.isAdmin = isAdmin;
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", username='" + username + '\'' +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                ", isAdmin=" + isAdmin +
//                '}';
//    }
//}