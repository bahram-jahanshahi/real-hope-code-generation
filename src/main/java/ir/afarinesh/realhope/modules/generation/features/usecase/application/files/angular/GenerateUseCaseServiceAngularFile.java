package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular;

import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.entities.feature.enums.*;
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
    String t = StringUtility.space(2);
    String eol = "\n";
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
                + "import {Injectable} from '@angular/core';" + eol
                + "import {Observable} from 'rxjs';" + eol
                + "import {HttpClient} from '@angular/common/http';" + eol
                + "import {SecurityService} from '../../../../../core/services/security.service';" + eol
                + "import {environment} from '../../../../../../environments/environment';" + eol
                + "import {UseCaseCommand} from '../../../../../core/domain/use-case-command';" + eol
                + "import {PagedResultFruit} from '../../../../../core/domain/paged-result-fruit';" + eol
                + "import {UseCaseFruit} from '../../../../../core/domain/use-case-fruit';" + eol
                + "import {UseCaseSeedsCommand} from '../../../../../core/domain/use-case-seeds-command';" + eol
                + "import {UseCaseFruitSeeds} from '../../../../../core/domain/use-case-fruit-seeds';" + eol
                + "import {PaginationCommand} from '../../../../../core/domain/pagination-command';" + eol
                + "import {JavaDate} from '../../../../../core/domain/java-date';" + eol
                + "import {SelectEnum} from '../../../../../core/domain/select-enum';" + eol
                + "import {SelectEntity} from '../../../../../core/domain/select-entity';" + eol
                + eol
                + getDomainEntitiesImports(useCase.getSoftwareFeature());
        String serviceContent = ""
                + "@Injectable({" + eol
                + t + "providedIn: 'root'" + eol
                + "})" + eol
                + "export class " + useCaseTitle + "Service {" + eol
                + t + "baseUrl = environment.baseUrl + '" + this.getUrl(useCase) + "';" + eol
                + eol
                + t + "constructor(private httpClient: HttpClient, private securityService: SecurityService) {" + eol
                + t + "}" + eol
                + eol
                + t + "cultivate(plant: UseCaseCommand<" + useCaseTitle + "Plant>):" + eol
                + t + t + "Observable<UseCaseFruit<" + useCaseTitle + "Fruit>> {" + eol
                + t + t + "const url = this.baseUrl + '/cultivate';" + eol
                + t + t + "return this.httpClient" + eol
                + t + t + t + ".post<UseCaseFruit<" + useCaseTitle + "Fruit>>(" + eol
                + t + t + t + t + "url," + eol
                + t + t + t + t + "plant," + eol
                + t + t + t + t + "this.securityService.getSecuredJsonHttpOptions()" + eol
                + t + t + t + ");" + eol
                + t + "}" + eol
                + eol
                + t + "prepare(seedsCommand: UseCaseSeedsCommand<" + useCaseTitle + "SeedsCommand>):" + eol
                + t + t + "Observable<UseCaseFruitSeeds<" + useCaseTitle + "FruitSeeds>> {" + eol
                + t + t + "const url = this.baseUrl + '/prepare';" + eol
                + t + t + "return this.httpClient" + eol
                + t + t + t + ".post<UseCaseFruitSeeds<" + useCaseTitle + "FruitSeeds>>(" + eol
                + t + t + t + t + "url," + eol
                + t + t + t + t + "seedsCommand," + eol
                + t + t + t + t + "this.securityService.getSecuredJsonHttpOptions()" + eol
                + t + t + t + ");" + eol
                + t + "}" + eol
                + "}" + eol
                + eol
                + "export class " + useCaseTitle + "Fruit {" + eol
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.Fruit)
                + "}" + eol
                + eol
                + "export class " + useCaseTitle + "Plant {" + eol
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.Plant)
                + "}" + eol
                + eol
                + "export class " + useCaseTitle + "FruitSeeds {" + eol
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.FruitSeeds)
                + "}" + eol
                + eol
                + "export class " + useCaseTitle + "SeedsCommand {" + eol
                + this.getDataAttributes(useCase, UseCaseDataTypeEnum.SeedsCommand)
                + "}" + eol;
        return imports
                + eol
                + serviceContent;
    }

    private String getDomainEntitiesImports(SoftwareFeature softwareFeature) {
        List<DomainEntity> domainEntities = this.domainEntitySpringJpaRepository.findAllBySoftwareFeature_Id(softwareFeature.getId());
        String content = "";
        for (DomainEntity domainEntity : domainEntities) {
            content += "import {" + domainEntity.getName() + "}"
                    + " from "
                    + "'../domain/" + StringUtility.convertCamelToDash(domainEntity.getName()) + "';" + eol;
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
                        content += t + attributeName + ": " + attributeType + ";" + eol;
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.DomainEntity)) {
                        attributeType = attribute.getDomainEntityAttributeType().getName();
                        content += t + attributeName + ": " + attributeType + ";" + eol;
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEnum)) {
                        content += t + attributeName + "Enum: SelectEnum;" + eol;
                        if (useCaseDataType.equals(UseCaseDataTypeEnum.FruitSeeds)) {
                            content += t + attributeName + "EnumArray: Array<SelectEnum>;" + eol;
                        }
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEntity)) {
                        content += t + attributeName + ": SelectEntity;" + eol;
                        if (useCaseDataType.equals(UseCaseDataTypeEnum.FruitSeeds)) {
                            content += t + attributeName + "Array: Array<SelectEnum>;" + eol;
                        }
                    }
                }
                // List
                if (attribute.getAttributeQuantity().equals(EntityAttributeQuantityEnum.List)) {
                    String attributeType = "";
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.Primitive)) {
                        attributeType = attribute.getPrimitiveAttributeType().angular();
                        content += t + attributeName + ": " + "Array<" + attributeType + ">" + ";" + eol;
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.DomainEntity)) {
                        attributeType = attribute.getDomainEntityAttributeType().getName();
                        content += t + attributeName + ": " + "Array<" + attributeType + ">" + ";" + eol;
                    }
                }
            }
            if (useCaseDataType.equals(UseCaseDataTypeEnum.Plant) || useCaseDataType.equals(UseCaseDataTypeEnum.SeedsCommand)) {
                if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
                    content += t + "paginationCommand: PaginationCommand;" + eol;
                }
                content += eol;
                content += t + "constructor(";
                for (int i = 0; i < useCaseDataAttributes.size(); i++) {
                    UseCaseDataAttribute attribute = useCaseDataAttributes.get(i);
                    String attributeType = attribute.getPrimitiveAttributeType().angular();
                    String attributeName = StringUtility.firstLowerCase(attribute.getName());
                    content += attributeName + ": " + attributeType;
                    content += (i < useCaseDataAttributes.size() - 1) ? ", " : "";
                }
                if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
                    if (useCaseDataAttributes.size() > 0) {
                        content += ", ";
                    }
                    content += "paginationCommand: PaginationCommand) {" + eol;
                } else {
                    content += t + ") {" + eol;
                }
                for (UseCaseDataAttribute attribute : useCaseDataAttributes) {
                    String attributeName = StringUtility.firstLowerCase(attribute.getName());
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.Primitive)) {
                        content += t + t + "this." + attributeName + " = " + attributeName + ";" + eol;
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEnum)) {
                        content += t + t + "this." + attributeName + "Enum = " + attributeName + ";" + eol;
                    }
                    if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEntity)) {
                        content += t + t + "this." + attributeName + " = " + attributeName + ";" + eol;
                    }
                }
                if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
                    content += t + t + "this.paginationCommand = paginationCommand;" + eol;
                }
                content += t + "}" + eol;
            }
            if (useCaseDataType.equals(UseCaseDataTypeEnum.Fruit)
                    && useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
                content += t + "pagedResultFruit: PagedResultFruit;" + eol;
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
