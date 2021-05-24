package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class IndexControllerTest {

    @Mock
    RecipeService recipeSrvMock;

    @Mock
    Model modelMock;

    IndexController indexController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeSrvMock);
    }

    @Test
    public void getIndexPage() {

        //Given
        String expectIndexPage = "index";
        Set<Recipe> recipeSetMock = new HashSet<>();
        when(recipeSrvMock.getRecipes()).thenReturn(recipeSetMock);

        //When
        String indexPage = indexController.getIndexPage(modelMock);

        //then
        Class<Set<Recipe>> clazz = (Class<Set<Recipe>>) recipeSetMock.getClass();
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(clazz);

        verify(recipeSrvMock, times(1)).getRecipes();
        verify(modelMock).addAttribute(eq("recipes"), argumentCaptor.capture());
        assertEquals(expectIndexPage,indexPage);

        Set<Recipe> passedRecipeSetToController = argumentCaptor.getValue();
        assertEquals(recipeSetMock.size(),passedRecipeSetToController.size());
    }
}