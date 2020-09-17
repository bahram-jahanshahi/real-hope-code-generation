package ir.afarinesh.realhope.modules.software_design.features.use_case.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.shares.repositories.UseCaseSpringJpaRepository;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.ViewUseCaseByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.ViewUseCaseByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.software_design.features.use_case.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import org.springframework.stereotype.Service;


@Service
public class ViewUseCaseByProjectManagerService {

    // jpa repositories
    final UseCaseSpringJpaRepository useCaseSpringJpaRepository;

    public ViewUseCaseByProjectManagerService(UseCaseSpringJpaRepository useCaseSpringJpaRepository) {
        this.useCaseSpringJpaRepository = useCaseSpringJpaRepository;
    }

    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        UseCase entity =
                this.useCaseSpringJpaRepository.findById(plant.getPlant().getId())
                .orElseThrow(() -> new CultivateException("Cannot find by id = " + plant.getPlant().getId()));
        return new UseCaseFruit<>(
            new Fruit(
                    this.mapUseCase4ProjectManager(entity, plant.getLocale())
            ),
            true,
            ""
        );
    }

    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return null;
    }


    public UseCase4ProjectManager mapUseCase4ProjectManager(UseCase entity, String locale){
        return new UseCase4ProjectManager(
                entity.getId(),
                entity.getName(),
                entity.getTitle(),
                entity.getFaTitle(),
                entity.getDescription(),
                (entity.getUserInterfaceType() != null) ? entity.getUserInterfaceType().title(locale) : null,
                (entity.getSoftwareFeature() != null && entity.getSoftwareFeature().getSoftwareModule() != null && entity.getSoftwareFeature().getSoftwareModule().getSoftware() != null) ? entity.getSoftwareFeature().getSoftwareModule().getSoftware().title(locale) : null,
                (entity.getSoftwareFeature() != null && entity.getSoftwareFeature().getSoftwareModule() != null) ? entity.getSoftwareFeature().getSoftwareModule().title(locale) : null,
                (entity.getSoftwareFeature() != null) ? entity.getSoftwareFeature().title(locale) : null,
                (entity.getSoftwareApplicationPanel() != null) ? entity.getSoftwareApplicationPanel().title(locale) : null,
                (entity.getSoftwareRole() != null) ? entity.getSoftwareRole().title(locale) : null,
                (entity.getDataEntity() != null) ? entity.getDataEntity().title(locale) : null,
                entity.getGenerationEnable()
        );
    }
}