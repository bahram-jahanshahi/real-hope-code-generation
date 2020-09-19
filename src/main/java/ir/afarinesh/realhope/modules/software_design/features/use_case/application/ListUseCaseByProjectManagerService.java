package ir.afarinesh.realhope.modules.software_design.features.use_case.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.domain.SelectEnum;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.shares.repositories.UseCaseSpringJpaRepository;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.ListUseCaseByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.ListUseCaseByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.software_design.features.use_case.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;


import java.util.stream.Collectors;
import java.util.List;


@Service
@FeatureApplication
public class ListUseCaseByProjectManagerService {

    // jpa repositories
    final UseCaseSpringJpaRepository useCaseSpringJpaRepository;

    public ListUseCaseByProjectManagerService(UseCaseSpringJpaRepository useCaseSpringJpaRepository) {
        this.useCaseSpringJpaRepository = useCaseSpringJpaRepository;
    }

    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        Page<UseCase> page = this.useCaseSpringJpaRepository
                .findAll(PageRequest.of(plant.getPlant().getPaginationCommand().getPageIndex(), plant.getPlant().getPaginationCommand().getPageSize()));
        List<UseCase4ProjectManager> list = page
                .get()
                .map(entity -> this.mapUseCase4ProjectManager(entity, plant.getLocale()))
                .collect(Collectors.toList());
        return new UseCaseFruit<>(
                new Fruit(
                        list,
                        new PagedResultFruit(page.getTotalElements(), page.getTotalPages())
                ),
                true,
                ""
        );
    }

    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return new UseCaseFruitSeeds<>(
                new FruitSeeds(
                ),
                true,
                ""
        );    }

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
                entity.getGenerationEnable(),
                null,
                null,
                null,
                null
        );
    }
}
