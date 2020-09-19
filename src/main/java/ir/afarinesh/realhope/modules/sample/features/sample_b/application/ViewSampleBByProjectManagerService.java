package ir.afarinesh.realhope.modules.sample.features.sample_b.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.shares.repositories.SampleBSpringJpaRepository;
import ir.afarinesh.realhope.entities.sample.SampleB;
import ir.afarinesh.realhope.modules.sample.features.sample_b.application.ports.in.ViewSampleBByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.sample.features.sample_b.application.ports.in.ViewSampleBByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.sample.features.sample_b.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import org.springframework.stereotype.Service;


@Service
public class ViewSampleBByProjectManagerService {

    // jpa repositories
    final SampleBSpringJpaRepository sampleBSpringJpaRepository;

    public ViewSampleBByProjectManagerService(SampleBSpringJpaRepository sampleBSpringJpaRepository) {
        this.sampleBSpringJpaRepository = sampleBSpringJpaRepository;
    }

    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        SampleB entity =
                this.sampleBSpringJpaRepository.findById(plant.getPlant().getId())
                .orElseThrow(() -> new CultivateException("Cannot find by id = " + plant.getPlant().getId()));
        return new UseCaseFruit<>(
            new Fruit(
                    this.mapSampleB4ProjectManager(entity, plant.getLocale())
            ),
            true,
            ""
        );
    }

    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return null;
    }


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