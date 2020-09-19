package ir.afarinesh.realhope.modules.software_design.features.use_case.application;
import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseDataTypeEnum;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.AddNewUseCaseByProjectManagerUseCase;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.shares.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@Service
@FeatureApplication
public class AddNewUseCaseByProjectManagerServiceImpl implements AddNewUseCaseByProjectManagerUseCase {

    final AddNewUseCaseByProjectManagerService service;
    final UseCaseSpringJpaRepository useCaseSpringJpaRepository;
    final SoftwareApplicationPanelSpringJpaRepository softwareApplicationPanelSpringJpaRepository;
    final SoftwareFeatureSpringJpaRepository softwareFeatureSpringJpaRepository;
    final SoftwareRoleSpringJpaRepository softwareRoleSpringJpaRepository;
    final DataEntitySpringJpaRepository dataEntitySpringJpaRepository;
    final UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository;
    final CrudCodeGenerationSpringJpaRepository crudCodeGenerationSpringJpaRepository;

    public AddNewUseCaseByProjectManagerServiceImpl(AddNewUseCaseByProjectManagerService service,
                                                    UseCaseSpringJpaRepository useCaseSpringJpaRepository,
                                                    SoftwareApplicationPanelSpringJpaRepository softwareApplicationPanelSpringJpaRepository,
                                                    SoftwareFeatureSpringJpaRepository softwareFeatureSpringJpaRepository,
                                                    SoftwareRoleSpringJpaRepository softwareRoleSpringJpaRepository,
                                                    DataEntitySpringJpaRepository dataEntitySpringJpaRepository,
                                                    UseCaseDataSpringJpaRepository useCaseDataSpringJpaRepository,
                                                    CrudCodeGenerationSpringJpaRepository crudCodeGenerationSpringJpaRepository) {
        this.service = service;
        this.useCaseSpringJpaRepository = useCaseSpringJpaRepository;
        this.softwareApplicationPanelSpringJpaRepository = softwareApplicationPanelSpringJpaRepository;
        this.softwareFeatureSpringJpaRepository = softwareFeatureSpringJpaRepository;
        this.softwareRoleSpringJpaRepository = softwareRoleSpringJpaRepository;
        this.dataEntitySpringJpaRepository = dataEntitySpringJpaRepository;
        this.useCaseDataSpringJpaRepository = useCaseDataSpringJpaRepository;
        this.crudCodeGenerationSpringJpaRepository = crudCodeGenerationSpringJpaRepository;
    }

    @Override
    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {

        // Create
        UseCase useCase = new UseCase(
                null,
                plant.getPlant().getName().trim(),
                plant.getPlant().getTitle().trim(),
                plant.getPlant().getFaTitle().trim(),
                plant.getPlant().getDescription().trim(),
                UserInterfaceTypeEnum.findByName(plant.getPlant().getUserInterfaceTypeEnum().getValue()),
                this.softwareFeatureSpringJpaRepository.findById(plant.getPlant().getSoftwareFeature().getValue()).orElseThrow(),
                this.softwareApplicationPanelSpringJpaRepository.findById(plant.getPlant().getSoftwareApplicationPanel().getValue()).orElseThrow(),
                this.softwareRoleSpringJpaRepository.findById(plant.getPlant().getSoftwareRole().getValue()).orElseThrow(),
                this.dataEntitySpringJpaRepository.findById(plant.getPlant().getDataEntity().getValue()).orElseThrow(),
                Set.of(),
                plant.getPlant().getGenerationEnable(),
                this.crudCodeGenerationSpringJpaRepository.findById(plant.getPlant().getCrudCodeGeneration().getValue()).orElseThrow()
        );

        // Save use case
        this.useCaseSpringJpaRepository.save(useCase);

        // Add new plant
        this.addNewUseCaseData(UseCaseDataTypeEnum.Plant, useCase);
        // Add new Fruit
        this.addNewUseCaseData(UseCaseDataTypeEnum.Fruit, useCase);
        // Add new SeedsCommand
        this.addNewUseCaseData(UseCaseDataTypeEnum.SeedsCommand, useCase);
        // Add new FruitSeeds
        this.addNewUseCaseData(UseCaseDataTypeEnum.FruitSeeds, useCase);

        // Return
        return new UseCaseFruit<>(
                new Fruit(useCase.getId()),
                true,
                ""
        );
    }

    @Override
    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return this.service.prepare(seedsCommand);
    }

    protected void addNewUseCaseData(UseCaseDataTypeEnum useCaseDataType, UseCase useCase) {
        this.useCaseDataSpringJpaRepository
                .save(
                        new UseCaseData(
                                null,
                                useCaseDataType.name(),
                                useCaseDataType,
                                useCase.getName() + "." + useCaseDataType.name(),
                                useCase,
                                Collections.emptyList()
                        )
                );
    }
}
