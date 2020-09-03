package ir.afarinesh.realhope.modules.software_design.features.software.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.domain.SelectEnum;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.shares.repositories.SoftwareSpringJpaRepository;
import ir.afarinesh.realhope.entities.project.Software;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.ListSoftwareByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.ListSoftwareByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.software_design.features.software.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;


import java.util.stream.Collectors;
import java.util.List;


@Service
@FeatureApplication
public class ListSoftwareByProjectManagerService {

    // jpa repositories
    final SoftwareSpringJpaRepository softwareSpringJpaRepository;

    public ListSoftwareByProjectManagerService(SoftwareSpringJpaRepository softwareSpringJpaRepository) {
        this.softwareSpringJpaRepository = softwareSpringJpaRepository;
    }

    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        Page<Software> page = this.softwareSpringJpaRepository
                .findAll(PageRequest.of(plant.getPlant().getPaginationCommand().getPageIndex(), plant.getPlant().getPaginationCommand().getPageSize()));
        List<Software4ProjectManager> list = page
                .get()
                .map(entity -> this.mapSoftware4ProjectManager(entity, plant.getLocale()))
                .collect(Collectors.toList());
        return new UseCaseFruit<>(
                new Fruit(
                        list,
                        new PagedResultFruit(page.getTotalElements(), page.getTotalPages())
                ),
                true,
                ""
        );
    }

    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return new UseCaseFruitSeeds<>(
                new FruitSeeds(
                ),
                true,
                ""
        );    }

    public Software4ProjectManager mapSoftware4ProjectManager(Software entity, String locale){
        return new Software4ProjectManager(
                entity.getId(),
                entity.getName(),
                entity.getTitle()
        );
    }
}