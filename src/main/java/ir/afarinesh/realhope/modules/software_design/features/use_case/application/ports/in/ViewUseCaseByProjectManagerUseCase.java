package ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in;

import ir.afarinesh.realhope.core.domain.SelectEntity;import ir.afarinesh.realhope.core.domain.SelectEnum;
import ir.afarinesh.realhope.core.domain.JavaDate;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.core.annotations.UseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.domain.UseCase4ProjectManager;
import lombok.*;

import java.util.Date;
import java.util.List;

@UseCase
public interface ViewUseCaseByProjectManagerUseCase {

  UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException;

  UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Fruit {
    UseCase4ProjectManager entity;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Plant {
    Long id;
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
    boolean something;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class SeedsCommands {
    boolean doSomething;
  }

  class PrepareException extends Exception {
    public PrepareException(String message) {
      super(message);
    }
  }
}