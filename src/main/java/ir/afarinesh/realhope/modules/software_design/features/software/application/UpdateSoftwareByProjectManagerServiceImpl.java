package ir.afarinesh.realhope.modules.software_design.features.software.application;
import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.UpdateSoftwareByProjectManagerUseCase;
import ir.afarinesh.realhope.core.usecase.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FeatureApplication
public class UpdateSoftwareByProjectManagerServiceImpl implements UpdateSoftwareByProjectManagerUseCase {

    final UpdateSoftwareByProjectManagerService service;

    public UpdateSoftwareByProjectManagerServiceImpl(UpdateSoftwareByProjectManagerService service) {
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