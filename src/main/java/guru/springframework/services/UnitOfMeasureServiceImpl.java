package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService{

    private final UnitOfMeasureRepository unitOfMeasureRepo;

    private final UnitOfMeasureToUnitOfMeasureCommand uomConverter;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepo, UnitOfMeasureToUnitOfMeasureCommand uomConverter) {
        this.unitOfMeasureRepo = unitOfMeasureRepo;
        this.uomConverter = uomConverter;
    }

    @Override
    public List<UnitOfMeasureCommand> findAllUomCommands() {
        List<UnitOfMeasure> unitOfMeasureList = new ArrayList<>();
        unitOfMeasureRepo.findAll().forEach(unitOfMeasureList::add);
        return unitOfMeasureList.stream().map(uomConverter::convert).collect(Collectors.toList());
    }
}
