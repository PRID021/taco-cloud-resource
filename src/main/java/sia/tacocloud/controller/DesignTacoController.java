package sia.tacocloud.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import sia.tacocloud.models.Ingredient;
import sia.tacocloud.models.Ingredient.Type;
import sia.tacocloud.models.Taco;
import sia.tacocloud.models.TacoOrder;
import sia.tacocloud.repositories.IngredientRepository;
import sia.tacocloud.service.FileWriterGateway;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
@CrossOrigin(origins = "*")

public class DesignTacoController {
    private final IngredientRepository ingredientRepository;
    private FileWriterGateway fileWriterGateway;

    public DesignTacoController(IngredientRepository ingredientRepository, FileWriterGateway fileWriterGateway) {
        this.ingredientRepository = ingredientRepository;
        this.fileWriterGateway = fileWriterGateway;
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepository.findAll();
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder tacoOrder() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        log.info("showDesignForm have been call");
        fileWriterGateway.writeToFileChannel("ingredients", "getAllIngredient");
        return "design";
    }

    private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredients, Type type) {
        List<Ingredient> listIngredient = StreamSupport.stream(ingredients.spliterator(), false)
                .collect(Collectors.toList());
        return listIngredient.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
    }

    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) {
        if (errors.hasErrors()) {
            log.error("Error processing taco: {}", errors);
            return "design";
        }

        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);
        return "redirect:/orders/current";
    }

}
