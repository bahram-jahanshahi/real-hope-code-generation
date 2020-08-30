package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular;

import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.DomainEntityAttribute;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeQuantityEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.exceptions.GenerateFeatureDomainAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.exceptions.GenerateFeatureDomainJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateFeatureDomainAngularFile {
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;

    public GenerateFeatureDomainAngularFile(FileManagementService fileManagementService,
                                            UseCasePathService useCasePathService) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
    }

    public void generate(DomainEntity domainEntity) throws GenerateFeatureDomainAngularFileException {
        try {
            fileManagementService
                    .createFile(
                            this.getPath(domainEntity),
                            this.getFileName(domainEntity),
                            this.getContent(domainEntity),
                            true
                    );
        } catch (CreateFileException e) {
            throw new GenerateFeatureDomainAngularFileException(e.getMessage());
        }
    }

    protected String getPath(DomainEntity domainEntity) {
        String pathSeparator = fileManagementService.pathSeparator();
        return useCasePathService.getAngularCliFeaturePath(domainEntity.getSoftwareFeature(), domainEntity.getSoftwareApplicationPanel()) + pathSeparator
                + "domain";
    }

    protected String getFileName(DomainEntity domainEntity) {
        return StringUtility.convertCamelToDash(domainEntity.getName()) + ".ts";
    }

    protected String getContent(DomainEntity domainEntity) {
        return ""
                + "export class " + domainEntity.getName() + " {" + "\n"
                + this.getDataAttributes(domainEntity)
                + "}" + "\n";
    }

    private String getDataAttributes(DomainEntity domainEntity) {
        String content = "";
        List<DomainEntityAttribute> attributes = domainEntity.getDomainEntityAttributes();
        for (DomainEntityAttribute attribute: attributes) {
            String attributeName = StringUtility.firstLowerCase(attribute.getName());
            // Mono
            if (attribute.getAttributeQuantity().equals(EntityAttributeQuantityEnum.Mono)) {
                content += "  " + attributeName + ": " + attribute.getPrimitiveAttributeType().angular() + ";" + " // " + attribute.getDescription() + "\n";
            }
            // List
            if (attribute.getAttributeQuantity().equals(EntityAttributeQuantityEnum.List)) {
                content += "  " + attributeName + ": " + "Array<" + attribute.getPrimitiveAttributeType().angular() + ">" + ";" + " // " + attribute.getDescription() + "\n";
            }
        }
        return content;
    }
}
