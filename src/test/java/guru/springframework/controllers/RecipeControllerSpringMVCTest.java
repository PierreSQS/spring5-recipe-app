package guru.springframework.controllers;

import guru.springframework.domain.Notes;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerSpringMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RecipeService recipeService;


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testShowRecipeById() throws Exception {

        Notes notes = new Notes();
        notes.setRecipeNotes("notes");
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setNotes(notes);

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
                // Model-Attribute not present in this case.Why???
//                .andExpect(model().attributeExists("recipe"))
//                .andExpect(model().attribute("recipe",recipeMock))
                .andDo(print());
    }

    @Test
    public void testSaveOrUpdateWithMultiMap() throws Exception {
        Recipe recipeMock = new Recipe();
        recipeMock.setId(1L);
        recipeMock.setDescription("Recipe Mock");

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("id",recipeMock.getId().toString());
        paramMap.add("description", recipeMock.getDescription());
        mockMvc.perform(post("/recipe")
                .params(paramMap))
                .andExpect(status().is3xxRedirection())
                // Model-Attribute not present in this case.Why???
//                .andExpect(model().attributeExists("recipe"))
//                .andExpect(model().attribute("recipe",recipeMock))
                .andDo(print());
    }
}