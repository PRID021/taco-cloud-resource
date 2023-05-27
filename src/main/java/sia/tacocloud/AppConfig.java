package sia.tacocloud;

import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import sia.tacocloud.models.Ingredient;
import sia.tacocloud.models.Ingredient.Type;
import sia.tacocloud.repositories.IngredientRepository;
import sia.tacocloud.utils.ConfigReader;

@Slf4j
@Configuration
public class AppConfig implements CommandLineRunner {
    public static String getCreditCard() {
        try {
            return ConfigReader.getValueByKey("creditCard");
        } catch (IOException e) {
            return "0000000000000000";
        }
    }

    @Bean
    public CommandLineRunner dataLoader(IngredientRepository repo) {
        Iterable<Ingredient> ingredients = repo.findAll();
        if (ingredients.iterator().hasNext()) {
            log.info("Ingredients already loaded");
            log.warn("Perform delete {}", ingredients);
            repo.deleteAll();
        }

        return args -> {
            repo.save(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
            repo.save(new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
            repo.save(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
            repo.save(new Ingredient("CARN", "Carnitas", Type.PROTEIN));
            repo.save(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
            repo.save(new Ingredient("LETC", "Lettuce", Type.VEGGIES));
            repo.save(new Ingredient("CHED", "Cheddar", Type.CHEESE));
            repo.save(new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
            repo.save(new Ingredient("SLSA", "Salsa", Type.SAUCE));
            repo.save(new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
        };

    }

    @Override
    public void run(String... args) throws Exception {

    }
}
