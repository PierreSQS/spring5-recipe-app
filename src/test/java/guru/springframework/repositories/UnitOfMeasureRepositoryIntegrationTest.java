package guru.springframework.repositories;

import guru.springframework.domain.UnitOfMeasure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class UnitOfMeasureRepositoryIntegrationTest {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepo;

    @Test
    public void findByDescription() {
        Optional<UnitOfMeasure> pinch = unitOfMeasureRepo.findByDescription("Pinch");
        assertEquals("Pinch",pinch.get().getDescription());
    }
}