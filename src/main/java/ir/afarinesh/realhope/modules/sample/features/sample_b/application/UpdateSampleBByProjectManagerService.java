package ir.afarinesh.realhope.modules.sample.features.sample_b.application;

import ir.afarinesh.realhope.core.annotations.FeatureApplication;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.entities.sample.SampleB;
import ir.afarinesh.realhope.modules.sample.features.sample_b.application.ports.in.UpdateSampleBByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.sample.features.sample_b.application.ports.in.UpdateSampleBByProjectManagerUseCase.*;
import ir.afarinesh.realhope.modules.sample.features.sample_b.domain.*;
import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import ir.afarinesh.realhope.core.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

import ir.afarinesh.realhope.shares.repositories.SampleBSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.SampleASpringJpaRepository;
import ir.afarinesh.realhope.entities.sample.enums.SampleStatusEnum;


@Service
public class UpdateSampleBByProjectManagerService {

    // jpa repositories
    final SampleBSpringJpaRepository sampleBSpringJpaRepository;
    final SampleASpringJpaRepository sampleASpringJpaRepository;

    public UpdateSampleBByProjectManagerService(SampleBSpringJpaRepository sampleBSpringJpaRepository, SampleASpringJpaRepository sampleASpringJpaRepository){
        this.sampleBSpringJpaRepository = sampleBSpringJpaRepository;
        this.sampleASpringJpaRepository = sampleASpringJpaRepository;
    }

    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        SampleB entity =
                this.sampleBSpringJpaRepository.findById(plant.getPlant().getId())
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

        SampleB entity =
                this.sampleBSpringJpaRepository.findById(seedsCommand.getSeedsCommand().getId())
                .orElseThrow(() -> new PrepareException("Cannot find by id = " + seedsCommand.getSeedsCommand().getId()));
        Long id = entity.getId();
        String name = entity.getName();
        Boolean active = entity.getActive();
        JavaDate createDate = CalendarUtility.getJavaDate(entity.getCreateDate());
        Long value = entity.getValue();
        SelectEnum sampleStatusEnum = (entity.getSampleStatus() != null) ? entity.getSampleStatus().getSelectEnum(seedsCommand.getLocale()) :  new SelectEnum();
        List<SelectEnum> sampleStatusEnumList = SampleStatusEnum.Void.getSelectEnumList(seedsCommand.getLocale());
        SelectEntity sampleA = (entity.getSampleA() != null) ? new SelectEntity(entity.getSampleA().title(seedsCommand.getLocale()), entity.getSampleA().getId()) :  new SelectEntity();
        List<SelectEntity> sampleAList = this.sampleASpringJpaRepository
                .findAll()
                .stream()
                .map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))
                .collect(Collectors.toList());

        return new UseCaseFruitSeeds<>(
            new FruitSeeds(
                id,
                name,
                active,
                createDate,
                value,
                sampleStatusEnum,
                sampleStatusEnumList,
                sampleA,
                sampleAList
            ),
            true,
            ""
        );
    }


}