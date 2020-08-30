package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java;

import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.entities.feature.enums.*;
import ir.afarinesh.realhope.entities.project.SoftwareFeature;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.exceptions.GenerateUseCaseInterfaceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenerateUseCaseInterfaceJavaFile {

    String t = StringUtility.space(2);
    String eol = "\n";
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;

    public GenerateUseCaseInterfaceJavaFile(FileManagementService fileManagementService, UseCasePathService useCasePathService, DomainEntitySpringJpaRepository domainEntitySpringJpaRepository) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
    }

    public void generate(UseCase useCase) throws GenerateUseCaseInterfaceJavaFileException {
        try {
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getFileName(useCase),
                            this.getContent(useCase),
                            true
                    );
        } catch (CreateFileException e) {
            throw new GenerateUseCaseInterfaceJavaFileException(e.getMessage());
        }
    }

    public String getPath(UseCase useCase) {
        String pathSeparator = fileManagementService.pathSeparator();
        return useCasePathService.getSpringBootFeaturePath(useCase.getSoftwareFeature()) + pathSeparator
                + "application" + pathSeparator
                + "ports" + pathSeparator
                + "in";
    }

    public String getFileName(UseCase useCase) {
        return useCase.getName() + "By" + useCase.getSoftwareRole().getName() + "UseCase.java";
    }

    public String getContent(UseCase useCase) {
        String packageTitle = "package "
                + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + "."
                + "application" + "."
                + "ports" + "."
                + "in;"
                + eol;
        String imports = ""
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".domain.SelectEntity;"
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".domain.SelectEnum;" + eol
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".domain.JavaDate;" + eol
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".usecase.*;" + eol
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".annotations.UseCase;" + eol
                + this.getDomainEntitiesImports(useCase.getSoftwareFeature())
                + "import lombok.*;" + eol
                + eol
                + "import java.util.Date;" + eol
                + "import java.util.List;" + eol;
        String interfaceContent = ""
                + "@UseCase" + eol
                + "public interface " + useCase.getName() + "By" + useCase.getSoftwareRole().getName() + "UseCase" + " {" + eol
                + eol
                + t + "UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException;" + eol
                + eol
                + t + "UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException;" + eol
                + eol
                + this.getFruit(useCase)
                + eol
                + this.getPlant(useCase)
                + eol
                + this.getCultivateException()
                + eol
                + this.getFruitSeeds(useCase)
                + eol
                + this.getSeedsCommands(useCase)
                + eol
                + this.getPrepareException()
                + "}";
        return packageTitle
                + eol
                + imports
                + eol
                + interfaceContent;

    }

    private String getPlant(UseCase useCase) {
        return ""
                + t + "@Data" + eol
                + t + "@NoArgsConstructor" + eol
                + t + "@AllArgsConstructor" + eol
                + t + "class Plant {" + eol
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.Plant)
                + t + "}" + eol;
    }

    private String getFruit(UseCase useCase) {
        return ""
                + t + "@Data" + eol
                + t + "@NoArgsConstructor" + eol
                + t + "@AllArgsConstructor" + eol
                + t + "class Fruit {" + eol
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.Fruit)
                + t + "}" + eol;
    }

    private String getCultivateException() {
        return ""
                + t + "class CultivateException extends Exception {" + eol
                + t + t + "public CultivateException(String message) {" + eol
                + t + t + t + "super(message);" + eol
                + t + t + "}" + eol
                + t + "}" + eol;
    }

    private String getSeedsCommands(UseCase useCase) {
        return ""
                + t + "@Data" + eol
                + t + "@NoArgsConstructor" + eol
                + t + "@AllArgsConstructor" + eol
                + t + "class SeedsCommands {" + eol
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.SeedsCommand)
                + t + "}" + eol;
    }

    private String getFruitSeeds(UseCase useCase) {
        return ""
                + t + "@NoArgsConstructor" + eol
                + t + "@AllArgsConstructor" + eol
                + t + "@Data" + eol
                + t + "class FruitSeeds {" + eol
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.FruitSeeds)
                + t + "}" + eol;
    }

    private String getPrepareException() {
        return ""
                + t + "class PrepareException extends Exception {" + eol
                + t + t + "public PrepareException(String message) {" + eol
                + t + t + t + "super(message);" + eol
                + t + t + "}" + eol
                + t + "}" + eol;
    }

    private String getDomainEntitiesImports(SoftwareFeature softwareFeature) {
        List<DomainEntity> domainEntities = this.domainEntitySpringJpaRepository.findAllBySoftwareFeature_Id(softwareFeature.getId());
        String content = "";
        for (DomainEntity domainEntity : domainEntities) {
            content += "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(softwareFeature) + "."
                    + "domain."
                    + domainEntity.getName()+";" + eol;
        }
        return content;
    }

    private String getDataAttributes(UseCase useCase, UseCaseDataTypeEnum useCaseDataType) {
        Optional<UseCaseData> useCaseDataOptional = useCase
                .getUseCaseDataSet()
                .stream()
                .filter(useCaseData -> useCaseData.getUseCaseDataType().equals(useCaseDataType))
                .findFirst();
        if (useCaseDataOptional.isPresent()) {
            List<UseCaseDataAttribute> useCaseDataAttributes = useCaseDataOptional.get().getUseCaseDataAttributes();
            String content = "";
            for (UseCaseDataAttribute attribute : useCaseDataAttributes) {
                String attributeName = StringUtility.firstLowerCase(attribute.getName());
                // Mono
                if (attribute.getAttributeQuantity().equals(EntityAttributeQuantityEnum.Mono)) {
                    String attributeType = "";
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.Primitive)) {
                        attributeType = attribute.getPrimitiveAttributeType().name();
                        content += t + t + attributeType + " " + attributeName + ";" + eol;
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.DomainEntity)) {
                        attributeType = attribute.getDomainEntityAttributeType().getName();
                        content += t + t + attributeType + " " + attributeName + ";" + eol;
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEnum)) {
                        content += t + t + "SelectEnum" + " " + attributeName + "Enum;" + eol;
                        content += t + t + "List<SelectEnum>" + " " + attributeName + "EnumArray;" + eol;
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEntity)) {
                        content += t + t + "SelectEntity" + " " + attributeName + ";" + eol;
                        content += t + t + "List<SelectEntity>" + " " + attributeName + "Array;" + eol;
                    }
                }
                // List
                if (attribute.getAttributeQuantity().equals(EntityAttributeQuantityEnum.List)) {
                    String attributeType = "";
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.Primitive)) {
                        attributeType = attribute.getPrimitiveAttributeType().name();
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.DomainEntity)) {
                        attributeType = attribute.getDomainEntityAttributeType().getName();
                    }
                    content += t + t + "List<" + attributeType + ">" + " " + attributeName + ";" + eol;
                }
            }
            if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)
                    && useCaseDataType.equals(UseCaseDataTypeEnum.Plant)) {
                content += t + t + "private PaginationCommand paginationCommand;" + eol;
            }
            if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)
                    && useCaseDataType.equals(UseCaseDataTypeEnum.Fruit)) {
                content += t + t + "private PagedResultFruit pagedResultFruit;" + eol;
            }
            if (useCaseDataType.equals(UseCaseDataTypeEnum.Plant)) {
                if (useCaseDataAttributes.size() == 0) {
                    content += t + t + "boolean doSomething;" + eol;
                }
            }
            if (useCaseDataType.equals(UseCaseDataTypeEnum.Fruit)) {
                if (useCaseDataAttributes.size() == 0) {
                    content += t + t + "boolean something;" + eol;
                }
            }
            if (useCaseDataType.equals(UseCaseDataTypeEnum.SeedsCommand)) {
                if (useCaseDataAttributes.size() == 0) {
                    content += t + t + "boolean doSomething;" + eol;
                }
            }
            if (useCaseDataType.equals(UseCaseDataTypeEnum.FruitSeeds)) {
                if (useCaseDataAttributes.size() == 0) {
                    content += t + t + "boolean something;" + eol;
                }
            }
            return content;
        }

        return "";
    }
}
