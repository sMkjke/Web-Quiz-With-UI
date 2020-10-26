package engine.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
public class User {

    // TODO: 26.10.2020 добавить id генерироваться и айди будет ссылкой
    @NotNull
    @NotBlank(message = "Email is required")
    @Email
    @Pattern(regexp = ".+@.+\\..+")
    @Id
    private String email;

    @NotNull
    @NotBlank(message = "Password is required")
    @Size(min = 5)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}