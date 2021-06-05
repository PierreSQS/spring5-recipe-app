package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IngredientServiceImplTest {

    @Mock
    RecipeRepository recipeRepoMock;

    @Mock
    IngredientToIngredientCommand ingredientToIngredientCmdMock;


    IngredientService ingredientService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientService = new IngredientServiceImpl(recipeRepoMock,ingredientToIngredientCmdMock);
    }

    @Test
    public void testFindByRecipeIdAndIngredientId() {
        // Given
        Long recipeId = 2L;
        Long ingredientId = 2L;

        Ingredient ingredientMock = new Ingredient();
        ingredientMock.setId(ingredientId);
        ingredientMock.setDescription("Ingredient Mock");

        Recipe recipeMock = new Recipe();
        recipeMock.setId(recipeId);
        recipeMock.getIngredients().add(ingredientMock);

        IngredientCommand ingredientCmdMock = new IngredientCommand();
        ingredientCmdMock.setId(ingredientMock.getId());
        ingredientCmdMock.setDescription(ingredientMock.getDescription());

        // When
        when(recipeRepoMock.findById(anyLong())).thenReturn(Optional.of(recipeMock));
        when(ingredientToIngredientCmdMock.convert(any())).thenReturn(ingredientCmdMock);

        IngredientCommand ingCmdRepIdAndIngId = ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId);

        // Then
        assertThat(ingCmdRepIdAndIngId.getDescription()).isEqualTo("Ingredient Mock");
        assertThat(ingCmdRepIdAndIngId.getId()).isEqualTo(2L);

        verify(recipeRepoMock).findById(anyLong());
        verify(ingredientToIngredientCmdMock).convert(any());

    }
}