package ir.afarinesh.realhope.modules.sample.features.sample_b.application;
import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.modules.sample.features.sample_b.application.ports.in.AddNewSampleBByProjectManagerUseCase;
import ir.afarinesh.realhope.core.usecase.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FeatureApplication
public class AddNewSampleBByProjectManagerServiceImpl implements AddNewSampleBByProjectManagerUseCase {

    final AddNewSampleBByProjectManagerService service;

    public AddNewSampleBByProjectManagerServiceImpl(AddNewSampleBByProjectManagerService service) {
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