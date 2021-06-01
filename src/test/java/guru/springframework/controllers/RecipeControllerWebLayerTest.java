package guru.springframework.controllers;

import guru.springframework.commands.NotesCommand;
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    public void showById() throws Exception {
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
    public void showRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void saveOrUpdateRecipe() throws Exception {

        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setRecipeNotes("Recipe Notes");

        RecipeCommand recipeCommandMock = new RecipeCommand();
        recipeCommandMock.setId(1L);
        recipeCommandMock.setNotes(notesCommand);
        recipeCommandMock.setDescription("Recipe Description");

        when(recipeSrvMock.saveRecipeCommand(any())).thenReturn(recipeCommandMock);

        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", recipeCommandMock.getId().toString());
//        multiValueMap.add("notes", recipeCommandMock.getNotes().getRecipeNotes());
        multiValueMap.add("description", recipeCommandMock.getDescription());

//        mockMvc.perform(post("/recipe").params(multiValueMap))
        mockMvc.perform(post("/recipe").params(multiValueMap))
                .andExpect(status().is3xxRedirection())
//                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("redirect:1"+"/show"))
                .andDo(print());
    }
}