package sia.tacocloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import sia.tacocloud.models.Ingredient;
import sia.tacocloud.repositories.IngredientRepository;

@RestController
@RequestMapping(path = "api/ingredients", produces = "application/json")
@Slf4j
public class IngredientController {
    private IngredientRepository ingredientRepo;

    public IngredientController(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    @GetMapping
    public Iterable<Ingredient> allIngredients() {
        return ingredientRepo.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = "application/json")
    @ResponseStatus(code = org.springframework.http.HttpStatus.CREATED)
    public Ingredient postIngredient(@RequestBody Ingredient ingredient) {
        return ingredientRepo.save(ingredient);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(code = org.springframework.http.HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable("id") String id) {
        try {
            log.info("Deleting ingredient with id: " + id);
            ingredientRepo.deleteById(id);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Ingredient ingredientById(@PathVariable("id") String ingredientId) {
        return ingredientRepo.findById(ingredientId).get();
    }
}
