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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerSpringMVCTest {

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
        notes.setRecipeNotes("notes");
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

        // Then
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("id",recipeCmdMock.getId().toString());
        paramMap.add("description", recipeCmdMock.getDescription());
        mockMvc.perform(post("/recipe")
                .params(paramMap))
                .andExpect(status().is3xxRedirection())
                // Model-Attribute not present in this case.Why???
//                .andExpect(model().attributeExists("recipe"))
//                .andExpect(model().attribute("recipe",recipeMock))
                .andDo(print());
    }

    @Test
    public void testUpdateRecipe() throws Exception {

        RecipeCommand recipeCmdMock = new RecipeCommand();
        recipeCmdMock.setId(3L);
        recipeCmdMock.setDescription("Recipe Mock 3");

        when(recipeSrvMock.findCommandById(anyLong())).thenReturn(recipeCmdMock);

        mockMvc.perform(get("/recipe/3/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andDo(print());
    }
}