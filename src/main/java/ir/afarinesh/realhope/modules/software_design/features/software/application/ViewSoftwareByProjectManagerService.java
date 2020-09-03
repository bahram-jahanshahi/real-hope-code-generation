package ir.afarinesh.realhope.modules.software_design.features.software.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.shares.repositories.SoftwareSpringJpaRepository;
import ir.afarinesh.realhope.entities.project.Software;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.ViewSoftwareByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.ViewSoftwareByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.software_design.features.software.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import org.springframework.stereotype.Service;


@Service
public class ViewSoftwareByProjectManagerService {

    // jpa repositories
    final SoftwareSpringJpaRepository softwareSpringJpaRepository;

    public ViewSoftwareByProjectManagerService(SoftwareSpringJpaRepository softwareSpringJpaRepository) {
        this.softwareSpringJpaRepository = softwareSpringJpaRepository;
    }

    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        Software entity =
                this.softwareSpringJpaRepository.findById(plant.getPlant().getId())
                .orElseThrow(() -> new CultivateException("Cannot find by id = " + plant.getPlant().getId()));
        return new UseCaseFruit<>(
            new Fruit(
                    this.mapSoftware4ProjectManager(entity, plant.getLocale())
            ),
            true,
            ""
        );
    }

    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return null;
    }


    public Software4ProjectManager mapSoftware4ProjectManager(Software entity, String locale){
        return new Software4ProjectManager(
                entity.getId(),
                entity.getName(),
                entity.getTitle()
        );
    }
}