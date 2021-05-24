package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;





/**
 * Created by PierreSQS on 24/05/21.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {IndexController.class})
public class IndexControllerTest {

    @MockBean
    RecipeService recipeSrvMock;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testWithMvcMockAsSpringTeam() throws Exception {
        // Given
        Recipe recipe1 = new Recipe();
        recipe1.setDescription("Recipe1");
        recipe1.setId(1L);
        Recipe recipe2 = new Recipe();
        recipe2.setDescription("Recipe2");
        recipe2.setId(2L);

        Set<Recipe> recipeSetMock = new HashSet<>(Arrays.asList(recipe1,recipe2));
        when(recipeSrvMock.getRecipes()).thenReturn(recipeSetMock);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(header().stringValues("Content-Language","en"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("My Recipes!")))
                .andDo(print());
    }

}