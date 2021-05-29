package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by jt on 6/19/17.
 */
public class RecipeControllerTest {

    private MockMvc mockMvc;

    @Mock
    RecipeService recipeService;

    RecipeController controller;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetRecipe() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testShowRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("recipe",new Recipe()))
                .andDo(print());
    }

    @Test
    public void testSaveOrUpdate() throws Exception {
        Recipe recipeMock = new Recipe();
        recipeMock.setId(1L);
        recipeMock.setDescription("Recipe Mock");
        mockMvc.perform(post("/recipe")
                .param("id",recipeMock.getId().toString())
                .param("description",recipeMock.getDescription()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe",recipeMock))
                .andDo(print());
    }

    @Test
    public void testSaveOrUpdateWithMultiMap() throws Exception {
        Recipe recipeMock = new Recipe();
        recipeMock.setId(1L);
        recipeMock.setDescription("Recipe Mock");

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("id",recipeMock.getId().toString());
        paramMap.add("description",recipeMock.getDescription());
        mockMvc.perform(post("/recipe")
                .params(paramMap))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe",recipeMock))
                .andDo(print());
    }
}