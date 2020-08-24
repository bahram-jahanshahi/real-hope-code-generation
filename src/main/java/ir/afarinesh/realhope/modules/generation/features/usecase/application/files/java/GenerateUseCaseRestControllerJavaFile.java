package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java;

import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.exceptions.GenerateUseCaseRestControllerJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

@Service
public class GenerateUseCaseRestControllerJavaFile {
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;

    public GenerateUseCaseRestControllerJavaFile(FileManagementService fileManagementService, UseCasePathService useCasePathService) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
    }

    public void generate(UseCase useCase) throws GenerateUseCaseRestControllerJavaFileException {
        try {
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getFileName(useCase),
                            this.getContent(useCase)
                    );
        } catch (CreateFileException e) {
            throw new GenerateUseCaseRestControllerJavaFileException(e.getMessage());
        }
    }

    public String getPath(UseCase useCase) {
        String pathSeparator = fileManagementService.pathSeparator();
        return useCasePathService.getSpringBootFeaturePath(useCase.getSoftwareFeature()) + pathSeparator
                + "adapter" + pathSeparator
                + "web";
    }

    public String getFileName(UseCase useCase) {
        return useCase.getName() + "By" + useCase.getSoftwareRole().getName() + "RestController.java";
    }

    public String getContent(UseCase useCase) {
        String packageTitle = "package "
                + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + "."
                + "adapter" + "."
                + "web;"
                + "\n";
        String imports = ""
                + "import " + this.useCasePathService.getCorePackageTitle(useCase.getSoftwareFeature()) + ".usecase.*;" + "\n"
                + "import org.springframework.web.bind.annotation.*;" + "\n"
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".application.ports.in." + useCase.getName() + "By" + useCase.getSoftwareRole().getName() + "UseCase;" + "\n"
                + "import " + this.useCasePathService.getSpringBootFeaturePackageTitle(useCase.getSoftwareFeature()) + ".application.ports.in." + useCase.getName() + "By" + useCase.getSoftwareRole().getName() + "UseCase.*;" + "\n";
        String applicationPanelDashedName = StringUtility.convertCamelToDash(useCase.getSoftwareApplicationPanel().getName());
        String softwareRoleDashedName = StringUtility.convertCamelToDash(useCase.getSoftwareRole().getName());
        String featureDashedName = StringUtility.convertCamelToDash(useCase.getSoftwareFeature().getName());
        String useCaseDashedName = StringUtility.convertCamelToDash(useCase.getName());
        String restControllerContent = ""
                + "@RestController" + "\n"
                + "@RequestMapping(\"" + this.getUrl(useCase) + "\")" + "\n"
                + "public class " + useCase.getName() + "By" + useCase.getSoftwareRole().getName() + "RestController{" + "\n"
                + "\n"
                + "\t" + "final " + useCase.getName() + "By" + useCase.getSoftwareRole().getName() + "UseCase" + " " + "useCase;" + " \n"
                + "\n"
                + "\t" + "public " + useCase.getName() + "By" + useCase.getSoftwareRole().getName() + "RestController(" + useCase.getName() + "By" + useCase.getSoftwareRole().getName() + "UseCase" + " " + "useCase) {" + "\n"
                + "\t" + "\t" + "this.useCase = useCase;" + "\n"
                + "\t" + "}" + "\n"
                + "\n"
                + "\t" + "@PostMapping(\"/cultivate\")" + "\n"
                + "\t" + "public UseCaseFruit<Fruit> cultivate(@RequestBody UseCasePlant<Plant> plant) {" + "\n"
                + "\t" + "\t" + "try {" + "\n"
                + "\t" + "\t" + "\t" + "return useCase.cultivate(plant);" + "\n"
                + "\t" + "\t" + "} catch (CultivateException e) {" + "\n"
                + "\t" + "\t" + "\t" + "return new UseCaseFruit<>(null, false, e.getMessage());" + "\n"
                + "\t" + "\t" + "}" + "\n"
                + "\t" + "}" + "\n"
                + "\n"
                + "\t" + "@PostMapping(\"/prepare\")" + "\n"
                + "\t" + "public UseCaseFruitSeeds<FruitSeeds> prepare(@RequestBody UseCaseSeedsCommand<SeedsCommands> seedsCommand) {" + "\n"
                + "\t" + "\t" + "try {" + "\n"
                + "\t" + "\t" + "\t" + "return useCase.prepare(seedsCommand);" + "\n"
                + "\t" + "\t" + "} catch (PrepareException e) {" + "\n"
                + "\t" + "\t" + "\t" + "return new UseCaseFruitSeeds<>(null, false, e.getMessage());" + "\n"
                + "\t" + "\t" + "}" + "\n"
                + "\t" + "}" + "\n"
                + "}";
        return packageTitle
                + "\n"
                + imports
                + "\n"
                + restControllerContent;
    }

    protected String getUrl(UseCase useCase) {
        String applicationPanelDashedName = StringUtility.convertCamelToDash(useCase.getSoftwareApplicationPanel().getName());
        String softwareRoleDashedName = StringUtility.convertCamelToDash(useCase.getSoftwareRole().getName());
        String featureDashedName = StringUtility.convertCamelToDash(useCase.getSoftwareFeature().getName());
        String useCaseDashedName = StringUtility.convertCamelToDash(useCase.getName() + "By" + useCase.getSoftwareRole().getName());
        return "/app"
                + "/" + applicationPanelDashedName
                + "/" + softwareRoleDashedName
                + "/" + featureDashedName
                + "/" + useCaseDashedName;
    }
}
