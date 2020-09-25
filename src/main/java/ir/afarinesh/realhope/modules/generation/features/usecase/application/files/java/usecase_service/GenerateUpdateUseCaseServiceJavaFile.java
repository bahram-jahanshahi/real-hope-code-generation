package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service;

import ir.afarinesh.realhope.entities.data_model.DataEntity;
import ir.afarinesh.realhope.entities.data_model.DataEntityAttribute;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.entities.feature.enums.PrimitiveAttributeTypeEnum;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.exceptions.GenerateUpdateUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetPlantException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetViewDomainEntityException;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
            // Service
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getFileName(useCase),
                            this.getContent(useCase),
                            true
                    );
            // Service Impl
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getFileNameImpl(useCase),
                            this.getContentImpl(useCase),
                            false
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

    protected String getFileNameImpl(UseCase useCase) {
        return this.useCaseService.getUseCaseTitle(useCase) + "ServiceImpl.java";
    }

    protected String getContentImpl(UseCase useCase) {
        String useCaseTitle = this.useCaseService.getUseCaseTitle(useCase);
        // package title
        String packageTitle = "package "
                + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + "."
                + "application;" + eol;
        // imports
        String imports = ""
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".annotations.FeatureApplication;" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".application.ports.in." + useCaseTitle + "UseCase;" + eol
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".usecase.*;" + eol
                + "import org.springframework.stereotype.Service;" + eol
                + "import org.springframework.transaction.annotation.Transactional;" + eol;
        // content
        String serviceContent = ""
                + "@Service" + eol
                + "@FeatureApplication" + eol
                + "public class " + useCaseTitle + "ServiceImpl implements " + useCaseTitle + "UseCase {" + eol
                + eol
                + t + "final " + useCaseTitle + "Service service;" + eol
                + eol
                + t + "public " + useCaseTitle + "ServiceImpl(" + useCaseTitle + "Service service) {" + eol
                + t + t + "this.service = service;" + eol
                + t + "}" + eol
                + eol
                + t + "@Override" + eol
                + t + "@Transactional" + eol
                + t + "public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {" + eol
                + t + t + "return this.service.cultivate(plant);" + eol
                + t + "}" + eol
                + eol
                + t + "@Override" + eol
                + t + "public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {" + eol
                + t + t + "return this.service.prepare(seedsCommand);" + eol
                + t + "}"
                + eol
                + "}";

        return packageTitle + imports + eol + serviceContent;
    }

    protected String getFileName(UseCase useCase) {
        return this.useCaseService.getUseCaseTitle(useCase) + "Service.java";
    }

    protected String getContent(UseCase useCase) throws GetPlantException {
        String useCaseTitle = this.useCaseService.getUseCaseTitle(useCase);
        // package title
        String packageTitle = "package "
                + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + "."
                + "application;" + eol;
        // imports
        String imports = ""
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".annotations.FeatureApplication;" + eol
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".usecase.*;" + eol
                + "import " + this.useCasePathService.getEntitiesPackageTitle(useCase.getSoftwareFeature()) + "." + useCase.getDataEntity().getCategory() + "." + useCase.getDataEntity().getName() + ";" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".application.ports.in." + useCaseTitle + "UseCase;" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".application.ports.in." + useCaseTitle + "UseCase.*;" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".domain.*;" + eol
                + "import " + this.useCasePathService.getSharesPackageTitle(useCase.getSoftwareFeature()) + ".utilities.CalendarUtility;" + eol
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".domain.*;" + eol
                + "import org.springframework.stereotype.Service;" + eol
                + "import org.springframework.transaction.annotation.Transactional;" + eol
                + eol
                + "import java.util.stream.Collectors;" + eol
                + "import java.util.ArrayList;" + eol
                + "import java.util.List;" + eol
                + eol
                + this.getSpringJpaRepositoriesImports(useCase)
                + this.getEnumsImports(useCase)
                + eol;
        // content
        String serviceContent = ""
                + "@Service" + eol
                + "public class " + useCaseTitle + "Service {" + eol
                + eol
                + t + "// jpa repositories" + eol
                + this.getSpringJpaRepositories(useCase)
                + eol
                + this.getConstructor(useCase)
                + eol
                + t + "@Transactional" + eol
                + t + "public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {" + eol
                + this.getCultivate(useCase, t + t)
                + t + "}" + eol
                + eol
                + t + "public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {" + eol
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
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.Update)) {
            content += ""
                    + t + t + useCase.getDataEntity().getName() + " entity =" + eol
                    + t + t + t + t + "this." + StringUtility.firstLowerCase(useCase.getDataEntity().getName()) + "SpringJpaRepository.findById(seedsCommand.getSeedsCommand().getId())" + eol
                    + t + t + t + t + ".orElseThrow(() -> new PrepareException(\"Cannot find by id = \" + seedsCommand.getSeedsCommand().getId()));" + eol;
        }
        List<UseCaseDataAttribute> attributes = this.useCaseService.getFruitSeeds(useCase).getUseCaseDataAttributes();
        for (UseCaseDataAttribute attribute : attributes) {
            if (attribute.isPrimitive()) {
                content += offset + attribute.getPrimitiveAttributeType().name() + " " + StringUtility.firstLowerCase(attribute.getName())
                        + " = " + this.getFruitSeedsAttributeGetter(attribute, false, false, useCase.getUserInterfaceType()) + ";" + eol;
            }
            if (attribute.isSelectEnum()) {
                content += offset + "SelectEnum " + StringUtility.firstLowerCase(attribute.getName()) + "Enum"
                        + " = " + this.getFruitSeedsAttributeGetter(attribute, false, false, useCase.getUserInterfaceType()) + ";" + eol;
                content += offset + "List<SelectEnum> " + StringUtility.firstLowerCase(attribute.getName()) + "EnumList"
                        + " = " + this.getFruitSeedsAttributeGetter(attribute, true, false, useCase.getUserInterfaceType()) + ";" + eol;
            }

            if (attribute.isSelectEntity()) {
                content += offset + "SelectEntity " + StringUtility.firstLowerCase(attribute.getName())
                        + " = " + this.getFruitSeedsAttributeGetter(attribute, false, false, useCase.getUserInterfaceType()) + ";" + eol;
                content += offset + "List<SelectEntity> " + StringUtility.firstLowerCase(attribute.getName()) + "List"
                        + " = " + this.getFruitSeedsAttributeGetter(attribute, false, true, useCase.getUserInterfaceType()) + ";" + eol;
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

    private String getFruitSeedsAttributeGetter(UseCaseDataAttribute attribute, boolean isEnumList, boolean isEntityList, UserInterfaceTypeEnum userInterfaceType) {
        String content = "";
        String sequenceOfGetters = this.useCaseService.getSequenceOfGetters(attribute.getGetterOfUpdatePath());
        String sequenceOfIfNotNullGetters = this.useCaseService.getSequenceOfIfNotNullGetters(attribute.getGetterOfUpdatePath());
        if (sequenceOfGetters != null) {
            // Primitive
            if (attribute.isPrimitive()) {
                if (userInterfaceType.equals(UserInterfaceTypeEnum.Update)) {
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
                if (userInterfaceType.equals(UserInterfaceTypeEnum.AddNew)) {
                    content += "null";
                }
            }
            // Enum
            if (attribute.isSelectEnum()) {
                if (userInterfaceType.equals(UserInterfaceTypeEnum.Update)) {
                    if (isEnumList) {
                        content += attribute.getDataEnum().getName() + ".Void.getSelectEnumList(seedsCommand.getLocale())";
                    } else {
                        content += sequenceOfIfNotNullGetters + " ? ";
                        content += "entity." + sequenceOfGetters + ".getSelectEnum(seedsCommand.getLocale())";
                        content += " : ";
                        content += " new SelectEnum()";
                    }
                }
                if (userInterfaceType.equals(UserInterfaceTypeEnum.AddNew)) {
                    if (isEnumList) {
                        content += attribute.getDataEnum().getName() + ".Void.getSelectEnumList(seedsCommand.getLocale())";
                    } else {
                        content += "new SelectEnum()";
                    }
                }
            }
            // Data Entity
            if (attribute.isSelectEntity()) {
                if (isEntityList) {
                    content += "this." + StringUtility.firstLowerCase(attribute.getDataEntityAttributeType().getName()) + "SpringJpaRepository" + eol
                            + t + t + t + t + ".findAll()" + eol
                            + t + t + t + t + ".stream()" + eol
                            + t + t + t + t + ".map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))" + eol
                            + t + t + t + t + ".collect(Collectors.toList())";
                } else {
                    if (userInterfaceType.equals(UserInterfaceTypeEnum.Update)) {
                        content += sequenceOfIfNotNullGetters + " ? ";
                        content += "new SelectEntity(entity." + sequenceOfGetters + ".title(seedsCommand.getLocale()), entity." + sequenceOfGetters + ".getId())";
                        content += " : ";
                        content += " new SelectEntity()";
                    }
                    if (userInterfaceType.equals(UserInterfaceTypeEnum.AddNew)) {
                        content += "new SelectEntity()";
                    }
                }
            }
        } else {
            content += "null";
        }
        return content;
    }

    private String getSpringJpaRepositories(UseCase useCase) throws GetPlantException {
        Set<DataEntity> dataEntities = new HashSet<>();
        dataEntities.add(useCase.getDataEntity());
        List<UseCaseDataAttribute> attributes = this.useCaseService.getFruitSeeds(useCase).getUseCaseDataAttributes();
        for (UseCaseDataAttribute attribute : attributes) {
            if (attribute.isSelectEntity() && attribute.getDataEntityAttributeType() != null) {
                dataEntities.add(attribute.getDataEntityAttributeType());
            }
        }
        String content = "";
        List<DataEntity> dataEntitiesArray = new ArrayList<>(dataEntities);
        for (DataEntity dataEntity : dataEntitiesArray) {
            content += t + "final " + dataEntity.getName() + "SpringJpaRepository " + StringUtility.firstLowerCase(dataEntity.getName()) + "SpringJpaRepository;" + eol;
        }
        return content;
    }

    private String getConstructor(UseCase useCase) throws GetPlantException {
        Set<DataEntity> dataEntities = new HashSet<>();
        dataEntities.add(useCase.getDataEntity());
        List<UseCaseDataAttribute> attributes = this.useCaseService.getFruitSeeds(useCase).getUseCaseDataAttributes();
        for (UseCaseDataAttribute attribute : attributes) {
            if (attribute.isSelectEntity() && attribute.getDataEntityAttributeType() != null) {
                dataEntities.add(attribute.getDataEntityAttributeType());
            }
        }
        String content = t + "public " + useCaseService.getUseCaseTitle(useCase) + "Service(";
        List<DataEntity> dataEntitiesArray = new ArrayList<>(dataEntities);
        for (int i = 0; i < dataEntitiesArray.size(); i++) {
            DataEntity dataEntity = dataEntitiesArray.get(i);
            content += dataEntity.getName() + "SpringJpaRepository " + StringUtility.firstLowerCase(dataEntity.getName()) + "SpringJpaRepository";
            content += (i < dataEntities.size() - 1) ? ", " : "";
        }
        content += "){" + eol;
        for (int i = 0; i < dataEntitiesArray.size(); i++) {
            DataEntity dataEntity = dataEntitiesArray.get(i);
            content += t + t + "this." + StringUtility.firstLowerCase(dataEntity.getName()) + "SpringJpaRepository = " + StringUtility.firstLowerCase(dataEntity.getName()) + "SpringJpaRepository;" + eol;
        }
        content += t + "}" + eol;
        return content;
    }

    private String getSpringJpaRepositoriesImports(UseCase useCase) throws GetPlantException {
        Set<DataEntity> dataEntities = new HashSet<>();
        dataEntities.add(useCase.getDataEntity());
        List<UseCaseDataAttribute> attributes = this.useCaseService.getFruitSeeds(useCase).getUseCaseDataAttributes();
        for (UseCaseDataAttribute attribute : attributes) {
            if (attribute.isSelectEntity() && attribute.getDataEntityAttributeType() != null) {
                dataEntities.add(attribute.getDataEntityAttributeType());
            }
        }
        String content = "";
        List<DataEntity> dataEntitiesArray = new ArrayList<>(dataEntities);
        for (DataEntity dataEntity : dataEntitiesArray) {
            content += "import " + this.useCasePathService.getSharesPackageTitle(useCase.getSoftwareFeature()) + ".repositories." + dataEntity.getName() + "SpringJpaRepository;" + eol;
        }
        return content;
    }

    private String getEnumsImports(UseCase useCase) throws GetPlantException {
        String content = "";
        List<UseCaseDataAttribute> attributes = this.useCaseService.getFruitSeeds(useCase).getUseCaseDataAttributes();
        for (UseCaseDataAttribute attribute : attributes) {
            if (attribute.isSelectEnum() && attribute.getDataEnum() != null) {
                content += "import " + this.useCasePathService.getEntitiesPackageTitle(useCase.getSoftwareFeature()) + "." + attribute.getDataEnum().getCategory() + ".enums." + attribute.getDataEnum().getName() + ";" + eol;
            }
        }
        return content;
    }

    private String getCultivate(UseCase useCase, String offset) throws GetPlantException {
        UseCaseData plant = this.useCaseService.getPlant(useCase);
        List<UseCaseDataAttribute> plantAttributes = plant
                .getUseCaseDataAttributes()
                .stream()
                .filter(attribute -> attribute.getRelatedDataEntityAttribute() != null)
                .collect(Collectors.toList());
        String content = "";
        content += offset + "// Entity" + eol;
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.Update)) {
            content += ""
                    + offset + useCase.getDataEntity().getName() + " entity =" + eol
                    + offset + t + t + "this." + StringUtility.firstLowerCase(useCase.getDataEntity().getName()) + "SpringJpaRepository.findById(plant.getPlant().getId())" + eol
                    + offset + t + t + t + ".orElseThrow(() -> new CultivateException(\"Cannot find by id = \" + plant.getPlant().getId()));" + eol;
        }
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.AddNew)) {
            content += offset + useCase.getDataEntity().getName() + " entity = new " + useCase.getDataEntity().getName() + "();" + eol;
        }
        content += offset + "// Setters" + eol;
        for (UseCaseDataAttribute attribute: plantAttributes) {
            DataEntityAttribute dataEntityAttribute = attribute.getRelatedDataEntityAttribute();
            if (attribute.isPrimitive()) {
                if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.JavaDate)) {
                    content += offset + "entity.set" + dataEntityAttribute.getName() + "(CalendarUtility.getDate(plant.getPlant().get" + attribute.getName() + "()));" + eol;
                } else {
                    content += offset + "entity.set" + dataEntityAttribute.getName() + "(plant.getPlant().get" + attribute.getName() + "());" + eol;
                }
            }
            if (attribute.isSelectEnum()) {
                content += offset + "entity.set" + dataEntityAttribute.getName() + "(" + dataEntityAttribute.getDataEnum().getName()
                        + ".findByName(plant.getPlant().get" + attribute.getName() + "Enum().getValue()));" + eol;
            }
            if (attribute.isSelectEntity()) {
                content += offset + "entity.set" + attribute.getName() + "(plant.getPlant().get" + attribute.getName() + "().getValue() != null ? "
                        + StringUtility.firstLowerCase(dataEntityAttribute.getDataEntityAttributeType().getName()) + "SpringJpaRepository.findById(plant.getPlant().get" + attribute.getName() + "().getValue()).orElseThrow() : null);" + eol;
            }
        }
        content += offset + "// Save or update" + eol;
        content += offset + "this." + StringUtility.firstLowerCase(useCase.getDataEntity().getName()) + "SpringJpaRepository.save(entity);" + eol;
        content += ""
                + offset + "// Return" + eol
                + offset + "return new UseCaseFruit<>(" + eol
                + offset + t + "new Fruit(" + eol
                + offset + t + t + t + "entity.getId()" + eol
                + offset + t + ")," + eol
                + offset + t + "true," + eol
                + offset + t + "\"\"" + eol
                + offset + ");" + eol;
        return content;
    }
}
