package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    private Recipe recipeMock;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RecipeService recipeSrv;

    @Before
    public void setUp() throws Exception {
        recipeMock = new Recipe();
        recipeMock.setDescription("Recipe Mock");
        recipeMock.setId(1L);
        when(recipeSrv.getRecipeByID(anyLong())).thenReturn(recipeMock);
    }

    @Test
    public void showRecipeById() throws Exception {

        mockMvc.perform(get("/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe", recipeMock))
                .andExpect(view().name("/recipe/show"))
                .andExpect(content().string(containsString("<h1 class=\"panel-title\">Recipe Mock</h1>")))
                .andDo(print());
        verify(recipeSrv).getRecipeByID(anyLong());
        verify(recipeSrv, never()).getRecipes();
    }
}