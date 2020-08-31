package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service;

import ir.afarinesh.realhope.entities.feature.*;
import ir.afarinesh.realhope.entities.feature.enums.PrimitiveAttributeTypeEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.exceptions.GenerateListUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetGridListFruitDomainEntityException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetPlantException;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenerateListUseCaseServiceJavaFile {
    String t = StringUtility.space(4);
    String eol = StringUtility.endOfLine();
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final UseCaseService useCaseService;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;

    public GenerateListUseCaseServiceJavaFile(FileManagementService fileManagementService,
                                              UseCasePathService useCasePathService,
                                              UseCaseService useCaseService,
                                              DomainEntitySpringJpaRepository domainEntitySpringJpaRepository) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.useCaseService = useCaseService;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
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
        } catch (CreateFileException | GetGridListFruitDomainEntityException | GetPlantException e) {
            throw new GenerateListUseCaseServiceJavaFileException(e.getMessage());
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
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".usecase.*;" + eol
                + "import " + this.useCasePathService.getSharesPackageTitle(useCase.getSoftwareFeature()) + ".repositories." + useCase.getDataEntity().getName() + "SpringJpaRepository;" + eol
                + "import " + this.useCasePathService.getEntitiesPackageTitle(useCase.getSoftwareFeature()) + "." + useCase.getDataEntity().getCategory() + "." + useCase.getDataEntity().getName() + ";" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".application.ports.in." + useCaseTitle + "UseCase;" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".application.ports.in." + useCaseTitle + "UseCase.*;" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".domain.*;" + eol
                + "import ir.amnpardaz.enterprise.avshop.shares.utilities.CalendarUtility;" + eol
                + "import org.springframework.data.domain.PageRequest;" + eol
                + "import org.springframework.stereotype.Service;" + eol
                + "import org.springframework.data.domain.Page;" + eol
                + eol
                + this.getEnumsImports(useCase)
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
                + t + "final " + entitySpringJpaRepository + " " + StringUtility.firstLowerCase(entitySpringJpaRepository) + ";" + eol
                + eol
                + t + "public " + useCaseTitle + "Service(" + entitySpringJpaRepository + " " + StringUtility.firstLowerCase(entitySpringJpaRepository) + ") {" + eol
                + t + t + "this." + StringUtility.firstLowerCase(entitySpringJpaRepository) + " = " + StringUtility.firstLowerCase(entitySpringJpaRepository) + ";" + eol
                + t + "}" + eol
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
        List<UseCaseDataAttribute> enumAttributes = this.useCaseService
                .getFruitSeeds(useCase)
                .getUseCaseDataAttributes()
                .stream()
                .filter(attribute -> attribute.isSelectEnum())
                .collect(Collectors.toList());
        for (UseCaseDataAttribute attribute : enumAttributes) {
            if (attribute.isSelectEnum()) {
                content += t + t + "SelectEnum " + StringUtility.firstLowerCase(attribute.getName()) + "Enum = null;" + eol;
                content += t + t + "List<SelectEnum> " + StringUtility.firstLowerCase(attribute.getName()) + "EnumArray = "
                        + attribute.getDataEnum().getName()
                        + ".Void.getSelectEnumList(seedsCommand.getLocale());" + eol;
            }
        }
        content += t + t + "return new UseCaseFruitSeeds<>(" + eol;
        content += t + t + t + t + "new FruitSeeds(" + eol;
        for (int i = 0; i < enumAttributes.size(); i++) {
            UseCaseDataAttribute attribute = enumAttributes.get(i);
            if (attribute.isSelectEnum()) {
                content += t + t + t + t + t + t + StringUtility.firstLowerCase(attribute.getName()) + "Enum," + eol;
                content += t + t + t + t + t + t + StringUtility.firstLowerCase(attribute.getName()) + "EnumArray" + eol;
                content += (i < enumAttributes.size() - 1) ? "," : "";
            }
        }
        content += t + t + t + t + ")," + eol;
        content += t + t + t + t + "true," + eol;
        content += t + t + t + t + "\"\"" + eol;
        content += t + t + ");";

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
}
