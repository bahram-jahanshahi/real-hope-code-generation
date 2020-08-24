package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java;

import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.DomainEntityAttribute;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeQuantityEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.exceptions.GenerateFeatureDomainJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateFeatureDomainJavaFile {

    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;

    public GenerateFeatureDomainJavaFile(FileManagementService fileManagementService, UseCasePathService useCasePathService) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
    }

    public void generate(DomainEntity domainEntity) throws GenerateFeatureDomainJavaFileException {
        try {
            fileManagementService
                    .createFile(
                            this.getPath(domainEntity),
                            this.getFileName(domainEntity),
                            this.getContent(domainEntity)
                    );
        } catch (CreateFileException e) {
            throw new GenerateFeatureDomainJavaFileException(e.getMessage());
        }
    }

    protected String getPath(DomainEntity domainEntity) {
        String pathSeparator = fileManagementService.pathSeparator();
        return useCasePathService.getSpringBootFeaturePath(domainEntity.getSoftwareFeature()) + pathSeparator
                + "domain";
    }

    protected String getFileName(DomainEntity domainEntity) {
        return domainEntity.getName() + ".java";
    }

    protected String getContent(DomainEntity domainEntity) {
        String packageTitle = "package "
                + this.useCasePathService.getSpringBootFeaturePackageTitle(domainEntity.getSoftwareFeature()) + "."
                + "domain;"
                + "\n";
        String imports = ""
                + "import " + this.useCasePathService.getCorePackageTitle(domainEntity.getSoftwareFeature()) + ".annotations.FeatureDomain;" + "\n"
                + "import lombok.*;" + "\n";
        String domainContent = ""
                + "@AllArgsConstructor" + "\n"
                + "@NoArgsConstructor" + "\n"
                + "@Getter" + "\n"
                + "@Setter" + "\n"
                + "@ToString" + "\n"
                + "\n"
                + "@FeatureDomain" + "\n"
                + "public class " + domainEntity.getName() + "{" + "\n"
                + this.getDataAttributes(domainEntity)
                + "}";
        return packageTitle
                + "\n"
                + imports
                + "\n"
                + domainContent;
    }

    private String getDataAttributes(DomainEntity domainEntity) {
        String content = "";
        List<DomainEntityAttribute> attributes = domainEntity.getDomainEntityAttributes();
        for (DomainEntityAttribute attribute: attributes) {
            String attributeName = StringUtility.firstLowerCase(attribute.getName());
            // Mono
            if (attribute.getAttributeQuantity().equals(EntityAttributeQuantityEnum.Mono)) {
                content += "\t" + "private " + attribute.getPrimitiveAttributeType().name() + " " + attributeName + ";" + " // " + attribute.getDescription() + "\n";
            }
            // List
            if (attribute.getAttributeQuantity().equals(EntityAttributeQuantityEnum.List)) {
                content += "\t" + "private " + "List<" + attribute.getPrimitiveAttributeType().name() + ">" + " " + attributeName + ";" + " // " + attribute.getDescription() + "\n";
            }
        }
        return content;
    }
}
