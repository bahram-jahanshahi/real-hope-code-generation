package ir.afarinesh.realhope.modules.software_design.features.software.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.shares.repositories.SoftwareSpringJpaRepository;
import ir.afarinesh.realhope.entities.project.Software;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.DeleteSoftwareByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.DeleteSoftwareByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.software_design.features.software.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@FeatureApplication
public class DeleteSoftwareByProjectManagerService {

    // jpa repositories
    final SoftwareSpringJpaRepository softwareSpringJpaRepository;

    public DeleteSoftwareByProjectManagerService(SoftwareSpringJpaRepository softwareSpringJpaRepository) {
        this.softwareSpringJpaRepository = softwareSpringJpaRepository;
    }

    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        Software entity =
                this.softwareSpringJpaRepository.findById(plant.getPlant().getId())
                .orElseThrow(() -> new CultivateException("Cannot find by id = " + plant.getPlant().getId()));
        this.softwareSpringJpaRepository.delete(entity);
        return new UseCaseFruit<>(
            new Fruit(
                    true
            ),
            true,
            ""
        );
    }

    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return null;
    }
}