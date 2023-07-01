package sia.tacocloud.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import sia.tacocloud.models.Ingredient;
import sia.tacocloud.models.Taco;
import sia.tacocloud.models.Ingredient.Type;
import sia.tacocloud.repositories.IngredientRepository;
import sia.tacocloud.repositories.TacoRepository;
import sia.tacocloud.repositories.UserRepository;
import sia.tacocloud.utils.ConfigReader;

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
    public CommandLineRunner dataLoader(
            IngredientRepository repo,
            UserRepository userRepo,
            PasswordEncoder encoder,
            TacoRepository tacoRepo

    ) {
        Iterable<Ingredient> ingredients = repo.findAll();
        if (ingredients.iterator().hasNext()) {
            return null;
            // log.info("Ingredients already loaded");
            // log.warn("Perform delete {}", ingredients);
            // repo.deleteAll();
        }

        return args -> {

            Ingredient flourTortilla = new Ingredient("FLTO", "Flour Tortilla",
                    Type.WRAP);
            repo.save(flourTortilla);
            Ingredient cornTortilla = new Ingredient("COTO", "Corn Tortilla", Type.WRAP);
            repo.save(cornTortilla);
            Ingredient groundBeef = new Ingredient("GRBF", "Ground Beef", Type.PROTEIN);
            repo.save(groundBeef);
            Ingredient carnitas = new Ingredient("CARN", "Carnitas", Type.PROTEIN);
            repo.save(carnitas);
            Ingredient tomatoes = new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES);
            repo.save(tomatoes);
            Ingredient lettuce = new Ingredient("LETC", "Lettuce", Type.VEGGIES);
            repo.save(lettuce);
            Ingredient cheddar = new Ingredient("CHED", "Cheddar", Type.CHEESE);
            repo.save(cheddar);
            Ingredient jack = new Ingredient("JACK", "Monterrey Jack", Type.CHEESE);
            repo.save(jack);
            Ingredient salsa = new Ingredient("SLSA", "Salsa", Type.SAUCE);
            repo.save(salsa);
            Ingredient sourCream = new Ingredient("SRCR", "Sour Cream", Type.SAUCE);
            repo.save(sourCream);

            // init tacos
            Taco taco1 = new Taco();
            taco1.setName("Carnivore");
            taco1.setIngredients(
                    Arrays.asList(flourTortilla, groundBeef, carnitas, sourCream, salsa,
                            cheddar));
            tacoRepo.save(taco1);

            Taco taco2 = new Taco();
            taco2.setName("Bovine Bounty");
            taco2.setIngredients(
                    Arrays.asList(cornTortilla, groundBeef, cheddar, jack, sourCream));
            tacoRepo.save(taco2);

            Taco taco3 = new Taco();
            taco3.setName("Veg-Out");
            taco3.setIngredients(
                    Arrays.asList(flourTortilla, cornTortilla, tomatoes, lettuce, salsa));
            tacoRepo.save(taco3);

        };

    }

    @Override
    public void run(String... args) throws Exception {

    }
}
