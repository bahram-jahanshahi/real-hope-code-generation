package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.delete;

import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.delete.exceptions.GenerateUseCaseDeleteComponentAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetViewDomainEntityException;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

@Service
public class GenerateUseCaseDeleteComponentAngularFile {
    String t = StringUtility.space(2);
    String eol = "\n";
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final UseCaseService useCaseService;

    public GenerateUseCaseDeleteComponentAngularFile(FileManagementService fileManagementService,
                                                     UseCasePathService useCasePathService,
                                                     UseCaseService useCaseService) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.useCaseService = useCaseService;
    }

    public void generate(UseCase useCase) throws GenerateUseCaseDeleteComponentAngularFileException {
        try {
            // create component
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getComponentFileName(useCase),
                            this.getComponentContent(useCase),
                            true
                    );
            // create html
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getHtmlFileName(useCase),
                            this.getHtmlContent(useCase),
                            true
                    );
            // create css
            fileManagementService
                    .createFile(
                            this.getPath(useCase),
                            this.getCssFileName(useCase),
                            this.getCssContent(useCase),
                            true
                    );
        } catch (CreateFileException | GetViewDomainEntityException e) {
            throw new GenerateUseCaseDeleteComponentAngularFileException(e.getMessage());
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
                + "import {UseCaseCommand} from '../../../../../../core/domain/use-case-command';" + eol;
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
                + t + "readyToDelete = false;" + eol
                + eol
                + t + "constructor(private useCase: " + useCaseTitle + "Service," + eol
                + t + t + t + t + t + t + t + "private dialogService: UtilityDialogService," + eol
                + t + t + t + t + t + t + t + "private localeService: LocaleService," + eol
                + t + t + t + t + t + t + t + "public dialogRef: MatDialogRef<" + useCaseTitle + "Component>," + eol
                + t + t + t + t + t + t + t + "@Inject(MAT_DIALOG_DATA) public data: number) {" + eol
                + t + t + "this.entityId = data;" + eol
                + t + "}" + eol
                + eol
                + t + "ngOnInit(): void {" + eol
                + t + "}" + eol
                + eol
                + t + "public submit(): void {" + eol
                + t + t + "this.dialogService" + eol
                + t + t + t + ".showQuickConfirmationDialog()" + eol
                + t + t + t + ".afterClosed()" + eol
                + t + t + t + ".subscribe(value => {" + eol
                + t + t + t + t + "if (value === 'Yes') {" + eol
                + t + t + t + t + t + "this.cultivate();" + eol
                + t + t + t + t + "}" + eol
                + t + t + t + "});" + eol
                + t + "}" + eol
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
                + t + t + t + t + t + "this.close();" + eol
                + t + t + t + t + "} else {" + eol
                + t + t + t + t + t + "this.dialogService.showErrorDialog( new ErrorDialogData('', Array.of(fruit.message)));" + eol
                + t + t + t + t + "}" + eol
                + t + t + t + "}, error => {" + eol
                + t + t + t + t + "this.dialogService.showQuickServerErrorDialog('Error: ' + error.message);" + eol
                + t + t + t + "});" + eol
                + t + "}" + eol
                + eol
                + t + "close(): void {\n"
                + t + t + "this.dialogRef.close();\n"
                + t + "}" + eol
                + "}" + eol;
        return imports + eol + componentContent;
    }

    // html content
    protected String getHtmlContent(UseCase useCase) throws GetViewDomainEntityException {
        String useCaseTitle = useCaseService.getUseCaseTitle(useCase);
        String header = ""
                + "<div mat-dialog-title>" + eol
                + t + "{{ '" + useCaseTitle + ".FormTitle' | translate }}" + eol
                + "</div>" + eol;
        String content = ""
                + "<mat-dialog-content>" + eol
                + t + "<div *ngIf='!readyToDelete' style='height: 240px; display: flex; align-items: center; justify-content: center;'>" + eol
                + t + t + "<mat-spinner diameter='128' strokeWidth='1'></mat-spinner>" + eol
                + t + "</div>" + eol
                + "</mat-dialog-content>" + eol
                + "<mat-dialog-actions>" + eol
                + t + "<button mat-flat-button (click)='close()'>" + eol
                + t + t + "{{'" + useCaseTitle + ".Close' | translate}}" + eol
                + t + "</button>" + eol
                + t + "<button mat-raised-button color='primary' (click)='submit()'>" + eol
                + t + t + "{{'" + useCaseTitle + ".Submit' | translate}}" + eol
                + t + "</button>" + eol
                + "</mat-dialog-actions>";
        String action = "";
        return header + content + action;
    }

    // css content
    protected String getCssContent(UseCase useCase) {
        return "";
    }
}
