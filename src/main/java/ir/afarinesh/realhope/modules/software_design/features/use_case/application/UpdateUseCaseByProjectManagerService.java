package ir.afarinesh.realhope.modules.software_design.features.use_case.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.UpdateUseCaseByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.UpdateUseCaseByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.software_design.features.use_case.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import ir.afarinesh.realhope.core.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

import ir.afarinesh.realhope.shares.repositories.DataEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.UseCaseSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.SoftwareApplicationPanelSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.SoftwareFeatureSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.SoftwareRoleSpringJpaRepository;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;


@Service
public class UpdateUseCaseByProjectManagerService {

    // jpa repositories
    final DataEntitySpringJpaRepository dataEntitySpringJpaRepository;
    final UseCaseSpringJpaRepository useCaseSpringJpaRepository;
    final SoftwareApplicationPanelSpringJpaRepository softwareApplicationPanelSpringJpaRepository;
    final SoftwareFeatureSpringJpaRepository softwareFeatureSpringJpaRepository;
    final SoftwareRoleSpringJpaRepository softwareRoleSpringJpaRepository;

    public UpdateUseCaseByProjectManagerService(DataEntitySpringJpaRepository dataEntitySpringJpaRepository, UseCaseSpringJpaRepository useCaseSpringJpaRepository, SoftwareApplicationPanelSpringJpaRepository softwareApplicationPanelSpringJpaRepository, SoftwareFeatureSpringJpaRepository softwareFeatureSpringJpaRepository, SoftwareRoleSpringJpaRepository softwareRoleSpringJpaRepository){
        this.dataEntitySpringJpaRepository = dataEntitySpringJpaRepository;
        this.useCaseSpringJpaRepository = useCaseSpringJpaRepository;
        this.softwareApplicationPanelSpringJpaRepository = softwareApplicationPanelSpringJpaRepository;
        this.softwareFeatureSpringJpaRepository = softwareFeatureSpringJpaRepository;
        this.softwareRoleSpringJpaRepository = softwareRoleSpringJpaRepository;
    }

    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        UseCase entity =
                this.useCaseSpringJpaRepository.findById(plant.getPlant().getId())
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

        UseCase entity =
                this.useCaseSpringJpaRepository.findById(seedsCommand.getSeedsCommand().getId())
                .orElseThrow(() -> new PrepareException("Cannot find by id = " + seedsCommand.getSeedsCommand().getId()));
        Long id = entity.getId();
        String name = entity.getName();
        String title = entity.getTitle();
        String faTitle = entity.getFaTitle();
        String description = entity.getDescription();
        SelectEnum userInterfaceTypeEnum = (entity.getUserInterfaceType() != null) ? entity.getUserInterfaceType().getSelectEnum(seedsCommand.getLocale()) :  new SelectEnum();
        List<SelectEnum> userInterfaceTypeEnumList = UserInterfaceTypeEnum.Void.getSelectEnumList(seedsCommand.getLocale());
        SelectEntity softwareFeature = (entity.getSoftwareFeature() != null) ? new SelectEntity(entity.getSoftwareFeature().title(seedsCommand.getLocale()), entity.getSoftwareFeature().getId()) :  new SelectEntity();
        List<SelectEntity> softwareFeatureList = this.softwareFeatureSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity softwareApplicationPanel = (entity.getSoftwareApplicationPanel() != null) ? new SelectEntity(entity.getSoftwareApplicationPanel().title(seedsCommand.getLocale()), entity.getSoftwareApplicationPanel().getId()) :  new SelectEntity();
        List<SelectEntity> softwareApplicationPanelList = this.softwareApplicationPanelSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity softwareRole = (entity.getSoftwareRole() != null) ? new SelectEntity(entity.getSoftwareRole().title(seedsCommand.getLocale()), entity.getSoftwareRole().getId()) :  new SelectEntity();
        List<SelectEntity> softwareRoleList = this.softwareRoleSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity dataEntity = (entity.getDataEntity() != null) ? new SelectEntity(entity.getDataEntity().title(seedsCommand.getLocale()), entity.getDataEntity().getId()) :  new SelectEntity();
        List<SelectEntity> dataEntityList = this.dataEntitySpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        Boolean generationEnable = entity.getGenerationEnable();

        return new UseCaseFruitSeeds<>(
            new FruitSeeds(
                id,
                name,
                title,
                faTitle,
                description,
                userInterfaceTypeEnum,
                userInterfaceTypeEnumList,
                softwareFeature,
                softwareFeatureList,
                softwareApplicationPanel,
                softwareApplicationPanelList,
                softwareRole,
                softwareRoleList,
                dataEntity,
                dataEntityList,
                generationEnable
            ),
            true,
            ""
        );
    }


}