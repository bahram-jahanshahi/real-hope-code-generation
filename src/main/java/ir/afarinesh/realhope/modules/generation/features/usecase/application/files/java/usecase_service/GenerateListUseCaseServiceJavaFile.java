package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service;

import ir.afarinesh.realhope.entities.data_model.DataEntity;
import ir.afarinesh.realhope.entities.data_model.DataEntityAttribute;
import ir.afarinesh.realhope.entities.feature.*;
import ir.afarinesh.realhope.entities.feature.enums.PrimitiveAttributeTypeEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.exceptions.GenerateListUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetGridListFruitDomainEntityException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetPlantException;
import ir.afarinesh.realhope.shares.repositories.DataEntityAttributeSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenerateListUseCaseServiceJavaFile {
    String t = StringUtility.space(4);
    String eol = StringUtility.endOfLine();
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final UseCaseService useCaseService;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;
    final DataEntityAttributeSpringJpaRepository dataEntityAttributeSpringJpaRepository;

    public GenerateListUseCaseServiceJavaFile(FileManagementService fileManagementService,
                                              UseCasePathService useCasePathService,
                                              UseCaseService useCaseService,
                                              DomainEntitySpringJpaRepository domainEntitySpringJpaRepository,
                                              DataEntityAttributeSpringJpaRepository dataEntityAttributeSpringJpaRepository) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.useCaseService = useCaseService;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
        this.dataEntityAttributeSpringJpaRepository = dataEntityAttributeSpringJpaRepository;
    }

    public void generate(UseCase useCase) throws GenerateListUseCaseServiceJavaFileException {
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
            // Repository
            fileManagementService
                    .createFile(
                            this.getRepositoryPath(useCase),
                            this.getRepositoryFileName(useCase),
                            this.getRepositoryContent(useCase),
                            true
                    );
        } catch (CreateFileException | GetGridListFruitDomainEntityException | GetPlantException e) {
            throw new GenerateListUseCaseServiceJavaFileException(e.getMessage());
        }
    }

    protected String getPath(UseCase useCase) {
        String pathSeparator = fileManagementService.pathSeparator();
        return useCasePathService.getSpringBootFeaturePath(useCase.getSoftwareFeature()) + pathSeparator
                + "application";
    }

    protected String getRepositoryPath(UseCase useCase) {
        String pathSeparator = fileManagementService.pathSeparator();
        return useCasePathService.getSpringBootFeaturePath(useCase.getSoftwareFeature()) + pathSeparator
                + "adapter" + pathSeparator + "repositories";
    }

    protected String getRepositoryFileName(UseCase useCase) {
        return this.useCaseService.getUseCaseTitle(useCase) + "RepositoryImpl.java";
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
                + "import org.springframework.stereotype.Service;" + eol;
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

    private String getRepositoryContent(UseCase useCase) throws GetPlantException {
        UseCaseData plant = useCaseService.getPlant(useCase);
        // List<DataEntityAttribute> attributes = this.dataEntityAttributeSpringJpaRepository.findByDataEntity_Id(useCase.getDataEntity().getId());
        List<UseCaseDataAttribute> attributes = plant.getUseCaseDataAttributes()
                .stream()
                .filter(attribute -> attribute.getRelatedDataEntityAttribute() != null)
                .collect(Collectors.toList());
        String useCaseTitle = this.useCaseService.getUseCaseTitle(useCase);
        // package title
        String packageTitle = "package "
                + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + "."
                + "adapter.repositories;" + eol;
        String imports = ""
                + "import " + this.useCasePathService.getEntitiesPackageTitle(useCase.getSoftwareFeature()) + "." + useCase.getDataEntity().getCategory() + "." + useCase.getDataEntity().getName() + ";" + eol
                + "import org.springframework.data.repository.CrudRepository;" + eol
                + "import org.springframework.data.domain.Page;" + eol
                + "import org.springframework.data.domain.Pageable;" + eol
                + "import org.springframework.data.jpa.repository.Query;" + eol
                + "import org.springframework.data.repository.query.Param;" + eol
                + "import java.util.Date;" + eol;
        String classContent = "";
        classContent += "public interface " + useCaseTitle + "RepositoryImpl extends CrudRepository<" + useCase.getDataEntity().getName() + ", Long> {" + eol;
        classContent += t + "@Query(value=\"" + "select Entity from " + useCase.getDataEntity().getName() + " as Entity where 1 = 1 \" +" + eol;
        for (int i = 0; i < attributes.size(); i++) {
            UseCaseDataAttribute attribute = attributes.get(i);
            if (attribute.isPrimitive()) {
                if (!attribute.getName().equals("Id") && (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.JavaDate) || attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Long) || attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Integer))) {
                    String operator = "=";
                    if (isAttributeBegin(attribute.getName())) {
                        operator = ">=";
                    }
                    if (isAttributeEnd(attribute.getName())) {
                        operator = "<=";
                    }
                    classContent += t + t + t + "\" and ( "
                            + ":" + StringUtility.firstLowerCase(attribute.getName()) + " is null or "
                            + "("
                            + "Entity." + StringUtility.firstLowerCase(attribute.getRelatedDataEntityAttribute().getName()) + " " + operator + " :" + StringUtility.firstLowerCase(attribute.getName()) + ""
                            + ")"
                            + ") \" +" + eol;
                }
                if (attribute.getName().equals("Id")) {
                    classContent += t + t + t + "\" and ( :" + StringUtility.firstLowerCase(attribute.getName()) + " is null or Entity." + StringUtility.firstLowerCase(attribute.getRelatedDataEntityAttribute().getName()) + " = :" + StringUtility.firstLowerCase(attribute.getName()) + " ) \" +" + eol;
                }
                if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.String)) {
                    classContent += t + t + t + "\" and ( :" + StringUtility.firstLowerCase(attribute.getName()) + " is null or Entity." + StringUtility.firstLowerCase(attribute.getRelatedDataEntityAttribute().getName()) + " like %:" + StringUtility.firstLowerCase(attribute.getName()) + "% ) \" +" + eol;
                }
                if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Boolean)) {
                    classContent += t + t + t + "\" and ( :" + StringUtility.firstLowerCase(attribute.getName()) + " is null or Entity." + StringUtility.firstLowerCase(attribute.getRelatedDataEntityAttribute().getName()) + " = :" + StringUtility.firstLowerCase(attribute.getName()) + " ) \" +" + eol;
                }
            }
            if (attribute.isSelectEnum()) {
                classContent += t + t + t + "\" and ( :" + StringUtility.firstLowerCase(attribute.getName()) + " is null or Entity." + StringUtility.firstLowerCase(attribute.getRelatedDataEntityAttribute().getName()) + " = :" + StringUtility.firstLowerCase(attribute.getName()) + " ) \" +" + eol;
            }
            if (attribute.isSelectEntity()) {
                classContent += t + t + t + "\" and ( :" + StringUtility.firstLowerCase(attribute.getName()) + " is null or Entity." + StringUtility.firstLowerCase(attribute.getRelatedDataEntityAttribute().getName()) + ".id = :" + StringUtility.firstLowerCase(attribute.getName()) + " ) \" +" + eol;
            }
        }
        classContent += t + t + t + "\"\"" + eol;
        classContent += t + ")" + eol;
        classContent += t + "Page<" + useCase.getDataEntity().getName() + "> findAll(" + eol;
        for (int i = 0; i < attributes.size(); i++) {
            UseCaseDataAttribute attribute = attributes.get(i);
            if (attribute.isPrimitive()) {
                if (!attribute.getName().equals("Id") && (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.JavaDate) || attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Long) || attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Integer))) {
                    if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.JavaDate)) {
                        classContent += t + t + "@Param(\"" + StringUtility.firstLowerCase(attribute.getName()) + "\") ";
                        classContent += "Date " + StringUtility.firstLowerCase(attribute.getName()) + "," + eol;
                    } else {
                        classContent += t + t + "@Param(\"" + StringUtility.firstLowerCase(attribute.getName()) + "\") ";
                        classContent += attribute.getPrimitiveAttributeType().name() + " " + StringUtility.firstLowerCase(attribute.getName()) + "," + eol;
                    }
                } else {
                    classContent += t + t + "@Param(\"" + StringUtility.firstLowerCase(attribute.getName()) + "\") ";
                    classContent += attribute.getPrimitiveAttributeType().name() + " " + StringUtility.firstLowerCase(attribute.getName()) + "," + eol;
                }
            }
            if (attribute.isSelectEnum()) {
                classContent += t + t + "@Param(\"" + StringUtility.firstLowerCase(attribute.getName()) + "\") ";
                classContent += "String " + StringUtility.firstLowerCase(attribute.getName()) + "," + eol;
            }
            if (attribute.isSelectEntity()) {
                classContent += t + t + "@Param(\"" + StringUtility.firstLowerCase(attribute.getName()) + "\") ";
                classContent += "Long " + StringUtility.firstLowerCase(attribute.getName()) + "," + eol;
            }
        }
        classContent += t + t + "Pageable pageable" + eol;
        classContent += t + ");" + eol;
        classContent += "}";
        return packageTitle + eol + imports + eol + classContent;
    }

    protected String getFileName(UseCase useCase) {
        return useCase.getName() + "By" + useCase.getSoftwareRole().getName() + "Service.java";
    }

    protected String getContent(UseCase useCase) throws GetGridListFruitDomainEntityException, GetPlantException {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String entitySpringJpaRepository = useCase.getDataEntity().getName() + "SpringJpaRepository";
        DomainEntity gridListFruitDomainEntity = useCaseService.getGridListFruitDomainEntity(useCase);
        String t = StringUtility.space(4);
        String packageTitle = "package "
                + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + "."
                + "application;"
                + eol;
        String imports = ""
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".annotations.FeatureApplication;" + eol
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".domain.SelectEnum;" + eol
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".domain.SelectEntity;" + eol
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".usecase.*;" + eol
                + "import " + this.useCasePathService.getSharesPackageTitle(useCase.getSoftwareFeature()) + ".repositories." + useCase.getDataEntity().getName() + "SpringJpaRepository;" + eol
                + "import " + this.useCasePathService.getEntitiesPackageTitle(useCase.getSoftwareFeature()) + "." + useCase.getDataEntity().getCategory() + "." + useCase.getDataEntity().getName() + ";" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".application.ports.in." + useCaseTitle + "UseCase;" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".application.ports.in." + useCaseTitle + "UseCase.*;" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".domain.*;" + eol
                + "import " + this.useCasePathService.getSharesPackageTitle(useCase.getSoftwareFeature()) + ".utilities.CalendarUtility;" + eol
                + "import org.springframework.data.domain.PageRequest;" + eol
                + "import org.springframework.stereotype.Service;" + eol
                + "import org.springframework.data.domain.Page;" + eol
                + eol
                + this.getEnumsImports(useCase)
                + eol
                + this.getSpringJpaRepositoriesImports(useCase)
                + eol
                + "import java.util.stream.Collectors;" + eol
                + "import java.util.List;" + eol
                + eol;
        String serviceContent = ""
                + "@Service" + eol
                + "@FeatureApplication" + eol
                + "public class " + useCaseTitle + "Service {" + eol
                + eol
                + t + "// jpa repositories" + eol
                + this.getSpringJpaRepositories(useCase)
                + eol
                + this.getConstructor(useCase)
                + eol
                + t + "public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {" + eol
                + t + t + "Page<" + useCase.getDataEntity().getName() + "> page = " + "this." + StringUtility.firstLowerCase(entitySpringJpaRepository) + eol
                + t + t + t + t + ".findAll(PageRequest.of(plant.getPlant().getPaginationCommand().getPageIndex(), plant.getPlant().getPaginationCommand().getPageSize()));" + eol
                + t + t + "List<" + gridListFruitDomainEntity.getName() + "> list = page" + eol
                + t + t + t + t + ".get()" + eol
                + t + t + t + t + ".map(entity -> this.map" + gridListFruitDomainEntity.getName() + "(entity, plant.getLocale()))" + eol
                + t + t + t + t + ".collect(Collectors.toList());" + eol
                + t + t + "return new UseCaseFruit<>(" + eol
                + t + t + t + t + "new Fruit(" + eol
                + t + t + t + t + t + t + "list," + eol
                + t + t + t + t + t + t + "new PagedResultFruit(page.getTotalElements(), page.getTotalPages())" + eol
                + t + t + t + t + ")," + eol
                + t + t + t + t + "true," + eol
                + t + t + t + t + "\"\"" + eol
                + t + t + ");" + eol
                + t + "}" + eol
                + eol
                + t + "public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {" + eol
                + this.getPrepare(useCase)
                + t + "}" + eol
                + eol
                + t + "public " + useCaseService.getGridListFruitDomainEntity(useCase).getName() + " map" + useCaseService.getGridListFruitDomainEntity(useCase).getName() + "(" + useCase.getDataEntity().getName() + " entity, String locale){" + eol
                + t + t + "return new " + useCaseService.getGridListFruitDomainEntity(useCase).getName() + "(" + eol
                + this.useCaseService.getDomainEntityMapArguments(gridListFruitDomainEntity)
                + t + t + ");" + eol
                + t + "}" + eol
                + "}";
        return packageTitle
                + eol
                + imports
                + eol
                + serviceContent;
    }

    private String getPrepare(UseCase useCase) throws GetPlantException {
        String content = "";
        List<UseCaseDataAttribute> attributes = this.useCaseService
                .getFruitSeeds(useCase)
                .getUseCaseDataAttributes()
                .stream()
                .filter(attribute -> attribute.isSelectEntity() || attribute.isSelectEnum())
                .collect(Collectors.toList());
        for (UseCaseDataAttribute attribute : attributes) {
            if (attribute.isSelectEnum()) {
                content += t + t + "SelectEnum " + StringUtility.firstLowerCase(attribute.getName()) + "Enum = null;" + eol;
                content += t + t + "List<SelectEnum> " + StringUtility.firstLowerCase(attribute.getName()) + "EnumArray = "
                        + attribute.getDataEnum().getName()
                        + ".Void.getSelectEnumList(seedsCommand.getLocale());" + eol;
            }
            if (attribute.isSelectEntity()) {
                content += t + t + "SelectEntity " + StringUtility.firstLowerCase(attribute.getName()) + " = null;" + eol;
                content += t + t + "List<SelectEntity> " + StringUtility.firstLowerCase(attribute.getName()) + "Array = "
                        + StringUtility.firstLowerCase(attribute.getDataEntityAttributeType().getName()) + "SpringJpaRepository" + eol
                        + t + t + t + t + ".findAll()" + eol
                        + t + t + t + t + ".stream()" + eol
                        + t + t + t + t + ".map(obj -> new SelectEntity(obj.title(seedsCommand.getLocale()), obj.getId()))" + eol
                        + t + t + t + t + ".collect(Collectors.toList());" + eol;
            }
        }
        content += t + t + "return new UseCaseFruitSeeds<>(" + eol;
        content += t + t + t + t + "new FruitSeeds(" + eol;
        for (int i = 0; i < attributes.size(); i++) {
            UseCaseDataAttribute attribute = attributes.get(i);
            if (attribute.isSelectEnum()) {
                content += t + t + t + t + t + t + StringUtility.firstLowerCase(attribute.getName()) + "Enum," + eol;
                content += t + t + t + t + t + t + StringUtility.firstLowerCase(attribute.getName()) + "EnumArray";
            }
            if (attribute.isSelectEntity()) {
                content += t + t + t + t + t + t + StringUtility.firstLowerCase(attribute.getName()) + "," + eol;
                content += t + t + t + t + t + t + StringUtility.firstLowerCase(attribute.getName()) + "Array";
            }
            content += ((i < attributes.size() - 1) ? "," : "") + eol;
        }
        content += t + t + t + t + ")," + eol;
        content += t + t + t + t + "true," + eol;
        content += t + t + t + t + "\"\"" + eol;
        content += t + t + ");" + eol;

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

    protected boolean isAttributeBegin(String name) {
        if (name.length() < 7) {
            return false;
        }
        int length = name.length();
        String begin = name.substring(length - 5, length);
        return begin.equals("Begin");
    }

    protected boolean isAttributeEnd(String name) {
        if (name.length() < 5) {
            return false;
        }
        int length = name.length();
        String end = name.substring(length - 3, length);
        return end.equals("End");
    }
}
