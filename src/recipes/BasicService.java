package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BasicService {

    @Autowired
    RecipesRepository repository;

    public Recipe get(long id) {
        Recipes item;
        if (repository.existsById(id)) {
            item = repository.findById(id).get();
            return item.mapToRecipe();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<Recipe> searchByName(String name) {

        Iterable<Recipes> all = repository.findAll();
        ArrayList<Recipe> result = new ArrayList<>();

        for (Recipes item : all) {
            if (item.getName().toUpperCase().contains(name.toUpperCase())) {
                result.add(item.mapToRecipe());
            }
        }

        result.sort(Comparator.comparing(Recipe::getDate));
        Collections.reverse(result);
        return result;
    }

    public List<Recipe> searchByCategory(String category) {

        Iterable<Recipes> all = repository.findAll();
        ArrayList<Recipe> result = new ArrayList<>();

        for (Recipes item : all) {
            if (Objects.equals(item.getCategory().toUpperCase(), category.toUpperCase())) {
                result.add(item.mapToRecipe());
            }
        }

        result.sort(Comparator.comparing(Recipe::getDate));
        Collections.reverse(result);
        return result;
    }

    public Map<String, Long> post(Recipe recipe, String username) {
        Recipes.RecipesBuilder builder = Recipes.builder();
        Recipes saved = builder.name(recipe.getName())
                .category(recipe.getCategory())
                .date(LocalDateTime.now())
                .description(recipe.getDescription())
                .ingredients(recipe.getIngredients())
                .directions(recipe.getDirections())
                .email(username)
                .build();
        repository.save(saved);
        return Map.of("id", saved.getId());
    }

    public ResponseEntity<String> update(long id, Recipe recipe, String username) {
        if (repository.existsById(id)) {
            if (!Objects.equals(repository.findById(id).get().getEmail(), username)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Recipes.RecipesBuilder builder = Recipes.builder();
            Recipes saved = builder.id(id)
                    .name(recipe.getName())
                    .category(recipe.getCategory())
                    .date(LocalDateTime.now())
                    .description(recipe.getDescription())
                    .ingredients(recipe.getIngredients())
                    .directions(recipe.getDirections())
                    .email(username)
                    .build();
            repository.save(saved);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> delete(long id, String username) {
        if (repository.existsById(id)) {
            if (!Objects.equals(repository.findById(id).get().getEmail(), username)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
