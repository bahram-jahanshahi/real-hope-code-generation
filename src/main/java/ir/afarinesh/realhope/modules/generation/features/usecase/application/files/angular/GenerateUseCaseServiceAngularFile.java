package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular;

import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeCategoryEnum;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeQuantityEnum;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseDataTypeEnum;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
import ir.afarinesh.realhope.entities.project.SoftwareFeature;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.exceptions.GenerateUseCaseServiceAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenerateUseCaseServiceAngularFile {
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;

    public GenerateUseCaseServiceAngularFile(FileManagementService fileManagementService,
                                             UseCasePathService useCasePathService,
                                             DomainEntitySpringJpaRepository domainEntitySpringJpaRepository) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
    }

    public void generate(UseCase useCase) throws GenerateUseCaseServiceAngularFileException {
        try {
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getFileName(useCase),
                            this.getContent(useCase)
                    );
        } catch (CreateFileException e) {
            throw new GenerateUseCaseServiceAngularFileException(e.getMessage());
        }
    }

    public String getPath(UseCase useCase) {
        String pathSeparator = fileManagementService.pathSeparator();
        return useCasePathService.getAngularCliFeaturePath(useCase.getSoftwareFeature(), useCase.getSoftwareApplicationPanel()) + pathSeparator
                + "services";
    }

    public String getFileName(UseCase useCase) {
        return StringUtility.convertCamelToDash(useCase.getName() + "By" + useCase.getSoftwareRole().getName()) + ".service.ts";
    }

    public String getContent(UseCase useCase) {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String imports = ""
                + "import {Injectable} from '@angular/core';" + "\n"
                + "import {Observable} from 'rxjs';" + "\n"
                + "import {HttpClient} from '@angular/common/http';" + "\n"
                + "import {SecurityService} from '../../../../../core/services/security.service';" + "\n"
                + "import {environment} from '../../../../../../environments/environment';" + "\n"
                + "import {UseCaseCommand} from '../../../../../core/domain/use-case-command';" + "\n"
                + "import {PagedResultFruit} from '../../../../../core/domain/paged-result-fruit';" + "\n"
                + "import {UseCaseFruit} from '../../../../../core/domain/use-case-fruit';" + "\n"
                + "import {UseCaseSeedsCommand} from '../../../../../core/domain/use-case-seeds-command';" + "\n"
                + "import {UseCaseFruitSeeds} from '../../../../../core/domain/use-case-fruit-seeds';" + "\n"
                + "import {PaginationCommand} from '../../../../../core/domain/pagination-command';" + "\n"
                + "\n"
                + getDomainEntitiesImports(useCase.getSoftwareFeature());
        String serviceContent = ""
                + "@Injectable({" + "\n"
                + space(2) + "providedIn: 'root'" + "\n"
                + "})" + "\n"
                + "export class " + useCaseTitle + "Service {" + "\n"
                + space(2) + "baseUrl = environment.baseUrl + '" + this.getUrl(useCase) + "';" + "\n"
                + "\n"
                + space(2) + "constructor(private httpClient: HttpClient, private securityService: SecurityService) {" + "\n"
                + space(2) + "}" + "\n"
                + "\n"
                + space(2) + "cultivate(plant: UseCaseCommand<" + useCaseTitle + "Plant>):" + "\n"
                + space(4) + "Observable<UseCaseFruit<" + useCaseTitle + "Fruit>> {" + "\n"
                + space(4) + "const url = this.baseUrl + '/cultivate';" + "\n"
                + space(4) + "return this.httpClient" + "\n"
                + space(6) + ".post<UseCaseFruit<" + useCaseTitle + "Fruit>>(" + "\n"
                + space(8) + "url," + "\n"
                + space(8) + "plant," + "\n"
                + space(8) + "this.securityService.getSecuredJsonHttpOptions()" + "\n"
                + space(6) + ");" + "\n"
                + space(2) + "}" + "\n"
                + "\n"
                + space(2) + "prepare(seedsCommand: UseCaseSeedsCommand<" + useCaseTitle + "SeedsCommand>):" + "\n"
                + space(4) + "Observable<UseCaseFruitSeeds<" + useCaseTitle + "FruitSeeds>> {" + "\n"
                + space(4) + "const url = this.baseUrl + '/prepare';" + "\n"
                + space(4) + "return this.httpClient" + "\n"
                + space(6) + ".post<UseCaseFruitSeeds<" + useCaseTitle + "FruitSeeds>>(" + "\n"
                + space(8) + "url," + "\n"
                + space(8) + "seedsCommand," + "\n"
                + space(8) + "this.securityService.getSecuredJsonHttpOptions()" + "\n"
                + space(6) + ");" + "\n"
                + space(2) + "}" + "\n"
                + "}" + "\n"
                + "\n"
                + "export class " + useCaseTitle + "Fruit {" + "\n"
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.Fruit)
                + "}" + "\n"
                + "\n"
                + "export class " + useCaseTitle + "Plant {" + "\n"
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.Plant)
                + "}" + "\n"
                + "\n"
                + "export class " + useCaseTitle + "FruitSeeds {" + "\n"
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.FruitSeeds)
                + "}" + "\n"
                + "\n"
                + "export class " + useCaseTitle + "SeedsCommand {" + "\n"
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.SeedsCommand)
                + "}" + "\n";
        return imports
                + "\n"
                + serviceContent;
    }

    private String getDomainEntitiesImports(SoftwareFeature softwareFeature) {
        List<DomainEntity> domainEntities = this.domainEntitySpringJpaRepository.findAllBySoftwareFeature_Id(softwareFeature.getId());
        String content = "";
        for (DomainEntity domainEntity : domainEntities) {
            content += "import {" + domainEntity.getName() + "}"
                    + " from "
                    + "'../domain/" + StringUtility.convertCamelToDash(domainEntity.getName()) + "';" + "\n";
        }
        return content;
    }

    private String getDataAttributes(UseCase useCase, UseCaseDataTypeEnum useCaseDataType) {
        Optional<UseCaseData> useCaseDataOptional = useCase
                .getUseCaseDataSet()
                .stream()
                .filter(useCaseData -> useCaseData.getUseCaseDataType().equals(useCaseDataType))
                .findFirst();
        if (useCaseDataOptional.isPresent()) {
            List<UseCaseDataAttribute> useCaseDataAttributes = useCaseDataOptional.get().getUseCaseDataAttributes();
            String content = "";
            for (UseCaseDataAttribute attribute : useCaseDataAttributes) {
                String attributeName = StringUtility.firstLowerCase(attribute.getName());
                // Mono
                if (attribute.getAttributeQuantity().equals(EntityAttributeQuantityEnum.Mono)) {
                    String attributeType = "";
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.Primitive)) {
                        attributeType = attribute.getPrimitiveAttributeType().angular();
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.DomainEntity)) {
                        attributeType = attribute.getDomainEntityAttributeType().getName();
                    }
                    content += space(2) + attributeName + ": " + attributeType + ";" + "\n";
                }
                // List
                if (attribute.getAttributeQuantity().equals(EntityAttributeQuantityEnum.List)) {
                    String attributeType = "";
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.Primitive)) {
                        attributeType = attribute.getPrimitiveAttributeType().angular();
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.DomainEntity)) {
                        attributeType = attribute.getDomainEntityAttributeType().getName();
                    }
                    content += space(2) + attributeName + ": " + "Array<" + attributeType + ">" + ";" + "\n";
                }
            }
            if (useCaseDataType.equals(UseCaseDataTypeEnum.Plant)) {
                if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
                    content += StringUtility.space(2) + "paginationCommand: PaginationCommand;" + "\n";
                }
                content += "\n";
                content += StringUtility.space(2) + "constructor(";
                for (int i = 0; i < useCaseDataAttributes.size(); i++) {
                    UseCaseDataAttribute attribute = useCaseDataAttributes.get(i);
                    String attributeType = attribute.getPrimitiveAttributeType().angular();
                    String attributeName = StringUtility.firstLowerCase(attribute.getName());
                    content += attributeName + ": " + attributeType;
                    content += (i < useCaseDataAttributes.size() - 1) ? ", " : "";
                }
                if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
                    content += ", paginationCommand: PaginationCommand) {" + "\n";
                } else {
                    content += StringUtility.space(2) + ") {" + "\n";
                }
                for (UseCaseDataAttribute attribute : useCaseDataAttributes) {
                    String attributeType = attribute.getPrimitiveAttributeType().angular();
                    String attributeName = StringUtility.firstLowerCase(attribute.getName());
                    content += StringUtility.space(4) + "this." + attributeName + " = " + attributeName +";" + "\n";
                }
                if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
                    content += StringUtility.space(4) + "this.paginationCommand = paginationCommand;" + "\n";
                }
                content += StringUtility.space(2) + "}" + "\n";
            }
            if (useCaseDataType.equals(UseCaseDataTypeEnum.Fruit)
                    && useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
                content += StringUtility.space(2) + "pagedResultFruit: PagedResultFruit;" + "\n";
            }
            return content;
        }

        return "";
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

    protected String space(int count) {
        return " ".repeat(Math.max(0, count));
    }
}
