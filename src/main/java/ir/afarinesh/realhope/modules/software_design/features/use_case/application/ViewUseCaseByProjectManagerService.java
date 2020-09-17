package ir.afarinesh.realhope.modules.software_design.features.use_case.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseDataTypeEnum;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.domain.UseCaseDataAttribute4ProjectManager;
import ir.afarinesh.realhope.shares.repositories.UseCaseSpringJpaRepository;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.ViewUseCaseByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.ViewUseCaseByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.software_design.features.use_case.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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
                entity.getGenerationEnable(),
                this.getAttributes(entity, UseCaseDataTypeEnum.Plant, locale),
                this.getAttributes(entity, UseCaseDataTypeEnum.Fruit, locale),
                this.getAttributes(entity, UseCaseDataTypeEnum.SeedsCommand, locale),
                this.getAttributes(entity, UseCaseDataTypeEnum.FruitSeeds, locale)
        );
    }

    protected List<UseCaseDataAttribute4ProjectManager> getAttributes(UseCase useCase, UseCaseDataTypeEnum useCaseDataType, String locale) {
        return useCase.getUseCaseDataSet()
                .stream()
                .filter(useCaseData -> useCaseData.getUseCaseDataType().equals(useCaseDataType))
                .findFirst()
                .orElseThrow()
                .getUseCaseDataAttributes()
                .stream()
                .map(attribute -> this.mapUseCaseDataAttribute4ProjectManager(attribute, locale))
                .collect(Collectors.toList());
    }

    public UseCaseDataAttribute4ProjectManager mapUseCaseDataAttribute4ProjectManager(UseCaseDataAttribute entity, String locale){
        return new UseCaseDataAttribute4ProjectManager(
                entity.getId(),
                entity.getUseCaseData().getUseCase().getName(),
                entity.getUseCaseData().getName(),
                entity.getName(),
                entity.getTitle(),
                entity.getFaTitle(),
                entity.getDescription(),
                (entity.getUseCaseUsageEnum() != null) ? entity.getUseCaseUsageEnum().title(locale) : null,
                (entity.getAttributeQuantity() != null) ? entity.getAttributeQuantity().title(locale) : null,
                (entity.getAttributeCategory() != null) ? entity.getAttributeCategory().title(locale) : null,
                (entity.getPrimitiveAttributeType() != null) ? entity.getPrimitiveAttributeType().title(locale) : null,
                entity.getSetterOfUpdatePath(),
                entity.getGetterOfUpdatePath(),
                (entity.getDomainEntityAttributeType() != null) ? entity.getDomainEntityAttributeType().title(locale) : null,
                (entity.getDataEntityAttributeType() != null) ? entity.getDataEntityAttributeType().title(locale) : null,
                (entity.getUseCaseData() != null) ? entity.getUseCaseData().title(locale) : null,
                (entity.getFruitSeedsAttribute() != null) ? entity.getFruitSeedsAttribute().title(locale) : null,
                (entity.getDataEnum() != null) ? entity.getDataEnum().title(locale) : null,
                entity.getNullable(),
                entity.getRequired(),
                entity.getMinLength(),
                entity.getMaxLength(),
                entity.getMin(),
                entity.getMax(),
                entity.getErrorTip()
        );
    }
}
