package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service;

import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.entities.feature.enums.PrimitiveAttributeTypeEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.exceptions.GenerateUpdateUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetPlantException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetViewDomainEntityException;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateUpdateUseCaseServiceJavaFile {
    String t = StringUtility.space(4);
    String eol = StringUtility.endOfLine();
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final UseCaseService useCaseService;

    public GenerateUpdateUseCaseServiceJavaFile(FileManagementService fileManagementService,
                                                UseCasePathService useCasePathService,
                                                UseCaseService useCaseService) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.useCaseService = useCaseService;
    }

    public void generate(UseCase useCase) throws GenerateUpdateUseCaseServiceJavaFileException {
        try {
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getFileName(useCase),
                            this.getContent(useCase)
                    );
        } catch (CreateFileException | GetPlantException e) {
            throw new GenerateUpdateUseCaseServiceJavaFileException(e.getMessage());
        }
    }

    protected String getPath(UseCase useCase) {
        String pathSeparator = fileManagementService.pathSeparator();
        return useCasePathService.getSpringBootFeaturePath(useCase.getSoftwareFeature()) + pathSeparator
                + "application";
    }

    protected String getFileName(UseCase useCase) {
        return this.useCaseService.getUseCaseTitle(useCase) + "Service.java";
    }

    protected String getContent(UseCase useCase) throws GetPlantException {
        String useCaseTitle = this.useCaseService.getUseCaseTitle(useCase);
        String entitySpringJpaRepository = useCase.getDataEntity().getName() + "SpringJpaRepository";
        // package title
        String packageTitle = "package "
                + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + "."
                + "application;" + eol;
        // imports
        String imports = ""
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".annotations.FeatureApplication;" + eol
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".usecase.*;" + eol
                + "import " + this.useCasePathService.getSharesPackageTitle(useCase.getSoftwareFeature()) + ".repositories." + useCase.getDataEntity().getName() + "SpringJpaRepository;" + eol
                + "import " + this.useCasePathService.getEntitiesPackageTitle(useCase.getSoftwareFeature()) + "." + useCase.getDataEntity().getCategory() + "." + useCase.getDataEntity().getName() + ";" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".application.ports.in." + useCaseTitle + "UseCase;" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".domain.*;" + eol
                + "import " + this.useCasePathService.getSharesPackageTitle(useCase.getSoftwareFeature()) + ".utilities.CalendarUtility;" + eol
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".domain.*;" + eol
                + "import org.springframework.stereotype.Service;" + eol
                + eol
                + "import java.util.ArrayList;" + eol
                + "import java.util.List;" + eol
                + eol;
        String serviceContent = ""
                + "@Service" + eol
                + "@FeatureApplication" + eol
                + "public class " + useCaseTitle + "Service implements " + useCaseTitle + "UseCase {" + eol
                + eol
                + t + "// jpa repositories" + eol
                + t + "final " + entitySpringJpaRepository + " " + StringUtility.firstLowerCase(entitySpringJpaRepository) + ";" + eol
                + eol
                + t + "public " + useCaseTitle + "Service(" + entitySpringJpaRepository + " " + StringUtility.firstLowerCase(entitySpringJpaRepository) + ") {" + eol
                + t + t + "this." + StringUtility.firstLowerCase(entitySpringJpaRepository) + " = " + StringUtility.firstLowerCase(entitySpringJpaRepository) + ";" + eol
                + t + "}" + eol
                + eol
                + t + "@Override" + eol
                + t + "public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {" + eol
                + t + t + useCase.getDataEntity().getName() + " entity =" + eol
                + t + t + t + t + "this." + StringUtility.firstLowerCase(useCase.getDataEntity().getName()) + "SpringJpaRepository.findById(plant.getPlant().getId())" + eol
                + t + t + t + t + ".orElseThrow(() -> new CultivateException(\"Cannot find by id = \" + plant.getPlant().getId()));"
                + eol
                + t + t + "return new UseCaseFruit<>(" + eol
                + t + t + t + "new Fruit(" + eol
                + t + t + t + ")," + eol
                + t + t + t + "true," + eol
                + t + t + t + "\"\"" + eol
                + t + t + ");" + eol
                + t + "}" + eol
                + eol
                + t + "@Override" + eol
                + t + "public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {" + eol
                + t + t + useCase.getDataEntity().getName() + " entity =" + eol
                + t + t + t + t + "this." + StringUtility.firstLowerCase(useCase.getDataEntity().getName()) + "SpringJpaRepository.findById(seedsCommand.getSeedsCommand().getId())" + eol
                + t + t + t + t + ".orElseThrow(() -> new PrepareException(\"Cannot find by id = \" + seedsCommand.getSeedsCommand().getId()));"
                + eol
                + this.getFruitSeedsAttributes(useCase, t + t)
                + eol
                + t + t + "return new UseCaseFruitSeeds<>(" + eol
                + t + t + t + "new FruitSeeds(" + eol
                + this.getFruitSeedsAttributesInput(useCase, t + t + t + t)
                + t + t + t + ")," + eol
                + t + t + t + "true," + eol
                + t + t + t + "\"\"" + eol
                + t + t + ");" + eol
                + t + "}" + eol
                + eol
                + "\n"
                + "}";
        return packageTitle
                + eol
                + imports
                + eol
                + serviceContent;
    }

    private String getFruitSeedsAttributes(UseCase useCase, String offset) throws GetPlantException {
        String content = "";
        List<UseCaseDataAttribute> attributes = this.useCaseService.getFruitSeeds(useCase).getUseCaseDataAttributes();
        for (UseCaseDataAttribute attribute: attributes) {
            if (attribute.isPrimitive()) {
                content += offset + attribute.getPrimitiveAttributeType().name() + " " + StringUtility.firstLowerCase(attribute.getName())
                        + " = " +  this.getFruitSeedsAttributeGetter(attribute, false, false) + ";" + eol;
            }
            if (attribute.isSelectEnum()) {
                content += offset + "SelectEnum " + StringUtility.firstLowerCase(attribute.getName()) + "Enum"
                        + " = " + this.getFruitSeedsAttributeGetter(attribute, false, false) + ";" + eol;
                content += offset + "List<SelectEnum> " + StringUtility.firstLowerCase(attribute.getName()) + "EnumList"
                        + " = " + this.getFruitSeedsAttributeGetter(attribute, true, false) + ";" + eol;
            }

            if (attribute.isSelectEntity()) {
                content += offset + "SelectEntity " + StringUtility.firstLowerCase(attribute.getName())
                        + " = " + this.getFruitSeedsAttributeGetter(attribute, false, false) + ";" + eol;
                content += offset + "List<SelectEntity> " + StringUtility.firstLowerCase(attribute.getName()) + "List"
                        + " = " + this.getFruitSeedsAttributeGetter(attribute, false, true) + ";" + eol;
            }
        }
        return content;
    }

    private String getFruitSeedsAttributesInput(UseCase useCase, String offset) throws GetPlantException {
        String content = "";
        List<UseCaseDataAttribute> attributes = this.useCaseService.getFruitSeeds(useCase).getUseCaseDataAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            UseCaseDataAttribute attribute = attributes.get(i);
            if (attribute.isPrimitive()) {
                content += offset + StringUtility.firstLowerCase(attribute.getName());
            }
            if (attribute.isSelectEnum()) {
                content += offset + StringUtility.firstLowerCase(attribute.getName()) + "Enum" + "," + eol;
                content += offset + StringUtility.firstLowerCase(attribute.getName()) + "EnumList";
            }

            if (attribute.isSelectEntity()) {
                content += offset + StringUtility.firstLowerCase(attribute.getName()) + "," + eol;
                content += offset + StringUtility.firstLowerCase(attribute.getName()) + "List";
            }
            content += (i < attributes.size() - 1) ? "," : "";
            content += eol;
        }
        return content;
    }

    private String getFruitSeedsAttributeGetter(UseCaseDataAttribute attribute, boolean isEnumList, boolean isEntityList) {
        String content = "";
        String sequenceOfGetters = this.useCaseService.getSequenceOfGetters(attribute.getGetterOfUpdatePath());
        if (sequenceOfGetters != null) {
            if (attribute.isPrimitive()) {
                // JavaDate begin
                if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.JavaDate)) {
                    content += "CalendarUtility.getJavaDate(";
                }
                content += "entity." + sequenceOfGetters;
                // JavaDate end
                if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.JavaDate)) {
                    content += ")";
                }
            }
            if (attribute.isSelectEnum()) {
                if (isEnumList) {
                    content += "entity." + sequenceOfGetters + ".getSelectEnumList(seedsCommand.getLocale())";
                } else {
                    content += "new SelectEnum("
                            + "entity." + sequenceOfGetters + ".title(seedsCommand.getLocale()), "
                            + "entity." + sequenceOfGetters + ".name()"
                            + ")";
                }
            }

            if (attribute.isSelectEntity()) {
                if (isEntityList) {
                    content += "new ArrayList<SelectEntity>()";
                } else {
                    content += "null";
                }
            }
        } else {
            content += "null";
        }
        return content;
    }
}
