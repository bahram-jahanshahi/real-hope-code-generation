package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service;

import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.DomainEntityAttribute;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.enums.PrimitiveAttributeTypeEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.exceptions.GenerateListUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetGridListFruitDomainEntityException;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

@Service
public class GenerateListUseCaseServiceJavaFile {

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
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getFileName(useCase),
                            this.getContent(useCase)
                    );
        } catch (CreateFileException | GetGridListFruitDomainEntityException e) {
            throw new GenerateListUseCaseServiceJavaFileException(e.getMessage());
        }
    }

    protected String getPath(UseCase useCase) {
        String pathSeparator = fileManagementService.pathSeparator();
        return useCasePathService.getSpringBootFeaturePath(useCase.getSoftwareFeature()) + pathSeparator
                + "application";
    }

    protected String getFileName(UseCase useCase) {
        return useCase.getName() + "By" + useCase.getSoftwareRole().getName() + "Service.java";
    }

    protected String getContent(UseCase useCase) throws GetGridListFruitDomainEntityException {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String entitySpringJpaRepository = useCase.getDataEntity().getName() + "SpringJpaRepository";
        DomainEntity gridListFruitDomainEntity = useCaseService.getGridListFruitDomainEntity(useCase);
        String t = StringUtility.space(4);
        String packageTitle = "package "
                + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + "."
                + "application;"
                + "\n";
        String imports = ""
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".annotations.FeatureApplication;" + "\n"
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".usecase.*;" + "\n"
                + "import " + this.useCasePathService.getSharesPackageTitle(useCase.getSoftwareFeature()) + ".repositories." + useCase.getDataEntity().getName() + "SpringJpaRepository;" + "\n"
                + "import " + this.useCasePathService.getEntitiesPackageTitle(useCase.getSoftwareFeature()) + "." + useCase.getDataEntity().getCategory() + "." + useCase.getDataEntity().getName() + ";" + "\n"
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".application.ports.in." + useCaseTitle + "UseCase;" + "\n"
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".domain.*;" + "\n"
                + "import ir.amnpardaz.enterprise.avshop.shares.utilities.CalendarUtility;" + "\n"
                + "import org.springframework.data.domain.PageRequest;" + "\n"
                + "import org.springframework.stereotype.Service;" + "\n"
                + "import org.springframework.data.domain.Page;" + "\n"
                + "\n"
                + "import java.util.stream.Collectors;" + "\n"
                + "import java.util.List;" + "\n"
                + "\n";
        String serviceContent = ""
                + "@Service" + "\n"
                + "@FeatureApplication" + "\n"
                + "public class " + useCaseTitle + "Service implements " + useCaseTitle + "UseCase {" + "\n"
                + "\n"
                + t + "// jpa repositories" + "\n"
                + t + "final " + entitySpringJpaRepository + " " + StringUtility.firstLowerCase(entitySpringJpaRepository) + ";" + "\n"
                + "\n"
                + t + "public " + useCaseTitle + "Service(" + entitySpringJpaRepository + " " + StringUtility.firstLowerCase(entitySpringJpaRepository) + ") {" + "\n"
                + t + t + "this." + StringUtility.firstLowerCase(entitySpringJpaRepository) + " = " + StringUtility.firstLowerCase(entitySpringJpaRepository) + ";" + "\n"
                + t + "}" + "\n"
                + "\n"
                + t + "@Override" + "\n"
                + t + "public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {" + "\n"
                + t + t + "Page<" + useCase.getDataEntity().getName() + "> page = " + "this." + StringUtility.firstLowerCase(entitySpringJpaRepository) + "\n"
                + t + t + t + t + ".findAll(PageRequest.of(plant.getPlant().getPaginationCommand().getPageIndex(), plant.getPlant().getPaginationCommand().getPageSize()));" + "\n"
                + t + t + "List<" + gridListFruitDomainEntity.getName() + "> list = page" + "\n"
                + t + t + t + t + ".get()" + "\n"
                + t + t + t + t + ".map(entity -> this.map" + gridListFruitDomainEntity.getName() + "(entity, plant.getLocale()))" + "\n"
                + t + t + t + t + ".collect(Collectors.toList());" + "\n"
                + t + t + "return new UseCaseFruit<>(" + "\n"
                + t + t + t + t + "new Fruit(" + "\n"
                + t + t + t + t + t + t + "list," + "\n"
                + t + t + t + t + t + t + "new PagedResultFruit(page.getTotalElements(), page.getTotalPages())" + "\n"
                + t + t + t + t + ")," + "\n"
                + t + t + t + t + "true," + "\n"
                + t + t + t + t + "\"\"" + "\n"
                + t + t + ");" + "\n"
                + t + "}" + "\n"
                + "\n"
                + t + "@Override" + "\n"
                + t + "public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {" + "\n"
                + t + t + "return null;" + "\n"
                + t + "}" + "\n"
                + "\n"
                + t + "public " + useCaseService.getGridListFruitDomainEntity(useCase).getName() + " map" + useCaseService.getGridListFruitDomainEntity(useCase).getName() + "(" + useCase.getDataEntity().getName() + " entity, String locale){" + "\n"
                + t + t + "return new " + useCaseService.getGridListFruitDomainEntity(useCase).getName() + "(" + "\n"
                + this.useCaseService.getDomainEntityMapArguments(gridListFruitDomainEntity)
                + t + t + ");" + "\n"
                + t + "}" + "\n"
                + "}";
        return packageTitle
                + "\n"
                + imports
                + "\n"
                + serviceContent;
    }


}
