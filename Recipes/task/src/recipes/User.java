package recipes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Entity
public class User {

    @Id
    @Pattern(regexp = ".+@.+\\..+")
    @JsonProperty(required = true)
    @NotBlank
    private String email;

    @JsonProperty(required = true)
    @NotBlank
    @Size(min = 8)
    private String password;

    private String role = "ROLE_USER";

    @OneToMany(mappedBy = "user")
    private Set<Recipe> recipes;
}
