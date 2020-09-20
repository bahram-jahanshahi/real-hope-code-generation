package ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application.ports.in.AddNewUseCaseDataAttributeByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application.ports.in.AddNewUseCaseDataAttributeByProjectManagerUseCase.*;
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
public class AddNewUseCaseDataAttributeByProjectManagerService {

    // jpa repositories
    final UseCaseDataAttributeSpringJpaRepository useCaseDataAttributeSpringJpaRepository;
    final DataEnumSpringJpaRepository dataEnumSpringJpaRepository;
    final DataEntitySpringJpaRepository dataEntitySpringJpaRepository;
    final UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;
    final DataEntityAttributeSpringJpaRepository dataEntityAttributeSpringJpaRepository;

    public AddNewUseCaseDataAttributeByProjectManagerService(UseCaseDataAttributeSpringJpaRepository useCaseDataAttributeSpringJpaRepository, DataEnumSpringJpaRepository dataEnumSpringJpaRepository, DataEntitySpringJpaRepository dataEntitySpringJpaRepository, UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository, DomainEntitySpringJpaRepository domainEntitySpringJpaRepository, DataEntityAttributeSpringJpaRepository dataEntityAttributeSpringJpaRepository){
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
        UseCaseDataAttribute entity = new UseCaseDataAttribute();
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

        Long id = null;
        String name = null;
        String title = null;
        String faTitle = null;
        String description = null;
        Long uiRow = null;
        Long uiColumn = null;
        SelectEnum useCaseUsageEnumEnum = new SelectEnum();
        List<SelectEnum> useCaseUsageEnumEnumList = UseCaseUsageEnum.Void.getSelectEnumList(seedsCommand.getLocale());
        SelectEnum attributeQuantityEnum = new SelectEnum();
        List<SelectEnum> attributeQuantityEnumList = EntityAttributeQuantityEnum.Void.getSelectEnumList(seedsCommand.getLocale());
        SelectEnum attributeCategoryEnum = new SelectEnum();
        List<SelectEnum> attributeCategoryEnumList = EntityAttributeCategoryEnum.Void.getSelectEnumList(seedsCommand.getLocale());
        SelectEnum primitiveAttributeTypeEnum = new SelectEnum();
        List<SelectEnum> primitiveAttributeTypeEnumList = PrimitiveAttributeTypeEnum.Void.getSelectEnumList(seedsCommand.getLocale());
        String setterOfUpdatePath = null;
        String getterOfUpdatePath = null;
        Boolean nullable = null;
        Boolean required = null;
        Long minLength = null;
        Long maxLength = null;
        Long min = null;
        Long max = null;
        String errorTip = null;
        SelectEntity domainEntityAttributeType = new SelectEntity();
        List<SelectEntity> domainEntityAttributeTypeList = this.domainEntitySpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity dataEntityAttributeType = new SelectEntity();
        List<SelectEntity> dataEntityAttributeTypeList = this.dataEntitySpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity useCaseData = new SelectEntity();
        List<SelectEntity> useCaseDataList = this.useCaseDataSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity fruitSeedsAttribute = new SelectEntity();
        List<SelectEntity> fruitSeedsAttributeList = this.useCaseDataAttributeSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity dataEnum = new SelectEntity();
        List<SelectEntity> dataEnumList = this.dataEnumSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity relatedDataEntityAttribute = new SelectEntity();
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