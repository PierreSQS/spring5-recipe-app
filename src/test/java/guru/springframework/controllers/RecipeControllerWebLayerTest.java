package guru.springframework.controllers;


import guru.springframework.commands.RecipeCommand;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RecipeService recipeSrvMock;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testShowRecipeById() throws Exception {

        Notes notes = new Notes();
        notes.setRecipeNotes("Recipe Notes");
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setNotes(notes);

        when(recipeSrvMock.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testShowRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andDo(print());
    }

    @Test
    public void testSaveOrUpdateWithParams() throws Exception {
        // given
        RecipeCommand recipeCmdMock = new RecipeCommand();
        recipeCmdMock.setId(2L);
        recipeCmdMock.setDescription("Recipe Mock");

        // when
        when(recipeSrvMock.saveRecipeCommand(any())).thenReturn(recipeCmdMock);

        // then
        mockMvc.perform(post("/recipe")
                    .param("id",recipeCmdMock.getId().toString())
                    .param("description",recipeCmdMock.getDescription()))
                .andExpect(status().is3xxRedirection())
                // Model-Attribute not present in this case.Why???
//                .andExpect(model().attributeExists("recipe"))
//                .andExpect(model().attribute("recipe",recipeMock))
                .andExpect(view().name("redirect:2/show"))
                .andDo(print());
    }

    @Test
    public void testSaveOrUpdateWithoutParams() throws Exception {
        // given
        RecipeCommand recipeCmdMock = new RecipeCommand();
        recipeCmdMock.setId(1L);
        recipeCmdMock.setDescription("Recipe Mock");

        // when
        when(recipeSrvMock.saveRecipeCommand(any())).thenReturn(recipeCmdMock);

        // then
        mockMvc.perform(post("/recipe"))
                .andExpect(status().is3xxRedirection())
                // Model-Attribute not present in this case.Why???
//                .andExpect(model().attributeExists("recipe"))
//                .andExpect(model().attribute("recipe",recipeMock))
                .andExpect(view().name("redirect:1/show"))
                .andDo(print());
    }

    @Test
    public void testSaveOrUpdateWithMultiMap() throws Exception {
        // Given
        RecipeCommand recipeCmdMock = new RecipeCommand();
        recipeCmdMock.setId(1L);
        recipeCmdMock.setDescription("Recipe Mock");

        // When
        when(recipeSrvMock.saveRecipeCommand(any())).thenReturn(recipeCmdMock);

        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", recipeCmdMock.getId().toString());
        multiValueMap.add("description", recipeCmdMock.getDescription());

        // Then
        mockMvc.perform(post("/recipe")
                    .params(multiValueMap))
                .andExpect(status().is3xxRedirection())
                // Model-Attribute not present in this case.Why???
//                .andExpect(model().attributeExists("recipe"))
//                .andExpect(model().attribute("recipe",recipeMock))                .andExpect(view().name("redirect:1"+"/show"))
                .andDo(print());
    }

    @Test
    public void testUpdateRecipe() throws Exception {
        // Given
        RecipeCommand recipeCmdMock = new RecipeCommand();
        recipeCmdMock.setId(3L);
        recipeCmdMock.setDescription("Recipe Mock 3");

        // When
        when(recipeSrvMock.findCommandById(anyLong())).thenReturn(recipeCmdMock);

        // Then
        mockMvc.perform(get("/recipe/3/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andDo(print());
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        // No Given since void-Method

        // When
        mockMvc.perform(get("/recipe/2/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andDo(print());

        // Then
        verify(recipeSrvMock).deleteById(2L);
    }
}