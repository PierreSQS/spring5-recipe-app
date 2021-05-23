package guru.springframework.controllers;

import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class IndexControllerTest {

    IndexController indexController;

    @Mock
    RecipeService recipeSrv;

    @Mock
    Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getIndexPage() {
        String aString = "index";
        indexController = new IndexController(recipeSrv);

        String indexPage = indexController.getIndexPage(model);

        verify(recipeSrv).getRecipes();
        verify(model).addAttribute("recipes", recipeSrv.getRecipes());

        assertEquals(aString,indexPage);
    }
}