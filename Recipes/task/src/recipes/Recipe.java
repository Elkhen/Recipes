package recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
public class Recipe {
    @JsonIgnore
    @Id
    @GeneratedValue(generator = "sequenceGenerator")
    @GenericGenerator(
            name = "sequenceGenerator",
            strategy = "sequence",
            parameters = {
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String category;
    private LocalDateTime date = LocalDateTime.now();
    @NotBlank
    private String description;
    @NotNull
    @Size(min = 1)
    private String[] ingredients;
    @NotNull
    @Size(min = 1)
    private String[] directions;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
