package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class UnitOfMeasureServiceImplJohnThomsonTest {

    UnitOfMeasureToUnitOfMeasureCommand uomToUomCmdConverter = new UnitOfMeasureToUnitOfMeasureCommand();

    @Mock
    UnitOfMeasureRepository uomRepoMock;

    UnitOfMeasureServiceImpl unitOfMeasureSrv;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        unitOfMeasureSrv = new UnitOfMeasureServiceImpl(uomRepoMock,uomToUomCmdConverter);
    }

    @Test
    public void testFindAllUomCommands() {
        // Given
        UnitOfMeasure uom1, uom2;

        uom1 = new UnitOfMeasure();
        uom2 = new UnitOfMeasure();
        uom1.setDescription("Pinch");
        uom2.setDescription("Ounce");
        List<UnitOfMeasure> unitOfMeasures = new ArrayList<>(Arrays.asList(uom1, uom2));

        when(uomRepoMock.findAll()).thenReturn(unitOfMeasures);

        // When
        List<UnitOfMeasureCommand> allUomCommands = unitOfMeasureSrv.findAllUomCommands();

        // Then
        assertThat(allUomCommands).hasSize(2);
        assertThat(allUomCommands.get(0).getDescription()).isEqualTo("Pinch");


    }
}