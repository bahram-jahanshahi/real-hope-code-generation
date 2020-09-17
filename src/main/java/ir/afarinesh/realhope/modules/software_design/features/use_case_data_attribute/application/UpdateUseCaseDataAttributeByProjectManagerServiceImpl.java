package ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application;
import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.entities.data_model.DataEntity;
import ir.afarinesh.realhope.entities.data_model.DataEnum;
import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeCategoryEnum;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeQuantityEnum;
import ir.afarinesh.realhope.entities.feature.enums.PrimitiveAttributeTypeEnum;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseUsageEnum;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application.ports.in.UpdateUseCaseDataAttributeByProjectManagerUseCase;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.shares.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FeatureApplication
public class UpdateUseCaseDataAttributeByProjectManagerServiceImpl implements UpdateUseCaseDataAttributeByProjectManagerUseCase {

    final UpdateUseCaseDataAttributeByProjectManagerService service;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;
    final DataEntitySpringJpaRepository dataEntitySpringJpaRepository;
    final UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository;
    final UseCaseDataAttributeSpringJpaRepository useCaseDataAttributeSpringJpaRepository;
    final DataEnumSpringJpaRepository dataEnumSpringJpaRepository;

    public UpdateUseCaseDataAttributeByProjectManagerServiceImpl(UpdateUseCaseDataAttributeByProjectManagerService service,
                                                                 DomainEntitySpringJpaRepository domainEntitySpringJpaRepository,
                                                                 DataEntitySpringJpaRepository dataEntitySpringJpaRepository,
                                                                 UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository,
                                                                 UseCaseDataAttributeSpringJpaRepository useCaseDataAttributeSpringJpaRepository,
                                                                 DataEnumSpringJpaRepository dataEnumSpringJpaRepository) {
        this.service = service;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
        this.dataEntitySpringJpaRepository = dataEntitySpringJpaRepository;
        this.useCaseDataSpringJpaRepository = useCaseDataSpringJpaRepository;
        this.useCaseDataAttributeSpringJpaRepository = useCaseDataAttributeSpringJpaRepository;
        this.dataEnumSpringJpaRepository = dataEnumSpringJpaRepository;
    }

    @Override
    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        UseCaseDataAttribute entity =
                this.useCaseDataAttributeSpringJpaRepository.findById(plant.getPlant().getId())
                        .orElseThrow(() -> new CultivateException("Cannot find by id = " + plant.getPlant().getId()));
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
        // Setters
        entity.setName(plant.getPlant().getName().trim());
        entity.setTitle(plant.getPlant().getTitle().trim());
        entity.setFaTitle(plant.getPlant().getFaTitle().trim());
        entity.setDescription(plant.getPlant().getDescription().trim());
        entity.setUiRow(plant.getPlant().getUiRow());
        entity.setUiColumn(plant.getPlant().getUiColumn());
        entity.setUseCaseUsageEnum(UseCaseUsageEnum.findByName(plant.getPlant().getUseCaseUsageEnumEnum().getValue()));
        entity.setAttributeQuantity(EntityAttributeQuantityEnum.findByName(plant.getPlant().getAttributeQuantityEnum().getValue()));
        entity.setAttributeCategory(EntityAttributeCategoryEnum.findByName(plant.getPlant().getAttributeCategoryEnum().getValue()));
        entity.setPrimitiveAttributeType(PrimitiveAttributeTypeEnum.findByName(plant.getPlant().getPrimitiveAttributeTypeEnum().getValue()));
        entity.setSetterOfUpdatePath(plant.getPlant().getSetterOfUpdatePath());
        entity.setGetterOfUpdatePath(plant.getPlant().getGetterOfUpdatePath());
        entity.setNullable(plant.getPlant().getNullable());
        entity.setRequired(plant.getPlant().getRequired());
        entity.setMinLength(plant.getPlant().getMinLength());
        entity.setMaxLength(plant.getPlant().getMaxLength());
        entity.setMin(plant.getPlant().getMin());
        entity.setMax(plant.getPlant().getMax());
        entity.setErrorTip(plant.getPlant().getErrorTip());
        entity.setDomainEntityAttributeType(domainEntityAttributeType);
        entity.setDataEntityAttributeType(dataEntityAttributeType);
        entity.setUseCaseData(useCaseData);
        entity.setFruitSeedsAttribute(fruitSeedsAttribute);
        entity.setDataEnum(dataEnum);
        // Update
        this.useCaseDataAttributeSpringJpaRepository.save(entity);
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
