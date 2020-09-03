package ir.afarinesh.realhope.modules.software_design.features.software.application;
import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.ViewSoftwareByProjectManagerUseCase;
import ir.afarinesh.realhope.core.usecase.*;
import org.springframework.stereotype.Service;

@Service
@FeatureApplication
public class ViewSoftwareByProjectManagerServiceImpl implements ViewSoftwareByProjectManagerUseCase {

    final ViewSoftwareByProjectManagerService service;

    public ViewSoftwareByProjectManagerServiceImpl(ViewSoftwareByProjectManagerService service) {
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