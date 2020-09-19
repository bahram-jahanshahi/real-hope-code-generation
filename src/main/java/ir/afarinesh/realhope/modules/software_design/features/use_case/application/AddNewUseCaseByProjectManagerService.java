package ir.afarinesh.realhope.modules.software_design.features.use_case.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.AddNewUseCaseByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.AddNewUseCaseByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.software_design.features.use_case.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import ir.afarinesh.realhope.core.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

import ir.afarinesh.realhope.shares.repositories.SoftwareApplicationPanelSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.SoftwareFeatureSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.UseCaseSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.DataEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.CrudCodeGenerationSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.SoftwareRoleSpringJpaRepository;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;


@Service
public class AddNewUseCaseByProjectManagerService {

    // jpa repositories
    final SoftwareApplicationPanelSpringJpaRepository softwareApplicationPanelSpringJpaRepository;
    final SoftwareFeatureSpringJpaRepository softwareFeatureSpringJpaRepository;
    final UseCaseSpringJpaRepository useCaseSpringJpaRepository;
    final DataEntitySpringJpaRepository dataEntitySpringJpaRepository;
    final CrudCodeGenerationSpringJpaRepository crudCodeGenerationSpringJpaRepository;
    final SoftwareRoleSpringJpaRepository softwareRoleSpringJpaRepository;

    public AddNewUseCaseByProjectManagerService(SoftwareApplicationPanelSpringJpaRepository softwareApplicationPanelSpringJpaRepository, SoftwareFeatureSpringJpaRepository softwareFeatureSpringJpaRepository, UseCaseSpringJpaRepository useCaseSpringJpaRepository, DataEntitySpringJpaRepository dataEntitySpringJpaRepository, CrudCodeGenerationSpringJpaRepository crudCodeGenerationSpringJpaRepository, SoftwareRoleSpringJpaRepository softwareRoleSpringJpaRepository){
        this.softwareApplicationPanelSpringJpaRepository = softwareApplicationPanelSpringJpaRepository;
        this.softwareFeatureSpringJpaRepository = softwareFeatureSpringJpaRepository;
        this.useCaseSpringJpaRepository = useCaseSpringJpaRepository;
        this.dataEntitySpringJpaRepository = dataEntitySpringJpaRepository;
        this.crudCodeGenerationSpringJpaRepository = crudCodeGenerationSpringJpaRepository;
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

        Long id = null;
        String name = null;
        String title = null;
        String faTitle = null;
        String description = null;
        SelectEnum userInterfaceTypeEnum = new SelectEnum();
        List<SelectEnum> userInterfaceTypeEnumList = UserInterfaceTypeEnum.Void.getSelectEnumList(seedsCommand.getLocale());
        SelectEntity softwareFeature = new SelectEntity();
        List<SelectEntity> softwareFeatureList = this.softwareFeatureSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity softwareApplicationPanel = new SelectEntity();
        List<SelectEntity> softwareApplicationPanelList = this.softwareApplicationPanelSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity softwareRole = new SelectEntity();
        List<SelectEntity> softwareRoleList = this.softwareRoleSpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        SelectEntity dataEntity = new SelectEntity();
        List<SelectEntity> dataEntityList = this.dataEntitySpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());
        Boolean generationEnable = null;
        SelectEntity crudCodeGeneration = new SelectEntity();
        List<SelectEntity> crudCodeGenerationList = this.crudCodeGenerationSpringJpaRepository
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
                generationEnable,
                crudCodeGeneration,
                crudCodeGenerationList
            ),
            true,
            ""
        );
    }


}