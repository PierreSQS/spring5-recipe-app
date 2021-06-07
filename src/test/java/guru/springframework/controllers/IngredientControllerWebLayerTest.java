package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(IngredientController.class)
public class IngredientControllerWebLayerTest {

    private IngredientCommand ingredientCmdMock;

    private List<UnitOfMeasureCommand> unitOfMeasureCmds;

    @MockBean
    IngredientService ingredientSrvMock;

    @MockBean
    RecipeService recipeSrvMock;

    @MockBean
    UnitOfMeasureService unitOfMeasureSrvMock;

    @Autowired
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        // Given
        // ---- IDS ----
        Long recipeId = 1L;
        Long ingredientId = 2L;

        // ---- UOMs Commands ---
        UnitOfMeasureCommand unitOfMeasureCmd1 = new UnitOfMeasureCommand();
        unitOfMeasureCmd1.setId(1L);
        unitOfMeasureCmd1.setDescription("Tee Spoon");

        UnitOfMeasureCommand unitOfMeasureCmd2 = new UnitOfMeasureCommand();
        unitOfMeasureCmd2.setId(2L);
        unitOfMeasureCmd2.setDescription("Pinch");

        UnitOfMeasureCommand unitOfMeasureCmd3 = new UnitOfMeasureCommand();
        unitOfMeasureCmd3.setId(3L);
        unitOfMeasureCmd3.setDescription("Ounce");

        // ---- Ingredient ---
        ingredientCmdMock = new IngredientCommand();
        ingredientCmdMock.setId(ingredientId);
        ingredientCmdMock.setRecipeId(recipeId);
        ingredientCmdMock.setDescription("Ingredient Mock");
        ingredientCmdMock.setAmount(new BigDecimal(3));
        ingredientCmdMock.setUom(unitOfMeasureCmd2);

        unitOfMeasureCmds =
                new ArrayList<>(Arrays.asList(unitOfMeasureCmd1,unitOfMeasureCmd2,unitOfMeasureCmd3));
    }

    @Test
    public void testListIngredients() throws Exception {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        when(recipeSrvMock.findCommandById(anyLong())).thenReturn(recipeCommand);

        //when
        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        //then
        verify(recipeSrvMock, times(1)).findCommandById(anyLong());
    }

    @Test
    public void testShowIngredient() throws Exception {
        //given
        UnitOfMeasureCommand uomCmd = new UnitOfMeasureCommand();
        uomCmd.setDescription("Pinch");
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setUom(uomCmd);

        //when
        when(ingredientSrvMock.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);

        //then
        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    public void testShowUpdateForm() throws Exception {

        when(ingredientSrvMock.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCmdMock);
        when(unitOfMeasureSrvMock.findAllUomCommands()).thenReturn(unitOfMeasureCmds);

        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ingredient","uomList"))
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(content().string(containsString("<h1 class=\"panel-title\">Edit Ingredient Information</h1>")))
                .andDo(print());

        verify(ingredientSrvMock).findByRecipeIdAndIngredientId(anyLong(),anyLong());
        verify(unitOfMeasureSrvMock).findAllUomCommands();

    }

}