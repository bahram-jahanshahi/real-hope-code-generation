package ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application;
import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.entities.data_model.DataEntity;
import ir.afarinesh.realhope.entities.data_model.DataEntityAttribute;
import ir.afarinesh.realhope.entities.data_model.DataEnum;
import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.entities.feature.enums.*;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application.ports.in.AddNewUseCaseDataAttributeByProjectManagerUseCase;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.shares.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@FeatureApplication
public class AddNewUseCaseDataAttributeByProjectManagerServiceImpl implements AddNewUseCaseDataAttributeByProjectManagerUseCase {

    final AddNewUseCaseDataAttributeByProjectManagerService service;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;
    final DataEntitySpringJpaRepository dataEntitySpringJpaRepository;
    final UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository;
    final UseCaseDataAttributeSpringJpaRepository useCaseDataAttributeSpringJpaRepository;
    final DataEnumSpringJpaRepository dataEnumSpringJpaRepository;
    final DataEntityAttributeSpringJpaRepository dataEntityAttributeSpringJpaRepository;

    public AddNewUseCaseDataAttributeByProjectManagerServiceImpl(AddNewUseCaseDataAttributeByProjectManagerService service,
                                                                 DomainEntitySpringJpaRepository domainEntitySpringJpaRepository,
                                                                 DataEntitySpringJpaRepository dataEntitySpringJpaRepository,
                                                                 UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository,
                                                                 UseCaseDataAttributeSpringJpaRepository useCaseDataAttributeSpringJpaRepository,
                                                                 DataEnumSpringJpaRepository dataEnumSpringJpaRepository,
                                                                 DataEntityAttributeSpringJpaRepository dataEntityAttributeSpringJpaRepository) {
        this.service = service;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
        this.dataEntitySpringJpaRepository = dataEntitySpringJpaRepository;
        this.useCaseDataSpringJpaRepository = useCaseDataSpringJpaRepository;
        this.useCaseDataAttributeSpringJpaRepository = useCaseDataAttributeSpringJpaRepository;
        this.dataEnumSpringJpaRepository = dataEnumSpringJpaRepository;
        this.dataEntityAttributeSpringJpaRepository = dataEntityAttributeSpringJpaRepository;
    }

    @Override
    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        DomainEntity domainEntityAttributeType = null;
        if (plant.getPlant().getDomainEntityAttributeType().getValue() != null) {
            domainEntityAttributeType = domainEntitySpringJpaRepository
                    .findById(plant.getPlant().getDomainEntityAttributeType().getValue())
                    .orElseThrow();
        }
        DataEntity dataEntityAttributeType = null;
        if (plant.getPlant().getDataEntityAttributeType().getValue() != null) {
            dataEntityAttributeType = dataEntitySpringJpaRepository
                    .findById(plant.getPlant().getDataEntityAttributeType().getValue())
                    .orElseThrow();
        }
        UseCaseData useCaseData = null;
        if (plant.getPlant().getUseCaseData().getValue() != null) {
            useCaseData = useCaseDataSpringJpaRepository
                    .findById(plant.getPlant().getUseCaseData().getValue())
                    .orElseThrow();
        }
        UseCaseDataAttribute fruitSeedsAttribute = null;
        if (plant.getPlant().getFruitSeedsAttribute().getValue() != null) {
            fruitSeedsAttribute = useCaseDataAttributeSpringJpaRepository
                    .findById(plant.getPlant().getUseCaseData().getValue())
                    .orElseThrow();
        }
        DataEnum dataEnum = null;
        if (plant.getPlant().getDataEnum().getValue() != null) {
            dataEnum = dataEnumSpringJpaRepository
                    .findById(plant.getPlant().getDataEnum().getValue())
                    .orElseThrow();
        }
        DataEntityAttribute relatedDataEntityAttribute = null;
        if (plant.getPlant().getRelatedDataEntityAttribute().getValue() != null) {
            relatedDataEntityAttribute = dataEntityAttributeSpringJpaRepository
                    .findById(plant.getPlant().getRelatedDataEntityAttribute().getValue())
                    .orElseThrow();
        }
        // Create
        UseCaseDataAttribute attribute = new UseCaseDataAttribute(
                null,
                plant.getPlant().getName().trim(),
                plant.getPlant().getTitle().trim(),
                plant.getPlant().getFaTitle().trim(),
                plant.getPlant().getDescription().trim(),
                plant.getPlant().getUiRow(),
                plant.getPlant().getUiColumn(),
                UseCaseUsageEnum.findByName(plant.getPlant().getUseCaseUsageEnumEnum().getValue()),
                EntityAttributeQuantityEnum.findByName(plant.getPlant().getAttributeQuantityEnum().getValue()),
                EntityAttributeCategoryEnum.findByName(plant.getPlant().getAttributeCategoryEnum().getValue()),
                PrimitiveAttributeTypeEnum.findByName(plant.getPlant().getPrimitiveAttributeTypeEnum().getValue()),
                plant.getPlant().getSetterOfUpdatePath(),
                plant.getPlant().getGetterOfUpdatePath(),
                plant.getPlant().getNullable(),
                plant.getPlant().getRequired(),
                plant.getPlant().getMinLength(),
                plant.getPlant().getMaxLength(),
                plant.getPlant().getMin(),
                plant.getPlant().getMax(),
                plant.getPlant().getErrorTip(),
                domainEntityAttributeType,
                dataEntityAttributeType,
                useCaseData,
                fruitSeedsAttribute,
                dataEnum,
                relatedDataEntityAttribute
        );
        // Save
        this.useCaseDataAttributeSpringJpaRepository.save(attribute);
        // If attribute is for Plant of AddNew or Update use case then copy it to the FruitSeeds one
        if (attribute.getUseCaseData().getUseCaseDataType().equals(UseCaseDataTypeEnum.Plant) &&
                (attribute.getUseCaseData().getUseCase().getUserInterfaceType().equals(UserInterfaceTypeEnum.AddNew) || attribute.getUseCaseData().getUseCase().getUserInterfaceType().equals(UserInterfaceTypeEnum.Update))) {
            UseCaseData fruitSeeds = this.useCaseDataSpringJpaRepository
                    .findFirstByUseCase_IdAndUseCaseDataType(attribute.getUseCaseData().getUseCase().getId(), UseCaseDataTypeEnum.FruitSeeds);
            // duplicate attribute
            if (fruitSeeds != null) {
                UseCaseDataAttribute duplicatedFruitSeedsAttribute = new UseCaseDataAttribute(
                        null,
                        plant.getPlant().getName().trim(),
                        plant.getPlant().getTitle().trim(),
                        plant.getPlant().getFaTitle().trim(),
                        plant.getPlant().getDescription().trim(),
                        plant.getPlant().getUiRow(),
                        plant.getPlant().getUiColumn(),
                        UseCaseUsageEnum.findByName(plant.getPlant().getUseCaseUsageEnumEnum().getValue()),
                        EntityAttributeQuantityEnum.findByName(plant.getPlant().getAttributeQuantityEnum().getValue()),
                        EntityAttributeCategoryEnum.findByName(plant.getPlant().getAttributeCategoryEnum().getValue()),
                        PrimitiveAttributeTypeEnum.findByName(plant.getPlant().getPrimitiveAttributeTypeEnum().getValue()),
                        plant.getPlant().getSetterOfUpdatePath(),
                        plant.getPlant().getGetterOfUpdatePath(),
                        plant.getPlant().getNullable(),
                        plant.getPlant().getRequired(),
                        plant.getPlant().getMinLength(),
                        plant.getPlant().getMaxLength(),
                        plant.getPlant().getMin(),
                        plant.getPlant().getMax(),
                        plant.getPlant().getErrorTip(),
                        domainEntityAttributeType,
                        dataEntityAttributeType,
                        fruitSeeds,
                        fruitSeedsAttribute,
                        dataEnum,
                        relatedDataEntityAttribute
                );
                this.useCaseDataAttributeSpringJpaRepository.save(duplicatedFruitSeedsAttribute);
                // set fruit seeds attribute
                attribute.setFruitSeedsAttribute(duplicatedFruitSeedsAttribute);
                // update plant attribute
                this.useCaseDataAttributeSpringJpaRepository.save(attribute);
            }
        }
        // Return fruit
        return new UseCaseFruit<>(
                new Fruit(true),
                true,
                ""
        );
    }

    @Override
    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return this.service.prepare(seedsCommand);
    }
}
