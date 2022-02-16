package recipes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String category;

    private LocalDateTime date;

    private String description;

    private String[] ingredients;

    private String[] directions;

    private String email;

    public Recipe mapToRecipe() {
        return new Recipe(name, category, date, description, ingredients, directions);
    }
}
