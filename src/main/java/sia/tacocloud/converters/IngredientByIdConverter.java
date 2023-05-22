package sia.tacocloud.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sia.tacocloud.data.IngredientRepository;
import sia.tacocloud.models.Ingredient;


@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    private IngredientRepository ingredientRepository;
    public  IngredientByIdConverter(IngredientRepository ingredientRepository){
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient convert(String id) {
        return ingredientRepository.findById(id).orElse(null);
    }
}
