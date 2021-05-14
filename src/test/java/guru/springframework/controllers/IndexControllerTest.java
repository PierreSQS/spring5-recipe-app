package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IndexControllerTest {

    IndexController indexController;

    @Mock
    RecipeService recipeSrv;

    @Mock
    Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeSrv);
    }

    @Test
    public void getIndexPage() {
        Set<Recipe> recipes = new HashSet<>();
        Recipe recipe1 = new Recipe();
        recipe1.setDescription("Recipe1");
        recipes.add(recipe1);
        when(recipeSrv.getRecipes()).thenReturn(recipes);

        String indexPage = indexController.getIndexPage(model);
        assertEquals("index",indexPage);
        verify(model,times(1)).addAttribute("recipes",recipeSrv.getRecipes());
    }
}