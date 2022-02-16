package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.BasicService;
import recipes.Recipe;
import recipes.User;
import recipes.UserRepository;


import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
class Controller {

    @Autowired
    private BasicService basicService;

    @GetMapping("/recipe/{id}")
    public Recipe get(@PathVariable long id) {
        return basicService.get(id);
    }

    @GetMapping("/recipe/search")
    public List<Recipe> search(@RequestParam Map<String, String> map) {
        if (map.size() == 1) {
            if (map.containsKey("category")) {
                return basicService.searchByCategory(map.get("category"));
            } else if (map.containsKey("name")) {
                return basicService.searchByName(map.get("name"));
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/recipe/new")
    public Map<String, Long> post(@Valid @RequestBody Recipe recipe, @AuthenticationPrincipal UserDetails details) {
        return basicService.post(recipe, details.getUsername());
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<String> update(@PathVariable long id, @Valid @RequestBody Recipe recipe, @AuthenticationPrincipal UserDetails details) {
        return basicService.update(id, recipe, details.getUsername());
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> delete(@PathVariable long id, @AuthenticationPrincipal UserDetails details) {
        return basicService.delete(id, details.getUsername());
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/register")
    public void register(@Valid @RequestBody User user) {
        if (user.getEmail().matches("\\w+@\\w+\\.\\w+")) {
            if (!userRepository.existsById(user.getEmail())) {
                user.setPassword(encoder.encode(user.getPassword()));
                userRepository.save(user);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
