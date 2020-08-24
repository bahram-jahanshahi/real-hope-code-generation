package ir.afarinesh.realhope.modules.generation.features.usecase.application.shares;

import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.project.Software;
import ir.afarinesh.realhope.entities.project.SoftwareApplicationPanel;
import ir.afarinesh.realhope.entities.project.SoftwareFeature;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

@Service
public class UseCasePathService {

    final FileManagementService fileManagementService;

    public UseCasePathService(FileManagementService fileManagementService) {
        this.fileManagementService = fileManagementService;
    }

    // spring boot feature path
    public String getSpringBootFeaturePath(SoftwareFeature softwareFeature) {
        Software software = softwareFeature.getSoftwareModule().getSoftware();
        String sep = fileManagementService.pathSeparator();
        return software.getSpringBootProjectPath() + sep
                + software.getSpringBootJavaSrcPath() + sep
                + software.getSpringBootProjectMainPackage() + sep
                + "modules" + sep
                + StringUtility.convertCamelToUnderLine(softwareFeature.getSoftwareModule().getName()) + sep
                + "features" + sep
                + StringUtility.convertCamelToUnderLine(softwareFeature.getName());
    }

    // spring boot feature package title
    public String getSpringBootFeaturePackageTitle(SoftwareFeature softwareFeature) {
        Software software = softwareFeature.getSoftwareModule().getSoftware();
        String springBootMainPackage = software.getSpringBootProjectMainPackage().replace("\\", ".");
        return springBootMainPackage + "."
                + "modules" + "."
                + StringUtility.convertCamelToUnderLine(softwareFeature.getSoftwareModule().getName()) + "."
                + "features" + "."
                + StringUtility.convertCamelToUnderLine(softwareFeature.getName());
    }

    // spring boot package of core classes
    public String getCorePackageTitle(SoftwareFeature softwareFeature) {
        Software software = softwareFeature.getSoftwareModule().getSoftware();
        String springBootMainPackage = software.getSpringBootProjectMainPackage().replace("\\", ".");
        return springBootMainPackage + "." + "core";
    }

    // spring boot package of share classes
    public String getSharesPackageTitle(SoftwareFeature softwareFeature) {
        Software software = softwareFeature.getSoftwareModule().getSoftware();
        String springBootMainPackage = software.getSpringBootProjectMainPackage().replace("\\", ".");
        return springBootMainPackage + "." + "shares";
    }

    // spring boot package of entities classes
    public String getEntitiesPackageTitle(SoftwareFeature softwareFeature) {
        Software software = softwareFeature.getSoftwareModule().getSoftware();
        String springBootMainPackage = software.getSpringBootProjectMainPackage().replace("\\", ".");
        return springBootMainPackage + "." + "entities";
    }

    // angular cli feature path
    public String getAngularCliFeaturePath(SoftwareFeature softwareFeature, SoftwareApplicationPanel panel) {
        String sep = fileManagementService.pathSeparator();
        return panel.getAngularProjectPath() + sep
                + panel.getAngularSrcPath() + sep
                + "app" + sep
                + "software-modules" + sep
                + StringUtility.convertCamelToDash(softwareFeature.getSoftwareModule().getName()) + sep
                + "software-features" + sep
                + StringUtility.convertCamelToDash(softwareFeature.getName());
    }

    // angular cli assets path
    public String getAngularCliAssetsPath(SoftwareApplicationPanel panel) {
        String sep = fileManagementService.pathSeparator();
        return panel.getAngularProjectPath() + sep
                + panel.getAngularSrcPath() + sep
                + "assets";
    }
}
