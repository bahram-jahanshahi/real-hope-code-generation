package ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application.ports.in.UpdateUseCaseDataAttributeByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application.ports.in.UpdateUseCaseDataAttributeByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import ir.afarinesh.realhope.core.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

import ir.afarinesh.realhope.shares.repositories.UseCaseDataAttributeSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.DataEnumSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.DataEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.UseCaseDataSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.DataEntityAttributeSpringJpaRepository;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseUsageEnum;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeQuantityEnum;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeCategoryEnum;
import ir.afarinesh.realhope.entities.feature.enums.PrimitiveAttributeTypeEnum;


@Service
public class UpdateUseCaseDataAttributeByProjectManagerService {

    // jpa repositories
    final UseCaseDataAttributeSpringJpaRepository useCaseDataAttributeSpringJpaRepository;
    final DataEnumSpringJpaRepository dataEnumSpringJpaRepository;
    final DataEntitySpringJpaRepository dataEntitySpringJpaRepository;
    final UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;
    final DataEntityAttributeSpringJpaRepository dataEntityAttributeSpringJpaRepository;

    public UpdateUseCaseDataAttributeByProjectManagerService(UseCaseDataAttributeSpringJpaRepository useCaseDataAttributeSpringJpaRepository, DataEnumSpringJpaRepository dataEnumSpringJpaRepository, DataEntitySpringJpaRepository dataEntitySpringJpaRepository, UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository, DomainEntitySpringJpaRepository domainEntitySpringJpaRepository, DataEntityAttributeSpringJpaRepository dataEntityAttributeSpringJpaRepository){
        this.useCaseDataAttributeSpringJpaRepository = useCaseDataAttributeSpringJpaRepository;
        this.dataEnumSpringJpaRepository = dataEnumSpringJpaRepository;
        this.dataEntitySpringJpaRepository = dataEntitySpringJpaRepository;
        this.useCaseDataSpringJpaRepository = useCaseDataSpringJpaRepository;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
        this.dataEntityAttributeSpringJpaRepository = dataEntityAttributeSpringJpaRepository;
    }

    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        // Entity
        UseCaseDataAttribute entity =
                this.useCaseDataAttributeSpringJpaRepository.findById(plant.getPlant().getId())
                    .orElseThrow(() -> new CultivateException("Cannot find by id = " + plant.getPlant().getId()));
        // Setters
        // Save or update
        this.useCaseDataAttributeSpringJpaRepository.save(entity);
        // Return
        return new UseCaseFruit<>(
            new Fruit(
                    entity.getId()
            ),
            true,
            ""
        );
    }

    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {

        UseCaseDataAttribute entity =
                this.useCaseDataAttributeSpringJpaRepository.findById(seedsCommand.getSeedsCommand().getId())
                .orElseThrow(() -> new PrepareException("Cannot find by id = " + seedsCommand.getSeedsCommand().getId()));
        Long id = entity.getId();
        String name = entity.getName();
        String title = entity.getTitle();
        String faTitle = entity.getFaTitle();
        String description = entity.getDescription();
        Long uiRow = entity.getUiRow();
        Long uiColumn = entity.getUiColumn();
        SelectEnum useCaseUsageEnumEnum = (entity.getUseCaseUsageEnum() != null) ? entity.getUseCaseUsageEnum().getSelectEnum(seedsCommand.getLocale()) :  new SelectEnum();
        List<SelectEnum> useCaseUsageEnumEnumList = UseCaseUsageEnum.Void.getSelectEnumList(seedsCommand.getLocale());
        SelectEnum attributeQuantityEnum = (entity.getAttributeQuantity() != null) ? entity.getAttributeQuantity().getSelectEnum(seedsCommand.getLocale()) :  new SelectEnum();
        List<SelectEnum> attributeQuantityEnumList = EntityAttributeQuantityEnum.Void.getSelectEnumList(seedsCommand.getLocale());
        SelectEnum attributeCategoryEnum = (entity.getAttributeCategory() != null) ? entity.getAttributeCategory().getSelectEnum(seedsCommand.getLocale()) :  new SelectEnum();
        List<SelectEnum> attributeCategoryEnumList = EntityAttributeCategoryEnum.Void.getSelectEnumList(seedsCommand.getLocale());
        SelectEnum primitiveAttributeTypeEnum = (entity.getPrimitiveAttributeType() != null) ? entity.getPrimitiveAttributeType().getSelectEnum(seedsCommand.getLocale()) :  new SelectEnum();
        List<SelectEnum> primitiveAttributeTypeEnumList = PrimitiveAttributeTypeEnum.Void.getSelectEnumList(seedsCommand.getLocale());
        String setterOfUpdatePath = entity.getSetterOfUpdatePath();
        String getterOfUpdatePath = entity.getGetterOfUpdatePath();
        Boolean nullable = entity.getNullable();
        Boolean required = entity.getRequired();
        Long minLength = entity.getMinLength();
        Long maxLength = entity.getMaxLength();
        Long min = entity.getMin();
        Long max = entity.getMax();
        String errorTip = entity.getErrorTip();
        SelectEntity domainEntityAttributeType = (entity.getDomainEntityAttributeType() != null) ? new SelectEntity(entity.getDomainEntityAttributeType().title(seedsCommand.getLocale()), entity.getDomainEntityAttributeType().getId()) :  new SelectEntity();
        List<SelectEntity> domainEntityAttributeTypeList = this.domainEntitySpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity dataEntityAttributeType = (entity.getDataEntityAttributeType() != null) ? new SelectEntity(entity.getDataEntityAttributeType().title(seedsCommand.getLocale()), entity.getDataEntityAttributeType().getId()) :  new SelectEntity();
        List<SelectEntity> dataEntityAttributeTypeList = this.dataEntitySpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity useCaseData = (entity.getUseCaseData() != null) ? new SelectEntity(entity.getUseCaseData().title(seedsCommand.getLocale()), entity.getUseCaseData().getId()) :  new SelectEntity();
        List<SelectEntity> useCaseDataList = this.useCaseDataSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity fruitSeedsAttribute = (entity.getFruitSeedsAttribute() != null) ? new SelectEntity(entity.getFruitSeedsAttribute().title(seedsCommand.getLocale()), entity.getFruitSeedsAttribute().getId()) :  new SelectEntity();
        List<SelectEntity> fruitSeedsAttributeList = this.useCaseDataAttributeSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity dataEnum = (entity.getDataEnum() != null) ? new SelectEntity(entity.getDataEnum().title(seedsCommand.getLocale()), entity.getDataEnum().getId()) :  new SelectEntity();
        List<SelectEntity> dataEnumList = this.dataEnumSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity relatedDataEntityAttribute = (entity.getRelatedDataEntityAttribute() != null) ? new SelectEntity(entity.getRelatedDataEntityAttribute().title(seedsCommand.getLocale()), entity.getRelatedDataEntityAttribute().getId()) :  new SelectEntity();
        List<SelectEntity> relatedDataEntityAttributeList = this.dataEntityAttributeSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());

        return new UseCaseFruitSeeds<>(
            new FruitSeeds(
                id,
                name,
                title,
                faTitle,
                description,
                uiRow,
                uiColumn,
                useCaseUsageEnumEnum,
                useCaseUsageEnumEnumList,
                attributeQuantityEnum,
                attributeQuantityEnumList,
                attributeCategoryEnum,
                attributeCategoryEnumList,
                primitiveAttributeTypeEnum,
                primitiveAttributeTypeEnumList,
                setterOfUpdatePath,
                getterOfUpdatePath,
                nullable,
                required,
                minLength,
                maxLength,
                min,
                max,
                errorTip,
                domainEntityAttributeType,
                domainEntityAttributeTypeList,
                dataEntityAttributeType,
                dataEntityAttributeTypeList,
                useCaseData,
                useCaseDataList,
                fruitSeedsAttribute,
                fruitSeedsAttributeList,
                dataEnum,
                dataEnumList,
                relatedDataEntityAttribute,
                relatedDataEntityAttributeList
            ),
            true,
            ""
        );
    }


}