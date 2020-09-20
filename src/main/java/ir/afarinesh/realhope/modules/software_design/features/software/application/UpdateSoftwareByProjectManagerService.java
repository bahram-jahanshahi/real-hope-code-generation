package ir.afarinesh.realhope.modules.software_design.features.software.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.entities.project.Software;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.UpdateSoftwareByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.UpdateSoftwareByProjectManagerUseCase.*;
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
public class UpdateSoftwareByProjectManagerService {

    // jpa repositories
    final SoftwareSpringJpaRepository softwareSpringJpaRepository;

    public UpdateSoftwareByProjectManagerService(SoftwareSpringJpaRepository softwareSpringJpaRepository){
        this.softwareSpringJpaRepository = softwareSpringJpaRepository;
    }

    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        // Entity
        Software entity =
                this.softwareSpringJpaRepository.findById(plant.getPlant().getId())
                    .orElseThrow(() -> new CultivateException("Cannot find by id = " + plant.getPlant().getId()));
        // Setters
        // Save or update
        this.softwareSpringJpaRepository.save(entity);
        // Return
        return new UseCaseFruit<>(
            new Fruit(

            ),
            true,
            ""
        );
    }

    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {

        Software entity =
                this.softwareSpringJpaRepository.findById(seedsCommand.getSeedsCommand().getId())
                .orElseThrow(() -> new PrepareException("Cannot find by id = " + seedsCommand.getSeedsCommand().getId()));
        Long id = entity.getId();
        String softwareName = entity.getName();
        String softwareTitle = entity.getTitle();
        String springBootProjectPath = entity.getSpringBootProjectPath();
        String springBootProjectMainPackage = entity.getSpringBootProjectMainPackage();
        String springBootJavaSrcPath = entity.getSpringBootJavaSrcPath();

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
