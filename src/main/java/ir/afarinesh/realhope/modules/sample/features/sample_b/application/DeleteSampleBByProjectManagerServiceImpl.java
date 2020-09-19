package ir.afarinesh.realhope.modules.sample.features.sample_b.application;
import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.modules.sample.features.sample_b.application.ports.in.DeleteSampleBByProjectManagerUseCase;
import ir.afarinesh.realhope.core.usecase.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FeatureApplication
public class DeleteSampleBByProjectManagerServiceImpl implements DeleteSampleBByProjectManagerUseCase {

    final DeleteSampleBByProjectManagerService service;

    public DeleteSampleBByProjectManagerServiceImpl(DeleteSampleBByProjectManagerService service) {
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