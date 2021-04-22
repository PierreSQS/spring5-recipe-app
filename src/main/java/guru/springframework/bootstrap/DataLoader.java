package guru.springframework.bootstrap;

import guru.springframework.domain.Difficulty;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final RecipeRepository recipeRepository;

    public DataLoader(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Recipe guacamole = new Recipe();
        guacamole.setDescription("Perfect Guacamole");
        guacamole.setPrepTime(10);
        guacamole.setCookTime(10);
        guacamole.setDifficulty(Difficulty.EASY);
        guacamole.setServings(2);


        Recipe spicyChicken = new Recipe();
        spicyChicken.setDescription("Spicy Garlic Chicken");
        spicyChicken.setPrepTime(10);
        spicyChicken.setCookTime(45);
        spicyChicken.setDifficulty(Difficulty.MODERATE);
        spicyChicken.setServings(4);

        recipeRepository.save(guacamole);
        recipeRepository.save(spicyChicken);
    }
}
