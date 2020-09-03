package ir.afarinesh.realhope.modules.software_design.features.software.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.entities.project.Software;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.AddNewSoftwareByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.AddNewSoftwareByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.software_design.features.software.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import ir.afarinesh.realhope.core.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

import ir.afarinesh.realhope.shares.repositories.SoftwareSpringJpaRepository;


@Service
public class AddNewSoftwareByProjectManagerService {

    // jpa repositories
    final SoftwareSpringJpaRepository softwareSpringJpaRepository;

    public AddNewSoftwareByProjectManagerService(SoftwareSpringJpaRepository softwareSpringJpaRepository){
        this.softwareSpringJpaRepository = softwareSpringJpaRepository;
    }

    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        Software entity =
                this.softwareSpringJpaRepository.findById(plant.getPlant().getId())
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
        String softwareName = null;
        String softwareTitle = null;
        String springBootProjectPath = null;
        String springBootProjectMainPackage = null;
        String springBootJavaSrcPath = null;

        return new UseCaseFruitSeeds<>(
            new FruitSeeds(
                id,
                softwareName,
                softwareTitle,
                springBootProjectPath,
                springBootProjectMainPackage,
                springBootJavaSrcPath
            ),
            true,
            ""
        );
    }


}