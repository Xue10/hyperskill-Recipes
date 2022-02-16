package recipes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;
}
