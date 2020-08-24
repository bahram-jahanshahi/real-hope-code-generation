package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java;

import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeCategoryEnum;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeQuantityEnum;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseDataTypeEnum;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
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
                            this.getContent(useCase)
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
                + "\n";
        String imports = ""
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".usecase.*;" + "\n"
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".annotations.UseCase;" + "\n"
                + this.getDomainEntitiesImports(useCase.getSoftwareFeature())
                + "import lombok.*;" + "\n"
                + "\n"
                + "import java.util.List;" + "\n";
        String interfaceContent = ""
                + "@UseCase" + "\n"
                + "public interface " + useCase.getName() + "By" + useCase.getSoftwareRole().getName() + "UseCase" + " {" + "\n"
                + "\n"
                + "\t" + "UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException;" + "\n"
                + "\n"
                + "\t" + "UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException;" + "\n"
                + "\n"
                + this.getFruit(useCase)
                + "\n"
                + this.getPlant(useCase)
                + "\n"
                + this.getCultivateException()
                + "\n"
                + this.getFruitSeeds(useCase)
                + "\n"
                + this.getSeedsCommands(useCase)
                + "\n"
                + this.getPrepareException()
                + "}";
        return packageTitle
                + "\n"
                + imports
                + "\n"
                + interfaceContent;

    }

    private String getPlant(UseCase useCase) {
        return ""
                + "\t" + "@Data" + "\n"
                + "\t" + "@NoArgsConstructor" + "\n"
                + "\t" + "@AllArgsConstructor" + "\n"
                + "\t" + "class Plant {" + "\n"
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.Plant)
                + "\t" + "}" + "\n";
    }

    private String getFruit(UseCase useCase) {
        return ""
                + "\t" + "@Data" + "\n"
                + "\t" + "@NoArgsConstructor" + "\n"
                + "\t" + "@AllArgsConstructor" + "\n"
                + "\t" + "class Fruit {" + "\n"
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.Fruit)
                + "\t" + "}" + "\n";
    }

    private String getCultivateException() {
        return ""
                + "\t" + "class CultivateException extends Exception {" + "\n"
                + "\t" + "\t" + "public CultivateException(String message) {" + "\n"
                + "\t" + "\t" + "\t" + "super(message);" + "\n"
                + "\t" + "\t" + "}" + "\n"
                + "\t" + "}" + "\n";
    }

    private String getSeedsCommands(UseCase useCase) {
        return ""
                + "\t" + "@Data" + "\n"
                + "\t" + "@NoArgsConstructor" + "\n"
                + "\t" + "@AllArgsConstructor" + "\n"
                + "\t" + "class SeedsCommands {" + "\n"
                + "\t" + "\t" + "boolean doSomething;" + "\n"
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.SeedsCommand)
                + "\t" + "}" + "\n";
    }

    private String getFruitSeeds(UseCase useCase) {
        return ""
                + "\t" + "@Data" + "\n"
                + "\t" + "class FruitSeeds {" + "\n"
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.FruitSeeds)
                + "\t" + "}" + "\n";
    }

    private String getPrepareException() {
        return ""
                + "\t" + "class PrepareException extends Exception {" + "\n"
                + "\t" + "\t" + "public PrepareException(String message) {" + "\n"
                + "\t" + "\t" + "\t" + "super(message);" + "\n"
                + "\t" + "\t" + "}" + "\n"
                + "\t" + "}" + "\n";
    }

    private String getDomainEntitiesImports(SoftwareFeature softwareFeature) {
        List<DomainEntity> domainEntities = this.domainEntitySpringJpaRepository.findAllBySoftwareFeature_Id(softwareFeature.getId());
        String content = "";
        for (DomainEntity domainEntity : domainEntities) {
            content += "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(softwareFeature) + "."
                    + "domain."
                    + domainEntity.getName()+";" + "\n";
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
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.DomainEntity)) {
                        attributeType = attribute.getDomainEntityAttributeType().getName();
                    }
                    content += "\t" + "\t" + attributeType + " " + attributeName + ";" + "\n";
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
                    content += "\t" + "\t" + "List<" + attributeType + ">" + " " + attributeName + ";" + "\n";
                }
            }
            if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)
                    && useCaseDataType.equals(UseCaseDataTypeEnum.Plant)) {
                content += "\t" + "\t" + "private PaginationCommand paginationCommand;" + "\n";
            }
            if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)
                    && useCaseDataType.equals(UseCaseDataTypeEnum.Fruit)) {
                content += "\t" + "\t" + "private PagedResultFruit pagedResultFruit;" + "\n";
            }
            return content;
        }

        return "";
    }
}
