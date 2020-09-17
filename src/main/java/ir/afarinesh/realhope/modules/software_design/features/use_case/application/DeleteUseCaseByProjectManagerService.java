package ir.afarinesh.realhope.modules.software_design.features.use_case.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.shares.repositories.UseCaseSpringJpaRepository;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.DeleteUseCaseByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.DeleteUseCaseByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.software_design.features.use_case.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@FeatureApplication
public class DeleteUseCaseByProjectManagerService {

    // jpa repositories
    final UseCaseSpringJpaRepository useCaseSpringJpaRepository;

    public DeleteUseCaseByProjectManagerService(UseCaseSpringJpaRepository useCaseSpringJpaRepository) {
        this.useCaseSpringJpaRepository = useCaseSpringJpaRepository;
    }

    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        UseCase entity =
                this.useCaseSpringJpaRepository.findById(plant.getPlant().getId())
                .orElseThrow(() -> new CultivateException("Cannot find by id = " + plant.getPlant().getId()));
        this.useCaseSpringJpaRepository.delete(entity);
        return new UseCaseFruit<>(
            new Fruit(
                    true
            ),
            true,
            ""
        );
    }

    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return null;
    }
}