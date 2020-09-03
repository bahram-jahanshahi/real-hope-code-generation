package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service;

import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.exceptions.GenerateViewUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetViewDomainEntityException;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

@Service
public class GenerateViewUseCaseServiceJavaFile {
    String t = StringUtility.space(4);
    String eol = StringUtility.endOfLine();
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final UseCaseService useCaseService;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;

    public GenerateViewUseCaseServiceJavaFile(FileManagementService fileManagementService,
                                              UseCasePathService useCasePathService,
                                              UseCaseService useCaseService,
                                              DomainEntitySpringJpaRepository domainEntitySpringJpaRepository) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.useCaseService = useCaseService;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
    }

    public void generate(UseCase useCase) throws GenerateViewUseCaseServiceJavaFileException {
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
        } catch (CreateFileException | GetViewDomainEntityException e) {
            throw new GenerateViewUseCaseServiceJavaFileException(e.getMessage());
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

    protected String getContent(UseCase useCase) throws GetViewDomainEntityException {
        String useCaseTitle = useCaseService.getUseCaseTitle(useCase);
        String entitySpringJpaRepository = useCase.getDataEntity().getName() + "SpringJpaRepository";
        DomainEntity viewDomainEntity = useCaseService.getViewDomainEntity(useCase);
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
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".application.ports.in." + useCaseTitle + "UseCase.*;" + eol
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".domain.*;" + eol
                + "import " + this.useCasePathService.getSharesPackageTitle(useCase.getSoftwareFeature()) + ".utilities.CalendarUtility;" + eol
                + "import org.springframework.stereotype.Service;" + eol
                + eol;
        String serviceContent = ""
                + "@Service" + eol
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
                + t + t + useCase.getDataEntity().getName() + " entity =" + eol
                + t + t + t + t + "this." + StringUtility.firstLowerCase(useCase.getDataEntity().getName()) + "SpringJpaRepository.findById(plant.getPlant().getId())" + eol
                + t + t + t + t + ".orElseThrow(() -> new CultivateException(\"Cannot find by id = \" + plant.getPlant().getId()));"
                + eol
                + t + t + "return new UseCaseFruit<>(" + eol
                + t + t + t + "new Fruit(" + eol
                + t + t + t + t + t + "this.map" + viewDomainEntity.getName() + "(entity, plant.getLocale())" + eol
                + t + t + t + ")," + eol
                + t + t + t + "true," + eol
                + t + t + t + "\"\"" + eol
                + t + t + ");" + eol
                + t + "}" + eol
                + eol
                + t + "public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {" + eol
                + t + t + "return null;" + eol
                + t + "}" + eol
                + eol
                + "\n"
                + t + "public " + viewDomainEntity.getName() + " map" + viewDomainEntity.getName() + "(" + useCase.getDataEntity().getName() + " entity, String locale){" + "\n"
                + t + t + "return new " + viewDomainEntity.getName() + "(" + "\n"
                + this.useCaseService.getDomainEntityMapArguments(viewDomainEntity)
                + t + t + ");" + "\n"
                + t + "}" + "\n"
                + "}";
        return packageTitle
                + eol
                + imports
                + eol
                + serviceContent;
    }


}
