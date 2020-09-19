package ir.afarinesh.realhope.modules.sample.features.sample_b.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.domain.SelectEnum;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.shares.repositories.SampleBSpringJpaRepository;
import ir.afarinesh.realhope.entities.sample.SampleB;
import ir.afarinesh.realhope.modules.sample.features.sample_b.application.ports.in.GridListSampleBByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.sample.features.sample_b.application.ports.in.GridListSampleBByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.sample.features.sample_b.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;


import java.util.stream.Collectors;
import java.util.List;


@Service
@FeatureApplication
public class GridListSampleBByProjectManagerService {

    // jpa repositories
    final SampleBSpringJpaRepository sampleBSpringJpaRepository;

    public GridListSampleBByProjectManagerService(SampleBSpringJpaRepository sampleBSpringJpaRepository) {
        this.sampleBSpringJpaRepository = sampleBSpringJpaRepository;
    }

    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        Page<SampleB> page = this.sampleBSpringJpaRepository
                .findAll(PageRequest.of(plant.getPlant().getPaginationCommand().getPageIndex(), plant.getPlant().getPaginationCommand().getPageSize()));
        List<SampleB4ProjectManager> list = page
                .get()
                .map(entity -> this.mapSampleB4ProjectManager(entity, plant.getLocale()))
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

    public SampleB4ProjectManager mapSampleB4ProjectManager(SampleB entity, String locale){
        return new SampleB4ProjectManager(
                entity.getId(),
                entity.getName(),
                entity.getActive(),
                CalendarUtility.format(entity.getCreateDate(), locale),
                entity.getValue(),
                (entity.getSampleStatus() != null) ? entity.getSampleStatus().title(locale) : null,
                (entity.getSampleA() != null) ? entity.getSampleA().title(locale) : null
        );
    }
}