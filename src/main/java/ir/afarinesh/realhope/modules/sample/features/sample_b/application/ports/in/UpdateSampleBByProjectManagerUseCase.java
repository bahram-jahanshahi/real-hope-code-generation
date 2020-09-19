package ir.afarinesh.realhope.modules.sample.features.sample_b.application.ports.in;

import ir.afarinesh.realhope.core.domain.SelectEntity;import ir.afarinesh.realhope.core.domain.SelectEnum;
import ir.afarinesh.realhope.core.domain.JavaDate;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.core.annotations.UseCase;
import ir.afarinesh.realhope.modules.sample.features.sample_b.domain.SampleB4ProjectManager;
import lombok.*;

import java.util.Date;
import java.util.List;

@UseCase
public interface UpdateSampleBByProjectManagerUseCase {

  UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException;

  UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Fruit {
    Long id;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Plant {
    Long id;
    String name;
    Boolean active;
    JavaDate createDate;
    Long value;
    SelectEnum sampleStatusEnum;
    List<SelectEnum> sampleStatusEnumArray;
    SelectEntity sampleA;
    List<SelectEntity> sampleAArray;
  }

  class CultivateException extends Exception {
    public CultivateException(String message) {
      super(message);
    }
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  class FruitSeeds {
    Long id;
    String name;
    Boolean active;
    JavaDate createDate;
    Long value;
    SelectEnum sampleStatusEnum;
    List<SelectEnum> sampleStatusEnumArray;
    SelectEntity sampleA;
    List<SelectEntity> sampleAArray;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class SeedsCommands {
    Long id;
  }

  class PrepareException extends Exception {
    public PrepareException(String message) {
      super(message);
    }
  }
}