package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.view;

import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.DomainEntityAttribute;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.UseCaseRelation;
import ir.afarinesh.realhope.entities.feature.enums.FrontendActionTypeEnum;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
import ir.afarinesh.realhope.entities.project.SoftwareFeature;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.view.exceptions.GenerateUseCaseViewComponentAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetViewDomainEntityException;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.UseCaseRelationSpringJpaRepository;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateUseCaseViewComponentAngularFile {
    String t = StringUtility.space(2);
    String eol = "\n";
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final UseCaseService useCaseService;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;
    final UseCaseRelationSpringJpaRepository useCaseRelationSpringJpaRepository;

    public GenerateUseCaseViewComponentAngularFile(FileManagementService fileManagementService,
                                                   UseCasePathService useCasePathService,
                                                   UseCaseService useCaseService,
                                                   DomainEntitySpringJpaRepository domainEntitySpringJpaRepository,
                                                   UseCaseRelationSpringJpaRepository useCaseRelationSpringJpaRepository) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.useCaseService = useCaseService;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
        this.useCaseRelationSpringJpaRepository = useCaseRelationSpringJpaRepository;
    }

    public void generate(UseCase useCase) throws GenerateUseCaseViewComponentAngularFileException {
        try {
            // create component
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getComponentFileName(useCase),
                            this.getComponentContent(useCase)
                    );
            // create html
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getHtmlFileName(useCase),
                            this.getHtmlContent(useCase)
                    );
            // create css
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getCssFileName(useCase),
                            this.getCssContent(useCase)
                    );
        } catch (CreateFileException | GetViewDomainEntityException e) {
            throw new GenerateUseCaseViewComponentAngularFileException(e.getMessage());
        }
    }

    protected String getPath(UseCase useCase) {
        String pathSeparator = fileManagementService.pathSeparator();
        return useCasePathService.getAngularCliFeaturePath(useCase.getSoftwareFeature(), useCase.getSoftwareApplicationPanel()) + pathSeparator
                + "components" + fileManagementService.pathSeparator()
                + StringUtility.convertCamelToDash(useCase.getName() + "By" + useCase.getSoftwareRole().getName());
    }

    protected String getComponentFileName(UseCase useCase) {
        return StringUtility.convertCamelToDash(useCase.getName() + "By" + useCase.getSoftwareRole().getName()) + ".component.ts";
    }

    protected String getHtmlFileName(UseCase useCase) {
        return StringUtility.convertCamelToDash(useCase.getName() + "By" + useCase.getSoftwareRole().getName()) + ".component.html";
    }

    protected String getCssFileName(UseCase useCase) {
        return StringUtility.convertCamelToDash(useCase.getName() + "By" + useCase.getSoftwareRole().getName()) + ".component.css";
    }

    // component content
    protected String getComponentContent(UseCase useCase) throws GetViewDomainEntityException {
        String useCaseTitle = useCaseService.getUseCaseTitle(useCase);
        DomainEntity viewDomainEntity = this.useCaseService.getViewDomainEntity(useCase);
        // imports
        String imports = ""
                + "import {Component, OnInit, Inject, ViewChild} from '@angular/core';" + eol
                + "import {NavigationService} from '../../../../../../core/services/navigation.service';" + eol
                + "import {UtilityDialogService} from '../../../../../../shares/utilities/utility-dialog.service';" + eol
                + "import {LocaleService} from '../../../../../../core/services/locale.service';" + eol
                + "import {ResponsiveService} from '../../../../../../core/services/responsive.service';" + eol
                + "import {ErrorDialogData} from '../../../../../../shares/components/dialogs/error-dialog/error-dialog.component';" + eol
                + "import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';" + eol
                + "import {" + eol
                + "  " + useCaseTitle + "Plant" + "," + eol
                + "  " + useCaseTitle + "Service" + eol
                + "} from '../../services/" + StringUtility.convertCamelToDash(useCaseTitle) + ".service';" + eol
                + "import {UseCaseCommand} from '../../../../../../core/domain/use-case-command';" + eol
                + this.getDomainEntitiesImports(useCase.getSoftwareFeature())
                + this.getPopupImports(useCase);
        // component
        String componentContent = ""
                + "@Component({" + eol
                + "  selector: 'app-" + StringUtility.convertCamelToDash(useCaseTitle) + "'," + eol
                + "  templateUrl: './" + StringUtility.convertCamelToDash(useCaseTitle) + ".component.html'," + eol
                + "  styleUrls: ['./" + StringUtility.convertCamelToDash(useCaseTitle) + ".component.css']" + eol
                + "})" + eol
                + "export class " + useCaseTitle + "Component implements OnInit {" + eol
                + eol
                + t + "entityId: number;" + eol
                + t + "entity: " + viewDomainEntity.getName() + ";" + eol
                + t + "readyToView = false;" + eol
                + eol
                + "  constructor(private useCase: " + useCaseTitle + "Service,\n"
                + "              private dialogService: UtilityDialogService,\n"
                + "              private localeService: LocaleService,\n"
                + "              public dialogRef: MatDialogRef<" + useCaseTitle + "Component>,\n"
                + "              @Inject(MAT_DIALOG_DATA) public data: number) {\n"
                + "    this.entityId = data;\n"
                + "  }"
                + eol
                + "  ngOnInit(): void {\n"
                + "    this.cultivate();\n"
                + "  }\n"
                + eol
                + "  close(): void {\n"
                + "    this.dialogRef.close();\n"
                + "  }"
                + eol
                + t + "private cultivate(): void {" + eol
                + t + t + "this.useCase" + eol
                + t + t + t + ".cultivate(" + eol
                + t + t + t + t + "new UseCaseCommand<" + useCaseTitle + "Plant>(" + eol
                + t + t + t + t + t + "new " + useCaseTitle + "Plant(" + eol
                + t + t + t + t + t + t + "this.entityId" + eol
                + t + t + t + t + t + ")," + eol
                + t + t + t + t + t + "this.localeService.getLocale().getValue()" + eol
                + t + t + t + t + ")" + eol
                + t + t + t + ")" + eol
                + t + t + t + ".subscribe(fruit => {" + eol
                + t + t + t + t + "if (fruit.isSuccessful) {" + eol
                + t + t + t + t + t + "this.entity = fruit.result.entity;" + eol
                + t + t + t + t + t + "this.readyToView = true;" + eol
                + t + t + t + t + "} else {" + eol
                + t + t + t + t + t + "this.dialogService.showErrorDialog( new ErrorDialogData('', Array.of(fruit.message)));" + eol
                + t + t + t + t + "}" + eol
                + t + t + t + "}, error => {" + eol
                + t + t + t + t + "this.dialogService.showQuickServerErrorDialog('Error: ' + error.message);" + eol
                + t + t + t + "});" + eol
                + t + "}" + eol
                + eol
                + getPopupMethods(useCase)
                + "}" + eol
                + eol;
        return imports + eol + componentContent;
    }

    // html content
    protected String getHtmlContent(UseCase useCase) throws GetViewDomainEntityException {
        String useCaseTitle = useCaseService.getUseCaseTitle(useCase);
        DomainEntity viewDomainEntity = this.useCaseService.getViewDomainEntity(useCase);
        String htmlContent = ""
                + "<div mat-dialog-title>" + eol
                + t + "{{ '" + useCaseTitle + ".FormTitle' | translate }}" + eol
                + "</div>" + eol
                + "<mat-dialog-content>" + eol
                + t + "<div *ngIf='!readyToView' style='height: 240px; display: flex; align-items: center; justify-content: center;'>" + eol
                + t + t + "<mat-spinner diameter='128' strokeWidth='1'></mat-spinner>" + eol
                + t + "</div>" + eol
                + t + "<mat-tab-group *ngIf='readyToView'>" + eol
                + t + t + "<mat-tab label=\"{{'" + useCaseTitle + ".Profile' | translate}}\">" + eol
                + t + t + t + "<table class='baf-table' *ngIf='readyToView'>" + eol
                + this.getKeyValueTableRow(useCase, viewDomainEntity, t + t + t + t)
                + t + t + t + "</table>" + eol
                + t + t + "</mat-tab>" + eol
                + t + "</mat-tab-group>" + eol
                + "</mat-dialog-content>" + eol
                + "<mat-dialog-actions>" + eol
                + t + "<button mat-flat-button (click)='close()'>" + eol
                + t + t + "{{ '" + useCaseTitle + ".Cancel' | translate }}"
                + t + "</button>" + eol
                + getPopupButtons(useCase, t)
                + "</mat-dialog-actions>" + eol;

        return htmlContent;
    }

    // css content
    protected String getCssContent(UseCase useCase) {
        return "";
    }

    private String getKeyValueTableRow(UseCase useCase, DomainEntity viewDomainEntity, String offset) {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String content = "";
        String eol = "\n";
        String t = StringUtility.space(2);
        for (DomainEntityAttribute attribute : viewDomainEntity.getDomainEntityAttributes()) {
            content += ""
                    + offset + "<tr>" + eol
                    + offset + t + "<td class='baf-table-key'>" + eol
                    + offset + t + t + "{{ '" + useCaseTitle + "." + attribute.getName() + "' | translate }}" + eol
                    + offset + t + "</td>" + eol
                    + offset + t + "<td class='baf-table-value'>" + eol
                    + offset + t + t + "{{ entity." + StringUtility.firstLowerCase(attribute.getName()) + "}}" + eol
                    + offset + t + "</td>" + eol
                    + offset + "</tr>" + eol;
        }
        return content;
    }

    private String getDomainEntitiesImports(SoftwareFeature softwareFeature) {
        String e = "\n";
        List<DomainEntity> domainEntities = this.domainEntitySpringJpaRepository.findAllBySoftwareFeature_Id(softwareFeature.getId());
        String content = "";
        for (DomainEntity domainEntity : domainEntities) {
            content += "import {" + domainEntity.getName() + "}"
                    + " from "
                    + "'../../domain/" + StringUtility.convertCamelToDash(domainEntity.getName()) + "';" + e;
        }
        return content;
    }

    private String getPopupButtons(UseCase useCase, String offset) {
        String content = "";
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        List<UseCaseRelation> relations = this.useCaseRelationSpringJpaRepository.findAllBySource_Id(useCase.getId());
        for (UseCaseRelation relation : relations) {
            if (relation.getFrontendActionType().equals(FrontendActionTypeEnum.PopupForm)) {
                content += ""
                        + offset + "<button mat-flat-button (click)='" + StringUtility.firstLowerCase(relation.getName()) + "()'>" + eol
                        + offset + t + "{{ '" + useCaseTitle + "." + relation.getName() + "' | translate }}" + eol
                        + offset + "</button>" + eol;
            }
        }
        return content;
    }

    private String getPopupMethods(UseCase useCase) {
        String content = "";
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        List<UseCaseRelation> relations = this.useCaseRelationSpringJpaRepository.findAllBySource_Id(useCase.getId());
        for (UseCaseRelation relation : relations) {
            if (relation.getFrontendActionType().equals(FrontendActionTypeEnum.PopupForm)) {
                String destinationUseCaseTitle = relation.getDestination().getName() + "By" + relation.getDestination().getSoftwareRole().getName();
                content = ""
                        + t + StringUtility.firstLowerCase(relation.getName()) + "(): void {" + eol
                        + t + t + "this.dialogService" + eol
                        + t + t + t + ".quickPopupDialog(" + destinationUseCaseTitle + "Component, this.entityId)" + eol
                        + t + t + t + ".afterClosed()" + eol
                        + t + t + t + ".subscribe(value => this.cultivate());" + eol
                        + t + "}" + eol
                        + eol;
            }
        }
        return content;
    }

    private String getPopupImports(UseCase useCase) {
        String content = "";
        List<UseCaseRelation> relations = this.useCaseRelationSpringJpaRepository.findAllBySource_Id(useCase.getId());
        for (UseCaseRelation relation : relations) {
            if (relation.getFrontendActionType().equals(FrontendActionTypeEnum.PopupForm)) {
                String destinationUseCaseTitle = relation.getDestination().getName() + "By" + relation.getDestination().getSoftwareRole().getName();
                String destinationUseCaseTitleDashed = StringUtility.convertCamelToDash(destinationUseCaseTitle);
                content = "import {" + destinationUseCaseTitle + "Component} from '../" + destinationUseCaseTitleDashed + "/" + destinationUseCaseTitleDashed + ".component';" + eol;
            }
        }
        return content;
    }
}
