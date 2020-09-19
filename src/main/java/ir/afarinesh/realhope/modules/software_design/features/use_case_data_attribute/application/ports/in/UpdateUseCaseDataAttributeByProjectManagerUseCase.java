package ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application.ports.in;

import ir.afarinesh.realhope.core.domain.SelectEntity;import ir.afarinesh.realhope.core.domain.SelectEnum;
import ir.afarinesh.realhope.core.domain.JavaDate;
import ir.afarinesh.realhope.core.usecase.*;
import ir.afarinesh.realhope.core.annotations.UseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.domain.UseCaseDataAttribute4ProjectManager;
import lombok.*;

import java.util.Date;
import java.util.List;

@UseCase
public interface UpdateUseCaseDataAttributeByProjectManagerUseCase {

  UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException;

  UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Fruit {
    Boolean isSuccessful;
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
    Long uiRow;
    Long uiColumn;
    SelectEnum useCaseUsageEnumEnum;
    List<SelectEnum> useCaseUsageEnumEnumArray;
    SelectEnum attributeQuantityEnum;
    List<SelectEnum> attributeQuantityEnumArray;
    SelectEnum attributeCategoryEnum;
    List<SelectEnum> attributeCategoryEnumArray;
    SelectEnum primitiveAttributeTypeEnum;
    List<SelectEnum> primitiveAttributeTypeEnumArray;
    String setterOfUpdatePath;
    String getterOfUpdatePath;
    Boolean nullable;
    Boolean required;
    Long minLength;
    Long maxLength;
    Long min;
    Long max;
    String errorTip;
    SelectEntity domainEntityAttributeType;
    List<SelectEntity> domainEntityAttributeTypeArray;
    SelectEntity dataEntityAttributeType;
    List<SelectEntity> dataEntityAttributeTypeArray;
    SelectEntity useCaseData;
    List<SelectEntity> useCaseDataArray;
    SelectEntity fruitSeedsAttribute;
    List<SelectEntity> fruitSeedsAttributeArray;
    SelectEntity dataEnum;
    List<SelectEntity> dataEnumArray;
    SelectEntity relatedDataEntityAttribute;
    List<SelectEntity> relatedDataEntityAttributeArray;
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
    Long uiRow;
    Long uiColumn;
    SelectEnum useCaseUsageEnumEnum;
    List<SelectEnum> useCaseUsageEnumEnumArray;
    SelectEnum attributeQuantityEnum;
    List<SelectEnum> attributeQuantityEnumArray;
    SelectEnum attributeCategoryEnum;
    List<SelectEnum> attributeCategoryEnumArray;
    SelectEnum primitiveAttributeTypeEnum;
    List<SelectEnum> primitiveAttributeTypeEnumArray;
    String setterOfUpdatePath;
    String getterOfUpdatePath;
    Boolean nullable;
    Boolean required;
    Long minLength;
    Long maxLength;
    Long min;
    Long max;
    String errorTip;
    SelectEntity domainEntityAttributeType;
    List<SelectEntity> domainEntityAttributeTypeArray;
    SelectEntity dataEntityAttributeType;
    List<SelectEntity> dataEntityAttributeTypeArray;
    SelectEntity useCaseData;
    List<SelectEntity> useCaseDataArray;
    SelectEntity fruitSeedsAttribute;
    List<SelectEntity> fruitSeedsAttributeArray;
    SelectEntity dataEnum;
    List<SelectEntity> dataEnumArray;
    SelectEntity relatedDataEntityAttribute;
    List<SelectEntity> relatedDataEntityAttributeArray;
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