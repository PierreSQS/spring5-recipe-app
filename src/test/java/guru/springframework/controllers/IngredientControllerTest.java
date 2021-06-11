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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class IngredientControllerTest {

    @Mock
    IngredientService ingredientSrvMock;

    @Mock
    RecipeService recipeSrvMock;

    @Mock
    UnitOfMeasureService unitOfMeasureCmdSrvMock;

    @InjectMocks
    IngredientController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
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
        IngredientCommand ingredientCommand = new IngredientCommand();

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
        // Given
        Long recipeId = 1L;
        Long ingredientId = 2L;
        IngredientCommand ingredientCmdMock = new IngredientCommand();
        ingredientCmdMock.setId(ingredientId);
        ingredientCmdMock.setRecipeId(recipeId);
        ingredientCmdMock.setDescription("Ingredient Mock");
        ingredientCmdMock.setAmount(new BigDecimal(3));

        UnitOfMeasureCommand unitOfMeasureCmd1 = new UnitOfMeasureCommand();
        unitOfMeasureCmd1.setDescription("Tee Spoon");

        UnitOfMeasureCommand unitOfMeasureCmd2 = new UnitOfMeasureCommand();
        unitOfMeasureCmd2.setDescription("Pinch");

        UnitOfMeasureCommand unitOfMeasureCmd3 = new UnitOfMeasureCommand();
        unitOfMeasureCmd3.setDescription("Ounce");

        List<UnitOfMeasureCommand> unitOfMeasureCmds =
                new ArrayList<>(Arrays.asList(unitOfMeasureCmd1,unitOfMeasureCmd2,unitOfMeasureCmd3));

        when(ingredientSrvMock.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCmdMock);
        when(unitOfMeasureCmdSrvMock.findAllUomCommands()).thenReturn(unitOfMeasureCmds);

        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ingredient","uomList"))
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andDo(print());

        verify(ingredientSrvMock).findByRecipeIdAndIngredientId(anyLong(),anyLong());
        verify(unitOfMeasureCmdSrvMock).findAllUomCommands();

    }

    @Test
    public void testSubmitIngredient() throws Exception {
        IngredientCommand ingrCmdMock = new IngredientCommand();
        ingrCmdMock.setId(1L);
        ingrCmdMock.setRecipeId(2L);

        ingrCmdMock.setDescription("Ingredient Mock");
        when(ingredientSrvMock.saveIngredientCommand(any())).thenReturn(ingrCmdMock);
        mockMvc.perform(post("/recipe/2/ingredient"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("redirect:/recipe/2/ingredient/1/show"))
                .andDo(print());
    }

}