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
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.AddNewUseCaseByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application.ports.in.AddNewUseCaseDataAttributeByProjectManagerUseCase;
import ir.afarinesh.realhope.shares.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        CrudCodeGeneration crudCodeGeneration = crudCodeGenerationSpringJpaRepository
                .findById(plant.getPlant().getCrudId())
                .orElseThrow(() -> new CultivateException("GenerateCrudByProjectManagerService: cannot find crud by id = " + plant.getPlant().getCrudId()));

        // Find the data entity attributes
        List<DataEntityAttribute> dataEntityAttributes = this.dataEntityAttributeSpringJpaRepository.findByDataEntity_Id(crudCodeGeneration.getDataEntity().getId());
        // Delete use case, use case data, use case data attributes, and use case relation by crud id
        this.useCaseRelationSpringJpaRepository.deleteByCrudCodeGeneration_Id(crudCodeGeneration.getId());
        this.useCaseDataAttributeSpringJpaRepository.deleteByUseCaseData_UseCase_CrudCodeGeneration_Id(crudCodeGeneration.getId());
        this.useCaseDataSpringJpaRepository.deleteByUseCase_CrudCodeGeneration_Id(crudCodeGeneration.getId());
        this.useCaseSpringJpaRepository.deleteByCrudCodeGeneration_Id(crudCodeGeneration.getId());
        // create domain entity
        DomainEntity domainEntity = this.createDomainEntity(crudCodeGeneration, dataEntityAttributes);

        // Use case: GridList
        UseCase gridListUseCase = this.createGridListUseCase(crudCodeGeneration, domainEntity, plant.getLocale(), dataEntityAttributes);
        // Use case: View
        UseCase viewUseCase = this.createViewUseCase(crudCodeGeneration, domainEntity, plant.getLocale());
        // Use case: Update
        UseCase updateUseCase = this.createUpdateUseCase(crudCodeGeneration, domainEntity, plant.getLocale(), dataEntityAttributes);
        // Use case: AddNew
        UseCase addNewUseCase = this.createAddNewUseCase(crudCodeGeneration, domainEntity, plant.getLocale(), dataEntityAttributes);
        // Use case: Delete
        UseCase deleteUseCase = this.createDeleteUseCase(crudCodeGeneration, domainEntity, plant.getLocale());

        // Relation: grid to view
        this.createRelation(crudCodeGeneration, gridListUseCase, viewUseCase, "View", "نمایش");
        // Relation: grid to add new
        this.createRelation(crudCodeGeneration, gridListUseCase, addNewUseCase, "AddNew", "افزودن");
        // Relation: view to update
        this.createRelation(crudCodeGeneration, viewUseCase, updateUseCase, "Update", "ویرایش");
        // Relation: view to delete
        this.createRelation(crudCodeGeneration, viewUseCase, deleteUseCase, "Delete", "حذف");

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

    // Grid List use case
    private UseCase createGridListUseCase(CrudCodeGeneration crudCodeGeneration, DomainEntity domainEntity, String locale, List<DataEntityAttribute> dataEntityAttributes) throws CultivateException {
        // Create a new grid list use case
        UseCase useCase = this.createUseCase(crudCodeGeneration, domainEntity, locale, UserInterfaceTypeEnum.GridList);
        // Get the plant of use case
        UseCaseData plant = this.getUseCaseData(useCase, UseCaseDataTypeEnum.Plant);
        // Get the fruit of use case
        UseCaseData fruit = this.getUseCaseData(useCase, UseCaseDataTypeEnum.Fruit);
        // Get the seeds command of use case
        UseCaseData seedsCommand = this.getUseCaseData(useCase, UseCaseDataTypeEnum.SeedsCommand);
        // Get the fruit seeds of use case
        UseCaseData fruitSeeds = this.getUseCaseData(useCase, UseCaseDataTypeEnum.FruitSeeds);

        // Plant
        for (DataEntityAttribute dataEntityAttribute : dataEntityAttributes) {
            try {
                String namePostfix = "";
                String titlePostfix = "";
                String faTitlePostfix = "";
                if (!dataEntityAttribute.getName().equals("Id") &&
                        (dataEntityAttribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.JavaDate) ||
                                dataEntityAttribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Long) ||
                                dataEntityAttribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Integer))
                ) {
                    namePostfix = "Begin";
                    titlePostfix = " begin";
                    faTitlePostfix = " از";
                }
                this.addNewUseCaseDataAttributeByProjectManagerUseCase
                        .cultivate(new UseCasePlant<>(
                                new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                        null,
                                        dataEntityAttribute.getName() + namePostfix,
                                        dataEntityAttribute.getTitle() + titlePostfix,
                                        dataEntityAttribute.getFaTitle() + faTitlePostfix,
                                        dataEntityAttribute.getDescription(),
                                        dataEntityAttribute.getUiRow(),
                                        dataEntityAttribute.getUiColumn(),
                                        new SelectEnum("", UseCaseUsageEnum.GridListSearchField.name()), null,
                                        new SelectEnum("", dataEntityAttribute.getAttributeQuantity().name()), null,
                                        new SelectEnum("", dataEntityAttribute.getAttributeCategory().name()), null,
                                        new SelectEnum("", dataEntityAttribute.getPrimitiveAttributeType().name()), null,
                                        null,
                                        null,
                                        true,
                                        false,
                                        dataEntityAttribute.getMinLength(),
                                        dataEntityAttribute.getMaxLength(),
                                        dataEntityAttribute.getMin(),
                                        dataEntityAttribute.getMax(),
                                        dataEntityAttribute.getErrorTip(),
                                        new SelectEntity("", null), null,
                                        new SelectEntity("", dataEntityAttribute.getDataEntityAttributeType() != null ? dataEntityAttribute.getDataEntityAttributeType().getId() : null), null,
                                        new SelectEntity("", plant.getId()), null,
                                        new SelectEntity("", null), null,
                                        new SelectEntity("", dataEntityAttribute.getDataEnum() != null ? dataEntityAttribute.getDataEnum().getId() : null), null,
                                        new SelectEntity("", null), null
                                ),
                                locale
                        ));
                if (!dataEntityAttribute.getName().equals("Id") &&
                        (dataEntityAttribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.JavaDate) ||
                                dataEntityAttribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Long) ||
                                dataEntityAttribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Integer))
                ) {
                    this.addNewUseCaseDataAttributeByProjectManagerUseCase
                            .cultivate(new UseCasePlant<>(
                                    new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                            null,
                                            dataEntityAttribute.getName() + "End",
                                            dataEntityAttribute.getTitle() + " end",
                                            dataEntityAttribute.getFaTitle() + " تا",
                                            dataEntityAttribute.getDescription(),
                                            dataEntityAttribute.getUiRow(),
                                            dataEntityAttribute.getUiColumn() + 1,
                                            new SelectEnum("", UseCaseUsageEnum.GridListSearchField.name()), null,
                                            new SelectEnum("", dataEntityAttribute.getAttributeQuantity().name()), null,
                                            new SelectEnum("", dataEntityAttribute.getAttributeCategory().name()), null,
                                            new SelectEnum("", dataEntityAttribute.getPrimitiveAttributeType().name()), null,
                                            null,
                                            null,
                                            true,
                                            false,
                                            dataEntityAttribute.getMinLength(),
                                            dataEntityAttribute.getMaxLength(),
                                            dataEntityAttribute.getMin(),
                                            dataEntityAttribute.getMax(),
                                            dataEntityAttribute.getErrorTip(),
                                            new SelectEntity("", null), null,
                                            new SelectEntity("", dataEntityAttribute.getDataEntityAttributeType() != null ? dataEntityAttribute.getDataEntityAttributeType().getId() : null), null,
                                            new SelectEntity("", plant.getId()), null,
                                            new SelectEntity("", null), null,
                                            new SelectEntity("", dataEntityAttribute.getDataEnum() != null ? dataEntityAttribute.getDataEnum().getId() : null), null,
                                            new SelectEntity("", null), null
                                    ),
                                    locale
                            ));
                }
            } catch (AddNewUseCaseDataAttributeByProjectManagerUseCase.CultivateException e) {
                e.printStackTrace();
            }
        }
        // Fruit
        try {
            // Add a new DataArray use case data attribute
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
                                    new SelectEnum("", UseCaseUsageEnum.GridListEntity.name()), null,
                                    new SelectEnum("", EntityAttributeQuantityEnum.List.name()), null,
                                    new SelectEnum("", EntityAttributeCategoryEnum.DomainEntity.name()), null,
                                    new SelectEnum("", PrimitiveAttributeTypeEnum.Entity.name()), null,
                                    null,
                                    null,
                                    false,
                                    true,
                                    0L,
                                    100L,
                                    0L,
                                    0L,
                                    null,
                                    new SelectEntity("", domainEntity.getId()), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", fruit.getId()), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null
                            ),
                            locale
                    ));
        } catch (AddNewUseCaseDataAttributeByProjectManagerUseCase.CultivateException e) {
            throw new CultivateException(e.getMessage());
        }
        // FruitSeeds
        for (DataEntityAttribute dataEntityAttribute : dataEntityAttributes) {
            if (dataEntityAttribute.isSelectEnum() || dataEntityAttribute.isSelectEntity()) {
                try {
                    this.addNewUseCaseDataAttributeByProjectManagerUseCase
                            .cultivate(new UseCasePlant<>(
                                    new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                            null,
                                            dataEntityAttribute.getName(),
                                            dataEntityAttribute.getTitle(),
                                            dataEntityAttribute.getFaTitle(),
                                            dataEntityAttribute.getDescription(),
                                            0L,
                                            0L,
                                            new SelectEnum("", UseCaseUsageEnum.GridListSearchField.name()), null,
                                            new SelectEnum("", dataEntityAttribute.getAttributeQuantity().name()), null,
                                            new SelectEnum("", dataEntityAttribute.getAttributeCategory().name()), null,
                                            new SelectEnum("", dataEntityAttribute.getPrimitiveAttributeType().name()), null,
                                            null,
                                            null,
                                            false,
                                            true,
                                            0L,
                                            100L,
                                            0L,
                                            0L,
                                            null,
                                            new SelectEntity("", null), null,
                                            new SelectEntity("", dataEntityAttribute.getDataEntityAttributeType() != null ? dataEntityAttribute.getDataEntityAttributeType().getId() : null), null,
                                            new SelectEntity("", fruitSeeds.getId()), null,
                                            new SelectEntity("", null), null,
                                            new SelectEntity("", dataEntityAttribute.getDataEnum() != null ? dataEntityAttribute.getDataEnum().getId() : null), null,
                                            new SelectEntity("", null), null
                                    ),
                                    locale
                            ));
                } catch (AddNewUseCaseDataAttributeByProjectManagerUseCase.CultivateException e) {
                    throw new CultivateException(e.getMessage());
                }
            }
        }
        return useCase;
    }

    // View use case
    private UseCase createViewUseCase(CrudCodeGeneration crudCodeGeneration, DomainEntity domainEntity, String locale) throws CultivateException {
        UseCase useCase = this.createUseCase(crudCodeGeneration, domainEntity, locale, UserInterfaceTypeEnum.View);
        try {
            // Get the plant of use case
            UseCaseData plant = this.getUseCaseData(useCase, UseCaseDataTypeEnum.Plant);
            // Get the fruit of use case
            UseCaseData fruit = this.getUseCaseData(useCase, UseCaseDataTypeEnum.Fruit);

            // Add a new ViewId use case data attribute to plant
            this.addNewUseCaseDataAttributeByProjectManagerUseCase
                    .cultivate(new UseCasePlant<>(
                            new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                    null,
                                    "Id",
                                    crudCodeGeneration.getDataEntity().getTitle() + " Id",
                                    "شناسه " + crudCodeGeneration.getDataEntity().getFaTitle(),
                                    "",
                                    0L,
                                    0L,
                                    new SelectEnum("", UseCaseUsageEnum.ViewId.name()), null,
                                    new SelectEnum("", EntityAttributeQuantityEnum.Mono.name()), null,
                                    new SelectEnum("", EntityAttributeCategoryEnum.Primitive.name()), null,
                                    new SelectEnum("", PrimitiveAttributeTypeEnum.Long.name()), null,
                                    null,
                                    null,
                                    false,
                                    true,
                                    0L,
                                    100L,
                                    0L,
                                    0L,
                                    null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", plant.getId()), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null
                            ),
                            locale
                    ));
            // Add a new ViewEntity use case data attribute to fruit
            this.addNewUseCaseDataAttributeByProjectManagerUseCase
                    .cultivate(new UseCasePlant<>(
                            new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                    null,
                                    "Entity",
                                    crudCodeGeneration.getDataEntity().getTitle(),
                                    crudCodeGeneration.getDataEntity().getFaTitle(),
                                    "",
                                    0L,
                                    0L,
                                    new SelectEnum("", UseCaseUsageEnum.ViewEntity.name()), null,
                                    new SelectEnum("", EntityAttributeQuantityEnum.Mono.name()), null,
                                    new SelectEnum("", EntityAttributeCategoryEnum.DomainEntity.name()), null,
                                    new SelectEnum("", PrimitiveAttributeTypeEnum.Entity.name()), null,
                                    null,
                                    null,
                                    false,
                                    true,
                                    0L,
                                    100L,
                                    0L,
                                    0L,
                                    null,
                                    new SelectEntity("", domainEntity.getId()), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", fruit.getId()), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null
                            ),
                            locale
                    ));
        } catch (AddNewUseCaseDataAttributeByProjectManagerUseCase.CultivateException e) {
            throw new CultivateException(e.getMessage());
        }
        return useCase;
    }

    // Delete use case
    private UseCase createDeleteUseCase(CrudCodeGeneration crudCodeGeneration, DomainEntity domainEntity, String locale) throws CultivateException {
        UseCase useCase = this.createUseCase(crudCodeGeneration, domainEntity, locale, UserInterfaceTypeEnum.Delete);
        // Get the plant of use case
        UseCaseData plant = this.getUseCaseData(useCase, UseCaseDataTypeEnum.Plant);
        // Get the fruit of use case
        UseCaseData fruit = this.getUseCaseData(useCase, UseCaseDataTypeEnum.Fruit);
        // Add a new DeleteId use case data attribute to plant
        try {
            this.addNewUseCaseDataAttributeByProjectManagerUseCase
                    .cultivate(new UseCasePlant<>(
                            new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                    null,
                                    "Id",
                                    crudCodeGeneration.getDataEntity().getTitle() + " Id",
                                    "شناسه " + crudCodeGeneration.getDataEntity().getFaTitle(),
                                    "",
                                    0L,
                                    0L,
                                    new SelectEnum("", UseCaseUsageEnum.DeleteId.name()), null,
                                    new SelectEnum("", EntityAttributeQuantityEnum.Mono.name()), null,
                                    new SelectEnum("", EntityAttributeCategoryEnum.Primitive.name()), null,
                                    new SelectEnum("", PrimitiveAttributeTypeEnum.Long.name()), null,
                                    null,
                                    null,
                                    false,
                                    true,
                                    0L,
                                    100L,
                                    0L,
                                    0L,
                                    null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", plant.getId()), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null
                            ),
                            locale
                    ));
            // Add a new DeleteResult use case data attribute to fruit
            this.addNewUseCaseDataAttributeByProjectManagerUseCase
                    .cultivate(new UseCasePlant<>(
                            new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                    null,
                                    "IsSuccessful",
                                    "IsSuccessful",
                                    "موفقیت آمیز بود؟",
                                    "",
                                    0L,
                                    0L,
                                    new SelectEnum("", UseCaseUsageEnum.DeleteResult.name()), null,
                                    new SelectEnum("", EntityAttributeQuantityEnum.Mono.name()), null,
                                    new SelectEnum("", EntityAttributeCategoryEnum.Primitive.name()), null,
                                    new SelectEnum("", PrimitiveAttributeTypeEnum.Boolean.name()), null,
                                    null,
                                    null,
                                    false,
                                    true,
                                    0L,
                                    100L,
                                    0L,
                                    0L,
                                    null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", fruit.getId()), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null
                            ),
                            locale
                    ));
        } catch (AddNewUseCaseDataAttributeByProjectManagerUseCase.CultivateException e) {
            throw new CultivateException(e.getMessage());
        }
        return useCase;
    }

    // AddNew use case
    private UseCase createAddNewUseCase(CrudCodeGeneration crudCodeGeneration, DomainEntity domainEntity, String locale, List<DataEntityAttribute> dataEntityAttributes) throws CultivateException {
        UseCase useCase = this.createUseCase(crudCodeGeneration, domainEntity, locale, UserInterfaceTypeEnum.AddNew);
        // Get the plant of use case
        UseCaseData plant = this.getUseCaseData(useCase, UseCaseDataTypeEnum.Plant);
        // Get the fruit of use case
        UseCaseData fruit = this.getUseCaseData(useCase, UseCaseDataTypeEnum.Fruit);
        // Get the seeds command of use case
        UseCaseData seedsCommand = this.getUseCaseData(useCase, UseCaseDataTypeEnum.SeedsCommand);
        // Get the fruit seeds of use case
        UseCaseData fruitSeeds = this.getUseCaseData(useCase, UseCaseDataTypeEnum.FruitSeeds);
        // Plant (and fruit seeds) Attributes
        for (DataEntityAttribute dataEntityAttribute : dataEntityAttributes) {
            try {
                this.addNewUseCaseDataAttributeByProjectManagerUseCase
                        .cultivate(new UseCasePlant<>(
                                new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                        null,
                                        dataEntityAttribute.getName(),
                                        dataEntityAttribute.getTitle(),
                                        dataEntityAttribute.getFaTitle(),
                                        dataEntityAttribute.getDescription(),
                                        dataEntityAttribute.getUiRow(),
                                        dataEntityAttribute.getUiColumn(),
                                        new SelectEnum("", dataEntityAttribute.getName().equals("Id") ? UseCaseUsageEnum.AddNewId.name() : UseCaseUsageEnum.AddNewField.name()), null,
                                        new SelectEnum("", dataEntityAttribute.getAttributeQuantity().name()), null,
                                        new SelectEnum("", dataEntityAttribute.getAttributeCategory().name()), null,
                                        new SelectEnum("", dataEntityAttribute.getPrimitiveAttributeType().name()), null,
                                        null,
                                        dataEntityAttribute.getName(),
                                        dataEntityAttribute.getNullable(),
                                        dataEntityAttribute.getRequired(),
                                        dataEntityAttribute.getMinLength(),
                                        dataEntityAttribute.getMaxLength(),
                                        dataEntityAttribute.getMin(),
                                        dataEntityAttribute.getMax(),
                                        dataEntityAttribute.getErrorTip(),
                                        new SelectEntity("", null), null,
                                        new SelectEntity("", dataEntityAttribute.getDataEntityAttributeType() != null ? dataEntityAttribute.getDataEntityAttributeType().getId() : null), null,
                                        new SelectEntity("", plant.getId()), null,
                                        new SelectEntity("", null), null,
                                        new SelectEntity("", dataEntityAttribute.getDataEnum() != null ? dataEntityAttribute.getDataEnum().getId() : null), null,
                                        new SelectEntity("", dataEntityAttribute.getId()), null
                                ),
                                locale
                        ));
            } catch (AddNewUseCaseDataAttributeByProjectManagerUseCase.CultivateException e) {
                throw new CultivateException(e.getMessage());
            }
        }
        // Fruit
        try {
            this.addNewUseCaseDataAttributeByProjectManagerUseCase
                    .cultivate(new UseCasePlant<>(
                            new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                    null,
                                    "Id",
                                    crudCodeGeneration.getDataEntity().getTitle() + " Id",
                                    "شناسه " + crudCodeGeneration.getDataEntity().getFaTitle(),
                                    "",
                                    0L,
                                    0L,
                                    new SelectEnum("", UseCaseUsageEnum.AddNewResult.name()), null,
                                    new SelectEnum("", EntityAttributeQuantityEnum.Mono.name()), null,
                                    new SelectEnum("", EntityAttributeCategoryEnum.Primitive.name()), null,
                                    new SelectEnum("", PrimitiveAttributeTypeEnum.Long.name()), null,
                                    null,
                                    null,
                                    false,
                                    true,
                                    0L,
                                    100L,
                                    0L,
                                    0L,
                                    null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", fruit.getId()), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null
                            ),
                            locale
                    ));
        } catch (AddNewUseCaseDataAttributeByProjectManagerUseCase.CultivateException e) {
            throw new CultivateException(e.getMessage());
        }
        // Seeds Command
        try {
            this.addNewUseCaseDataAttributeByProjectManagerUseCase
                    .cultivate(new UseCasePlant<>(
                            new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                    null,
                                    "Id",
                                    crudCodeGeneration.getDataEntity().getTitle() + " Id",
                                    "شناسه " + crudCodeGeneration.getDataEntity().getFaTitle(),
                                    "",
                                    0L,
                                    0L,
                                    new SelectEnum("", UseCaseUsageEnum.AddNewId.name()), null,
                                    new SelectEnum("", EntityAttributeQuantityEnum.Mono.name()), null,
                                    new SelectEnum("", EntityAttributeCategoryEnum.Primitive.name()), null,
                                    new SelectEnum("", PrimitiveAttributeTypeEnum.Long.name()), null,
                                    null,
                                    null,
                                    false,
                                    true,
                                    0L,
                                    100L,
                                    0L,
                                    0L,
                                    null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", seedsCommand.getId()), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null
                            ),
                            locale
                    ));
        } catch (AddNewUseCaseDataAttributeByProjectManagerUseCase.CultivateException e) {
            throw new CultivateException(e.getMessage());
        }

        // Return
        return useCase;
    }

    // Update use case
    private UseCase createUpdateUseCase(CrudCodeGeneration crudCodeGeneration, DomainEntity domainEntity, String locale, List<DataEntityAttribute> dataEntityAttributes) throws CultivateException {
        UseCase useCase = this.createUseCase(crudCodeGeneration, domainEntity, locale, UserInterfaceTypeEnum.Update);
        // Get the plant of use case
        UseCaseData plant = this.getUseCaseData(useCase, UseCaseDataTypeEnum.Plant);
        // Get the fruit of use case
        UseCaseData fruit = this.getUseCaseData(useCase, UseCaseDataTypeEnum.Fruit);
        // Get the seeds command of use case
        UseCaseData seedsCommand = this.getUseCaseData(useCase, UseCaseDataTypeEnum.SeedsCommand);
        // Get the fruit seeds of use case
        UseCaseData fruitSeeds = this.getUseCaseData(useCase, UseCaseDataTypeEnum.FruitSeeds);
        // Plant (and fruit seeds) Attributes
        for (DataEntityAttribute dataEntityAttribute : dataEntityAttributes) {
            try {
                this.addNewUseCaseDataAttributeByProjectManagerUseCase
                        .cultivate(new UseCasePlant<>(
                                new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                        null,
                                        dataEntityAttribute.getName(),
                                        dataEntityAttribute.getTitle(),
                                        dataEntityAttribute.getFaTitle(),
                                        dataEntityAttribute.getDescription(),
                                        dataEntityAttribute.getUiRow(),
                                        dataEntityAttribute.getUiColumn(),
                                        new SelectEnum("", dataEntityAttribute.getName().equals("Id") ? UseCaseUsageEnum.UpdateId.name() : UseCaseUsageEnum.UpdateField.name()), null,
                                        new SelectEnum("", dataEntityAttribute.getAttributeQuantity().name()), null,
                                        new SelectEnum("", dataEntityAttribute.getAttributeCategory().name()), null,
                                        new SelectEnum("", dataEntityAttribute.getPrimitiveAttributeType().name()), null,
                                        null,
                                        dataEntityAttribute.getName(),
                                        dataEntityAttribute.getNullable(),
                                        dataEntityAttribute.getRequired(),
                                        dataEntityAttribute.getMinLength(),
                                        dataEntityAttribute.getMaxLength(),
                                        dataEntityAttribute.getMin(),
                                        dataEntityAttribute.getMax(),
                                        dataEntityAttribute.getErrorTip(),
                                        new SelectEntity("", null), null,
                                        new SelectEntity("", dataEntityAttribute.getDataEntityAttributeType() != null ? dataEntityAttribute.getDataEntityAttributeType().getId() : null), null,
                                        new SelectEntity("", plant.getId()), null,
                                        new SelectEntity("", null), null,
                                        new SelectEntity("", dataEntityAttribute.getDataEnum() != null ? dataEntityAttribute.getDataEnum().getId() : null), null,
                                        new SelectEntity("", dataEntityAttribute.getId()), null
                                ),
                                locale
                        ));
            } catch (AddNewUseCaseDataAttributeByProjectManagerUseCase.CultivateException e) {
                throw new CultivateException(e.getMessage());
            }
        }
        // Fruit
        try {
            this.addNewUseCaseDataAttributeByProjectManagerUseCase
                    .cultivate(new UseCasePlant<>(
                            new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                    null,
                                    "Id",
                                    crudCodeGeneration.getDataEntity().getTitle() + " Id",
                                    "شناسه " + crudCodeGeneration.getDataEntity().getFaTitle(),
                                    "",
                                    0L,
                                    0L,
                                    new SelectEnum("", UseCaseUsageEnum.UpdateResult.name()), null,
                                    new SelectEnum("", EntityAttributeQuantityEnum.Mono.name()), null,
                                    new SelectEnum("", EntityAttributeCategoryEnum.Primitive.name()), null,
                                    new SelectEnum("", PrimitiveAttributeTypeEnum.Long.name()), null,
                                    null,
                                    null,
                                    false,
                                    true,
                                    0L,
                                    100L,
                                    0L,
                                    0L,
                                    null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", fruit.getId()), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null
                            ),
                            locale
                    ));
        } catch (AddNewUseCaseDataAttributeByProjectManagerUseCase.CultivateException e) {
            throw new CultivateException(e.getMessage());
        }
        // Seeds Command
        try {
            this.addNewUseCaseDataAttributeByProjectManagerUseCase
                    .cultivate(new UseCasePlant<>(
                            new AddNewUseCaseDataAttributeByProjectManagerUseCase.Plant(
                                    null,
                                    "Id",
                                    crudCodeGeneration.getDataEntity().getTitle() + " Id",
                                    "شناسه " + crudCodeGeneration.getDataEntity().getFaTitle(),
                                    "",
                                    0L,
                                    0L,
                                    new SelectEnum("", UseCaseUsageEnum.UpdateId.name()), null,
                                    new SelectEnum("", EntityAttributeQuantityEnum.Mono.name()), null,
                                    new SelectEnum("", EntityAttributeCategoryEnum.Primitive.name()), null,
                                    new SelectEnum("", PrimitiveAttributeTypeEnum.Long.name()), null,
                                    null,
                                    null,
                                    false,
                                    true,
                                    0L,
                                    100L,
                                    0L,
                                    0L,
                                    null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", seedsCommand.getId()), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null,
                                    new SelectEntity("", null), null
                            ),
                            locale
                    ));
        } catch (AddNewUseCaseDataAttributeByProjectManagerUseCase.CultivateException e) {
            throw new CultivateException(e.getMessage());
        }

        // Return
        return useCase;
    }

    // Create Use Case
    protected UseCase createUseCase(CrudCodeGeneration crudCodeGeneration, DomainEntity domainEntity, String locale, UserInterfaceTypeEnum userInterfaceType) throws CultivateException {
        // Add a new use case
        try {
            Long useCaseId = this.addNewUseCaseByProjectManagerUseCase
                    .cultivate(new UseCasePlant<>(
                            new AddNewUseCaseByProjectManagerUseCase.Plant(
                                    null,
                                    userInterfaceType.name() + crudCodeGeneration.getDataEntity().getName(),
                                    userInterfaceType.title("en") + crudCodeGeneration.getDataEntity().getTitle() + " by " + crudCodeGeneration.getSoftwareRole().getTitle(),
                                    userInterfaceType.title("fa") + crudCodeGeneration.getDataEntity().getFaTitle() + " به دست " + crudCodeGeneration.getSoftwareRole().getFaTitle(),
                                    "",
                                    new SelectEnum("", userInterfaceType.name()), null,
                                    new SelectEntity("", crudCodeGeneration.getSoftwareFeature().getId()), null,
                                    new SelectEntity("", crudCodeGeneration.getSoftwareApplicationPanel().getId()), null,
                                    new SelectEntity("", crudCodeGeneration.getSoftwareRole().getId()), null,
                                    new SelectEntity("", crudCodeGeneration.getDataEntity().getId()), null,
                                    true,
                                    new SelectEntity("", crudCodeGeneration.getId()), null
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

    // Relation GidList to View
    private void createRelation(CrudCodeGeneration crudCodeGeneration, UseCase source, UseCase destination, String relationName, String faRelationName) {
        this.useCaseRelationSpringJpaRepository.save(
                new UseCaseRelation(
                        null,
                        relationName + crudCodeGeneration.getDataEntity().getName() + "By" + crudCodeGeneration.getSoftwareRole().getName(),
                        relationName,
                        faRelationName,
                        UseCaseRelationContextEnum.Frontend,
                        FrontendActionTypeEnum.PopupForm,
                        source,
                        destination,
                        crudCodeGeneration
                )
        );
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

    private UseCaseData getUseCaseData(UseCase useCase, UseCaseDataTypeEnum type) {
        return this.useCaseDataSpringJpaRepository.findFirstByUseCase_IdAndUseCaseDataType(useCase.getId(), type);
    }

}
