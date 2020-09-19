package ir.afarinesh.realhope.modules.generation.features.crud.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.domain.SelectEntity;
import ir.afarinesh.realhope.core.domain.SelectEnum;
import ir.afarinesh.realhope.core.usecase.UseCaseFruit;
import ir.afarinesh.realhope.core.usecase.UseCaseFruitSeeds;
import ir.afarinesh.realhope.core.usecase.UseCasePlant;
import ir.afarinesh.realhope.core.usecase.UseCaseSeedsCommand;
import ir.afarinesh.realhope.entities.code_generation.CrudCodeGeneration;
import ir.afarinesh.realhope.entities.data_model.DataEntityAttribute;
import ir.afarinesh.realhope.entities.feature.*;
import ir.afarinesh.realhope.entities.feature.enums.*;
import ir.afarinesh.realhope.modules.generation.features.crud.application.ports.in.GenerateCrudByProjectManager;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetFruitException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetPlantException;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.AddNewUseCaseByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application.ports.in.AddNewUseCaseDataAttributeByProjectManagerUseCase;
import ir.afarinesh.realhope.shares.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FeatureApplication
public class GenerateCrudByProjectManagerService implements GenerateCrudByProjectManager {
    final CrudCodeGenerationSpringJpaRepository crudCodeGenerationSpringJpaRepository;
    final DataEntitySpringJpaRepository dataEntitySpringJpaRepository;
    final DataEntityAttributeSpringJpaRepository dataEntityAttributeSpringJpaRepository;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;
    final DomainEntityAttributeSpringJpaRepository domainEntityAttributeSpringJpaRepository;
    final UseCaseSpringJpaRepository useCaseSpringJpaRepository;
    final UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository;
    final UseCaseDataAttributeSpringJpaRepository useCaseDataAttributeSpringJpaRepository;
    final UseCaseRelationSpringJpaRepository useCaseRelationSpringJpaRepository;
    final UseCaseService useCaseService;
    final AddNewUseCaseByProjectManagerUseCase addNewUseCaseByProjectManagerUseCase;
    final AddNewUseCaseDataAttributeByProjectManagerUseCase addNewUseCaseDataAttributeByProjectManagerUseCase;

    public GenerateCrudByProjectManagerService(CrudCodeGenerationSpringJpaRepository crudCodeGenerationSpringJpaRepository,
                                               DataEntitySpringJpaRepository dataEntitySpringJpaRepository,
                                               DataEntityAttributeSpringJpaRepository dataEntityAttributeSpringJpaRepository,
                                               DomainEntitySpringJpaRepository domainEntitySpringJpaRepository,
                                               DomainEntityAttributeSpringJpaRepository domainEntityAttributeSpringJpaRepository,
                                               AddNewUseCaseByProjectManagerUseCase addNewUseCaseByProjectManagerUseCase,
                                               UseCaseSpringJpaRepository useCaseSpringJpaRepository,
                                               UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository,
                                               UseCaseDataAttributeSpringJpaRepository useCaseDataAttributeSpringJpaRepository,
                                               UseCaseRelationSpringJpaRepository useCaseRelationSpringJpaRepository,
                                               UseCaseService useCaseService,
                                               AddNewUseCaseDataAttributeByProjectManagerUseCase addNewUseCaseDataAttributeByProjectManagerUseCase) {
        this.crudCodeGenerationSpringJpaRepository = crudCodeGenerationSpringJpaRepository;
        this.dataEntitySpringJpaRepository = dataEntitySpringJpaRepository;
        this.dataEntityAttributeSpringJpaRepository = dataEntityAttributeSpringJpaRepository;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
        this.domainEntityAttributeSpringJpaRepository = domainEntityAttributeSpringJpaRepository;
        this.addNewUseCaseByProjectManagerUseCase = addNewUseCaseByProjectManagerUseCase;
        this.useCaseSpringJpaRepository = useCaseSpringJpaRepository;
        this.useCaseDataSpringJpaRepository = useCaseDataSpringJpaRepository;
        this.useCaseDataAttributeSpringJpaRepository = useCaseDataAttributeSpringJpaRepository;
        this.useCaseRelationSpringJpaRepository = useCaseRelationSpringJpaRepository;
        this.useCaseService = useCaseService;
        this.addNewUseCaseDataAttributeByProjectManagerUseCase = addNewUseCaseDataAttributeByProjectManagerUseCase;
    }

    @Override
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        CrudCodeGeneration crudCodeGeneration = crudCodeGenerationSpringJpaRepository
                .findById(plant.getPlant().getCrudId())
                .orElseThrow(() -> new CultivateException("GenerateCrudByProjectManagerService: cannot find crud by id = " + plant.getPlant().getCrudId()));

        // Find the data entity attributes
        List<DataEntityAttribute> dataEntityAttributes = this.dataEntityAttributeSpringJpaRepository.findByDataEntity_Id(crudCodeGeneration.getDataEntity().getId());
        // create domain entity
        DomainEntity domainEntity = this.createDomainEntity(crudCodeGeneration, dataEntityAttributes);
        // create GridList use case
        UseCase gridListUseCase = this.createGridListUseCase(crudCodeGeneration, domainEntity, plant.getLocale());
        // create View use case
        UseCase viewUseCase = this.createViewUseCase(crudCodeGeneration, domainEntity, plant.getLocale());
        // create Update use case
        UseCase updateUseCase = this.createUpdateUseCase(crudCodeGeneration, domainEntity, plant.getLocale(), dataEntityAttributes);
        // create AddNew use case
        UseCase addNewUseCase = this.createAddNewUseCase(crudCodeGeneration, domainEntity, plant.getLocale(), dataEntityAttributes);
        // create Remove use case
        UseCase removeUseCase = this.createDeleteUseCase(crudCodeGeneration, domainEntity, plant.getLocale());

        return new UseCaseFruit<>(
                new Fruit(true),
                true,
                ""
        );
    }

    @Override
    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return null;
    }

    // Domain Entity
    private DomainEntity createDomainEntity(CrudCodeGeneration crudCodeGeneration, List<DataEntityAttribute> dataEntityAttributes) {
        // Delete domain entity and its' attributes if exist by crud id
        this.domainEntityAttributeSpringJpaRepository.deleteByDomainEntity_CrudCodeGeneration_Id(crudCodeGeneration.getId());
        this.domainEntitySpringJpaRepository.deleteByCrudCodeGeneration_Id(crudCodeGeneration.getId());
        // Create a new domain entity
        DomainEntity domainEntity = new DomainEntity(
                null,
                crudCodeGeneration.getDataEntity().getName() + "4" + crudCodeGeneration.getSoftwareRole().getName(),
                crudCodeGeneration.getDataEntity().getTitle() + " for " + crudCodeGeneration.getSoftwareRole().getTitle(),
                crudCodeGeneration.getDataEntity().getFaTitle() + " برای " + crudCodeGeneration.getSoftwareRole().getFaTitle(),
                "",
                null,
                crudCodeGeneration.getSoftwareApplicationPanel(),
                crudCodeGeneration.getSoftwareFeature(),
                true,
                crudCodeGeneration
        );
        // Save the new created domain entity
        this.domainEntitySpringJpaRepository.save(domainEntity);
        // For each data entity attribute create a new domain entity attribute
        for (DataEntityAttribute dataEntityAttribute : dataEntityAttributes) {
            DomainEntityAttribute domainEntityAttribute = new DomainEntityAttribute(
                    null,
                    dataEntityAttribute.getName(),
                    dataEntityAttribute.getTitle(),
                    dataEntityAttribute.getFaTitle(),
                    dataEntityAttribute.getDescription(),
                    dataEntityAttribute.getName(),
                    dataEntityAttribute.getUiRow(),
                    dataEntityAttribute.getUiColumn(),
                    dataEntityAttribute.getUiGridListShow(),
                    dataEntityAttribute.getPrimitiveAttributeType(),
                    dataEntityAttribute.getAttributeQuantity(),
                    dataEntityAttribute.getAttributeCategory(),
                    this.getDomainEntityAttributePrimitiveType(dataEntityAttribute),
                    null,
                    domainEntity
            );
            // Save domain entity attribute
            this.domainEntityAttributeSpringJpaRepository.save(domainEntityAttribute);
        }
        return domainEntity;
    }

    // Grid List
    private UseCase createGridListUseCase(CrudCodeGeneration crudCodeGeneration, DomainEntity domainEntity, String locale) throws CultivateException {
        // Create a new grid list use case
        UseCase useCase = this.createUseCase(crudCodeGeneration, domainEntity, locale, UserInterfaceTypeEnum.GridList);
        try {
            // Get the plant of use case
            UseCaseData plant = useCaseService.getPlant(useCase);
            // Get the fruit of use case
            UseCaseData fruit = useCaseService.getFruit(useCase);
            // Add a new use case data grid attribute
            this.addNewUseCaseDataAttributeByProjectManagerUseCase
                    .cultivate(new UseCasePlant<>(
                            new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                    null,
                                    "DataArray",
                                    "List of " + crudCodeGeneration.getDataEntity().getTitle(),
                                    "فهرست " + crudCodeGeneration.getDataEntity().getFaTitle(),
                                    "",
                                    0L,
                                    0L,
                                    new SelectEnum("", UseCaseUsageEnum.GridListEntity.name()),
                                    null,
                                    new SelectEnum("", EntityAttributeQuantityEnum.List.name()),
                                    null,
                                    new SelectEnum("", EntityAttributeCategoryEnum.DomainEntity.name()),
                                    null,
                                    new SelectEnum("", PrimitiveAttributeTypeEnum.Entity.name()),
                                    null,
                                    null,
                                    null,
                                    false,
                                    true,
                                    0L,
                                    100L,
                                    0L,
                                    0L,
                                    null,
                                    new SelectEntity("", domainEntity.getId()),
                                    null,
                                    new SelectEntity("", null),
                                    null,
                                    new SelectEntity("", fruit.getId()),
                                    null,
                                    new SelectEntity("", null),
                                    null,
                                    new SelectEntity("", null),
                                    null,
                                    new SelectEntity("", null),
                                    null
                            ),
                            locale
                    ));
        } catch (GetPlantException | GetFruitException | AddNewUseCaseDataAttributeByProjectManagerUseCase.CultivateException e) {
            throw new CultivateException(e.getMessage());
        }
        return useCase;
    }

    private UseCase createViewUseCase(CrudCodeGeneration crudCodeGeneration, DomainEntity domainEntity, String locale) throws CultivateException {
        UseCase useCase = this.createUseCase(crudCodeGeneration, domainEntity, locale, UserInterfaceTypeEnum.View);
        return useCase;
    }

    private UseCase createDeleteUseCase(CrudCodeGeneration crudCodeGeneration, DomainEntity domainEntity, String locale) throws CultivateException {
        UseCase useCase = this.createUseCase(crudCodeGeneration, domainEntity, locale, UserInterfaceTypeEnum.Delete);
        return useCase;
    }

    private UseCase createAddNewUseCase(CrudCodeGeneration crudCodeGeneration, DomainEntity domainEntity, String locale, List<DataEntityAttribute> dataEntityAttributes) throws CultivateException {
        UseCase useCase = this.createUseCase(crudCodeGeneration, domainEntity, locale, UserInterfaceTypeEnum.AddNew);
        return useCase;
    }

    private UseCase createUpdateUseCase(CrudCodeGeneration crudCodeGeneration, DomainEntity domainEntity, String locale, List<DataEntityAttribute> dataEntityAttributes) throws CultivateException {
        UseCase useCase = this.createUseCase(crudCodeGeneration, domainEntity, locale, UserInterfaceTypeEnum.Update);
        return useCase;
    }


    protected UseCase createUseCase(CrudCodeGeneration crudCodeGeneration, DomainEntity domainEntity, String locale, UserInterfaceTypeEnum userInterfaceType) throws CultivateException {
        // Delete use case, use case data, use case data attributes, and use case relation by crud id
        this.useCaseRelationSpringJpaRepository.deleteByCrudCodeGeneration_Id(crudCodeGeneration.getId());
        this.useCaseDataAttributeSpringJpaRepository.deleteByUseCaseData_UseCase_CrudCodeGeneration_Id(crudCodeGeneration.getId());
        this.useCaseDataSpringJpaRepository.deleteByUseCase_CrudCodeGeneration_Id(crudCodeGeneration.getId());
        this.useCaseSpringJpaRepository.deleteByCrudCodeGeneration_Id(crudCodeGeneration.getId());
        // Add a new use case
        try {
            Long useCaseId = this.addNewUseCaseByProjectManagerUseCase
                    .cultivate(new UseCasePlant<>(
                            new AddNewUseCaseByProjectManagerUseCase.Plant(
                                    null,
                                    "List" + crudCodeGeneration.getDataEntity().getName() + "By" + crudCodeGeneration.getSoftwareRole().getName(),
                                    "List " + crudCodeGeneration.getDataEntity().getTitle() + " by " + crudCodeGeneration.getSoftwareRole().getTitle(),
                                    "فهرست" + crudCodeGeneration.getDataEntity().getFaTitle() + " به دست " + crudCodeGeneration.getSoftwareRole().getFaTitle(),
                                    "",
                                    new SelectEnum("", userInterfaceType.name()),
                                    null,
                                    new SelectEntity("", crudCodeGeneration.getSoftwareFeature().getId()),
                                    null,
                                    new SelectEntity("", crudCodeGeneration.getSoftwareApplicationPanel().getId()),
                                    null,
                                    new SelectEntity("", crudCodeGeneration.getSoftwareRole().getId()),
                                    null,
                                    new SelectEntity("", crudCodeGeneration.getDataEntity().getId()),
                                    null,
                                    true
                            ),
                            locale
                    ))
                    .getFruit()
                    .getNewUseCaseId();
            return this.useCaseSpringJpaRepository.findById(useCaseId).orElseThrow();
        } catch (AddNewUseCaseByProjectManagerUseCase.CultivateException e) {
            throw new CultivateException(e.getMessage());
        }
    }

    protected PrimitiveAttributeTypeEnum getDomainEntityAttributePrimitiveType(DataEntityAttribute dataEntityAttribute) {
        switch (dataEntityAttribute.getPrimitiveAttributeType()) {
            case Long:
            case Integer: {
                return PrimitiveAttributeTypeEnum.Long;
            }
            case Boolean: {
                return PrimitiveAttributeTypeEnum.Boolean;
            }
        }
        return PrimitiveAttributeTypeEnum.String;
    }

}
