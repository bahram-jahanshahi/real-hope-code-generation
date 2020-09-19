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
public interface AddNewUseCaseByProjectManagerUseCase {

  UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException;

  UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Fruit {
    Long newUseCaseId;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Plant {
    Long id;
    String name;
    String title;
    String faTitle;
    String description;
    SelectEnum userInterfaceTypeEnum;
    List<SelectEnum> userInterfaceTypeEnumArray;
    SelectEntity softwareFeature;
    List<SelectEntity> softwareFeatureArray;
    SelectEntity softwareApplicationPanel;
    List<SelectEntity> softwareApplicationPanelArray;
    SelectEntity softwareRole;
    List<SelectEntity> softwareRoleArray;
    SelectEntity dataEntity;
    List<SelectEntity> dataEntityArray;
    Boolean generationEnable;
    SelectEntity crudCodeGeneration;
    List<SelectEntity> crudCodeGenerationArray;
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
    String title;
    String faTitle;
    String description;
    SelectEnum userInterfaceTypeEnum;
    List<SelectEnum> userInterfaceTypeEnumArray;
    SelectEntity softwareFeature;
    List<SelectEntity> softwareFeatureArray;
    SelectEntity softwareApplicationPanel;
    List<SelectEntity> softwareApplicationPanelArray;
    SelectEntity softwareRole;
    List<SelectEntity> softwareRoleArray;
    SelectEntity dataEntity;
    List<SelectEntity> dataEntityArray;
    Boolean generationEnable;
    SelectEntity crudCodeGeneration;
    List<SelectEntity> crudCodeGenerationArray;
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