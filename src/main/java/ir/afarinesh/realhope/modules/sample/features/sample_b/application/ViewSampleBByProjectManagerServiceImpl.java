package ir.afarinesh.realhope.modules.sample.features.sample_b.application;
import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.modules.sample.features.sample_b.application.ports.in.ViewSampleBByProjectManagerUseCase;
import ir.afarinesh.realhope.core.usecase.*;
import org.springframework.stereotype.Service;

@Service
@FeatureApplication
public class ViewSampleBByProjectManagerServiceImpl implements ViewSampleBByProjectManagerUseCase {

    final ViewSampleBByProjectManagerService service;

    public ViewSampleBByProjectManagerServiceImpl(ViewSampleBByProjectManagerService service) {
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