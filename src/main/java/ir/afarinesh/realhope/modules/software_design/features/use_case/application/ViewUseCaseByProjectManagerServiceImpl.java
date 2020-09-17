package ir.afarinesh.realhope.modules.software_design.features.use_case.application;
import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.ViewUseCaseByProjectManagerUseCase;
import ir.afarinesh.realhope.core.usecase.*;
import org.springframework.stereotype.Service;

@Service
@FeatureApplication
public class ViewUseCaseByProjectManagerServiceImpl implements ViewUseCaseByProjectManagerUseCase {

    final ViewUseCaseByProjectManagerService service;

    public ViewUseCaseByProjectManagerServiceImpl(ViewUseCaseByProjectManagerService service) {
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