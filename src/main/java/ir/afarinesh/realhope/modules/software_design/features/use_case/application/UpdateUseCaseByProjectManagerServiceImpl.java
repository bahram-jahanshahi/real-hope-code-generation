package ir.afarinesh.realhope.modules.software_design.features.use_case.application;
import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.UpdateUseCaseByProjectManagerUseCase;
import ir.afarinesh.realhope.core.usecase.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FeatureApplication
public class UpdateUseCaseByProjectManagerServiceImpl implements UpdateUseCaseByProjectManagerUseCase {

    final UpdateUseCaseByProjectManagerService service;

    public UpdateUseCaseByProjectManagerServiceImpl(UpdateUseCaseByProjectManagerService service) {
        this.service = service;
    }

    @Override
    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        return this.service.cultivate(plant);
    }

    @Override
    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return this.service.prepare(seedsCommand);
    }
}