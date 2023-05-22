package sia.tacocloud;

import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import sia.tacocloud.data.IngredientRepository;
import sia.tacocloud.models.Ingredient;
import sia.tacocloud.models.Ingredient.Type;
import sia.tacocloud.utils.ConfigReader;

@Configuration
public class AppConfig implements CommandLineRunner {
    public static String getCreditCard() throws IOException {
        return ConfigReader.getValueByKey("creditCard");
    }

    @Bean
    public CommandLineRunner dataLoader(IngredientRepository repo) {
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
