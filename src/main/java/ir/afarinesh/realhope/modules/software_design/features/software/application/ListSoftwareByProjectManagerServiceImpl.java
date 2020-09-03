package ir.afarinesh.realhope.modules.software_design.features.software.application;
import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.ListSoftwareByProjectManagerUseCase;
import ir.afarinesh.realhope.core.usecase.*;
import org.springframework.stereotype.Service;

@Service
@FeatureApplication
public class ListSoftwareByProjectManagerServiceImpl implements ListSoftwareByProjectManagerUseCase {

    final ListSoftwareByProjectManagerService service;

    public ListSoftwareByProjectManagerServiceImpl(ListSoftwareByProjectManagerService service) {
        this.service = service;
    }

    @Override
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        return this.service.cultivate(plant);
    }

    @Override
    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return this.service.prepare(seedsCommand);
    }
}