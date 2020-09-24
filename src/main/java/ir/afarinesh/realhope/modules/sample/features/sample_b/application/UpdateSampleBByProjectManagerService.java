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

import ir.afarinesh.realhope.shares.repositories.SampleASpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.SampleBSpringJpaRepository;
import ir.afarinesh.realhope.entities.sample.enums.SampleStatusEnum;


@Service
public class UpdateSampleBByProjectManagerService {

    // jpa repositories
    final SampleASpringJpaRepository sampleASpringJpaRepository;
    final SampleBSpringJpaRepository sampleBSpringJpaRepository;

    public UpdateSampleBByProjectManagerService(SampleASpringJpaRepository sampleASpringJpaRepository, SampleBSpringJpaRepository sampleBSpringJpaRepository){
        this.sampleASpringJpaRepository = sampleASpringJpaRepository;
        this.sampleBSpringJpaRepository = sampleBSpringJpaRepository;
    }

    @Transactional
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        // Entity
        SampleB entity =
                this.sampleBSpringJpaRepository.findById(plant.getPlant().getId())
                    .orElseThrow(() -> new CultivateException("Cannot find by id = " + plant.getPlant().getId()));
        // Setters
        entity.setId(plant.getPlant().getId());
        entity.setName(plant.getPlant().getName());
        entity.setActive(plant.getPlant().getActive());
        entity.setCreateDate(CalendarUtility.getDate(plant.getPlant().getCreateDate()));
        entity.setValue(plant.getPlant().getValue());
        entity.setSampleStatus(SampleStatusEnum.findByName(plant.getPlant().getSampleStatusEnum().getValue()));
        entity.setSampleA(plant.getPlant().getSampleA().getValue() != null ? sampleASpringJpaRepository.findById(plant.getPlant().getSampleA().getValue()).orElseThrow() : null);
        // Save or update
        this.sampleBSpringJpaRepository.save(entity);
        // Return
        return new UseCaseFruit<>(
            new Fruit(
                    entity.getId()
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