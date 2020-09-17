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
import ir.afarinesh.realhope.shares.repositories.DataEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.DataEnumSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.UseCaseDataSpringJpaRepository;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseUsageEnum;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeQuantityEnum;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeCategoryEnum;
import ir.afarinesh.realhope.entities.feature.enums.PrimitiveAttributeTypeEnum;


@Service
public class AddNewUseCaseDataAttributeByProjectManagerService {

    // jpa repositories
    final UseCaseDataAttributeSpringJpaRepository useCaseDataAttributeSpringJpaRepository;
    final DataEntitySpringJpaRepository dataEntitySpringJpaRepository;
    final DataEnumSpringJpaRepository dataEnumSpringJpaRepository;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;
    final UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository;

    public AddNewUseCaseDataAttributeByProjectManagerService(UseCaseDataAttributeSpringJpaRepository useCaseDataAttributeSpringJpaRepository, DataEntitySpringJpaRepository dataEntitySpringJpaRepository, DataEnumSpringJpaRepository dataEnumSpringJpaRepository, DomainEntitySpringJpaRepository domainEntitySpringJpaRepository, UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository){
        this.useCaseDataAttributeSpringJpaRepository = useCaseDataAttributeSpringJpaRepository;
        this.dataEntitySpringJpaRepository = dataEntitySpringJpaRepository;
        this.dataEnumSpringJpaRepository = dataEnumSpringJpaRepository;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
        this.useCaseDataSpringJpaRepository = useCaseDataSpringJpaRepository;
    }

    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        UseCaseDataAttribute entity =
                this.useCaseDataAttributeSpringJpaRepository.findById(plant.getPlant().getId())
                    .orElseThrow(() -> new CultivateException("Cannot find by id = " + plant.getPlant().getId()));
                // ... 
        return new UseCaseFruit<>(
            new Fruit(
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
                dataEnumList
            ),
            true,
            ""
        );
    }


}