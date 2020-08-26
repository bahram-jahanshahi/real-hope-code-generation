package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.update;

import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeCategoryEnum;
import ir.afarinesh.realhope.entities.feature.enums.PrimitiveAttributeTypeEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.update.exceptions.GenerateUseCaseUpdateComponentAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseAngularFormService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetPlantException;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateUseCaseUpdateComponentAngularFile {

    String t = StringUtility.space(2);
    String eol = "\n";
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final UseCaseService useCaseService;
    final UseCaseAngularFormService useCaseAngularFormService;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;

    public GenerateUseCaseUpdateComponentAngularFile(FileManagementService fileManagementService,
                                                     UseCasePathService useCasePathService,
                                                     UseCaseService useCaseService,
                                                     UseCaseAngularFormService useCaseAngularFormService,
                                                     DomainEntitySpringJpaRepository domainEntitySpringJpaRepository) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.useCaseService = useCaseService;
        this.useCaseAngularFormService = useCaseAngularFormService;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
    }

    public void generate(UseCase useCase) throws GenerateUseCaseUpdateComponentAngularFileException {
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
        } catch (CreateFileException | GetPlantException e) {
            throw new GenerateUseCaseUpdateComponentAngularFileException(e.getMessage());
        }
    }

    // path
    protected String getPath(UseCase useCase) {
        String pathSeparator = fileManagementService.pathSeparator();
        return useCasePathService.getAngularCliFeaturePath(useCase.getSoftwareFeature(), useCase.getSoftwareApplicationPanel()) + pathSeparator
                + "components" + fileManagementService.pathSeparator()
                + StringUtility.convertCamelToDash(useCase.getName() + "By" + useCase.getSoftwareRole().getName());
    }

    // component file name
    protected String getComponentFileName(UseCase useCase) {
        return StringUtility.convertCamelToDash(useCase.getName() + "By" + useCase.getSoftwareRole().getName()) + ".component.ts";
    }

    // html file name
    protected String getHtmlFileName(UseCase useCase) {
        return StringUtility.convertCamelToDash(useCase.getName() + "By" + useCase.getSoftwareRole().getName()) + ".component.html";
    }

    // css file name
    protected String getCssFileName(UseCase useCase) {
        return StringUtility.convertCamelToDash(useCase.getName() + "By" + useCase.getSoftwareRole().getName()) + ".component.css";
    }

    // component content
    protected String getComponentContent(UseCase useCase) throws GetPlantException {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String imports = ""
                + "import {Component, Inject, OnInit} from '@angular/core';" + eol
                + "import {UtilityDialogService} from '../../../../../../shares/utilities/utility-dialog.service';" + eol
                + "import {LocaleService} from '../../../../../../core/services/locale.service';" + eol
                + "import {UtilityDateService} from '../../../../../../shares/utilities/utility-date.service';" + eol
                + "import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';" + eol
                + "import {UseCaseSeedsCommand} from '../../../../../../core/domain/use-case-seeds-command';" + eol
                + "import {ConfirmationDialogData} from '../../../../../../shares/components/dialogs/confirmation-dialog/confirmation-dialog.component';" + eol
                + "import {ErrorDialogData} from '../../../../../../shares/components/dialogs/error-dialog/error-dialog.component';" + eol
                + "import {UseCaseCommand} from '../../../../../../core/domain/use-case-command';" + eol
                + "import {FormControl, FormGroup, Validators} from '@angular/forms';" + eol
                + "import {" + eol
                + t + useCaseTitle + "Plant," + eol
                + t + useCaseTitle + "SeedsCommand," + eol
                + t + useCaseTitle + "FruitSeeds," + eol
                + t + useCaseTitle + "Service" + eol
                + "} from '../../services/" + StringUtility.convertCamelToDash(useCaseTitle) + ".service';" + eol
                + "import {SelectEnum} from '../../../../../../core/domain/select-enum';" + eol
                + "import {SelectEntity} from '../../../../../../core/domain/select-entity';" + eol;

        String declarations = ""
                + "@Component({" + eol
                + t + "selector: 'app-" + StringUtility.convertCamelToDash(useCaseTitle) + "'," + eol
                + t + "templateUrl: './" + StringUtility.convertCamelToDash(useCaseTitle) + ".component.html'," + eol
                + t + "styleUrls: ['./" + StringUtility.convertCamelToDash(useCaseTitle) + ".component.css']" + eol
                + "})" + eol;
        String component = ""
                + "export class " + useCaseTitle + "Component implements OnInit {" + eol
                + t + "entityId: number;" + eol
                + t + "readyToUpdate = false;" + eol
                + t + "loading = false;" + eol
                + t + "reactiveForm: FormGroup;" + eol
                + eol
                + this.getSelectVariables(useCase)
                + eol
                + t + "constructor(private useCase: " + useCaseTitle + "Service," + eol
                + t + t + t + t + t + t + t + "private dialogService: UtilityDialogService," + eol
                + t + t + t + t + t + t + t + "private localeService: LocaleService," + eol
                + t + t + t + t + t + t + t + "private dateService: UtilityDateService," + eol
                + t + t + t + t + t + t + t + "public dialogRef: MatDialogRef<" + useCaseTitle + "Component>," + eol
                + t + t + t + t + t + t + t + "@Inject(MAT_DIALOG_DATA) public data: number) {" + eol
                + t + t + "this.entityId = data;" + eol
                + t + "}" + eol
                + eol
                + t + "ngOnInit(): void {" + eol
                + t + t + "this.prepare();" + eol
                + t + "}" + eol
                + eol
                + t + "init(fruitSeeds: " + useCaseTitle + "FruitSeeds): void {" + eol
                + this.getFormGroup(useCase, t)
                + t + t + "this.readyToUpdate = true;" + eol
                + t + "}" + eol
                + eol
                + t + "private submit(): void {" + eol
                + t + "}" + eol
                + eol
                + t + "private cultivate(): void {" + eol
                + t + "}" + eol
                + eol
                + t + "private prepare(): void {" + eol
                + t + t + "this.useCase" + eol
                + t + t + t + ".prepare(new UseCaseSeedsCommand<" + useCaseTitle + "SeedsCommand>(" + eol
                + t + t + t + t + "new " + useCaseTitle + "SeedsCommand(this.entityId)," + eol
                + t + t + t + t + "this.localeService.getLocale().getValue()" + eol
                + t + t + t + "))" + eol
                + t + t + t + ".subscribe(fruitSeeds => {" + eol
                + t + t + t + t + "if (fruitSeeds.isSuccessful) {" + eol
                + t + t + t + t + t + "this.init(fruitSeeds.fruitSeeds);" + eol
                + t + t + t + t + "} else {" + eol
                + t + t + t + t + t + "this.dialogService.showErrorDialog(new ErrorDialogData('', Array.of(fruitSeeds.message)));" + eol
                + t + t + t + t + "}" + eol
                + t + t + t + "}, error => {" + eol
                + t + t + t + t + "this.dialogService.showQuickServerErrorDialog(error.message);" + eol
                + t + t + t + "});" + eol
                + t + "}" + eol
                + eol
                + "}" + eol;
        return imports + eol + declarations + component;
    }

    // html content
    protected String getHtmlContent(UseCase useCase) throws GetPlantException {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String header = ""
                + "<div mat-dialog-title>" + eol
                + t + "{{ '" + useCaseTitle + ".FormTitle' | translate }}" + eol
                + "</div>" + eol;
        String content = ""
                + "<mat-dialog-content>" + eol
                + t + "<div *ngIf='!readyToUpdate' style='height: 240px; display: flex; align-items: center; justify-content: center;'>"
                + t + t + "<mat-spinner diameter='128' strokeWidth='1'></mat-spinner>"
                + t + "</div>"
                + t + "<form [formGroup]='reactiveForm' *ngIf='readyToUpdate'>"
                + this.useCaseAngularFormService.getFormFields(useCase, t + t)
                + t + "</form>"
                + "</mat-dialog-content>" + eol;
        String action = "";
        return header + content + action;
    }

    // css content
    protected String getCssContent(UseCase useCase) {
        return "";
    }

    public String getFormGroup(UseCase useCase, String offset) throws GetPlantException {
        String content = "";
        UseCaseData plant = useCaseService.getPlant(useCase);
        List<UseCaseDataAttribute> attributesOfPlant = plant.getUseCaseDataAttributes();
        for (UseCaseDataAttribute attribute : attributesOfPlant) {
            if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.Primitive)) {
                if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.JavaDate)) {
                    content += offset + t + "const " + StringUtility.firstLowerCase(attribute.getName()) + "FruitSeeds = "
                            + "this.dateService.getMomentOfJavaDate("
                            + "fruitSeeds." + StringUtility.firstLowerCase(attribute.getFruitSeedsAttribute().getName())
                            + ");" + eol;
                } else {
                    content += offset + t + "const " + StringUtility.firstLowerCase(attribute.getName()) + "FruitSeeds ="
                            + " fruitSeeds." + StringUtility.firstLowerCase(attribute.getFruitSeedsAttribute().getName()) + ";" + eol;
                }
            }
            if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEnum)) {
                content += t + t + "const " + StringUtility.firstLowerCase(attribute.getName()) + "EnumFruitSeeds = "
                        + "fruitSeeds." + StringUtility.firstLowerCase(attribute.getFruitSeedsAttribute().getName()) + "Enum;" + eol;
                content += t + t + "this." + StringUtility.firstLowerCase(attribute.getName()) + "EnumArray = "
                        + "fruitSeeds." + StringUtility.firstLowerCase(attribute.getFruitSeedsAttribute().getName()) + "EnumArray;" + eol;
            }
            if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEntity)) {
                content += t + t + "const " + StringUtility.firstLowerCase(attribute.getName()) + "FruitSeeds = "
                        + "fruitSeeds." + StringUtility.firstLowerCase(attribute.getFruitSeedsAttribute().getName()) + ";" + eol;
                content += t + t + "this." + StringUtility.firstLowerCase(attribute.getName()) + "Array = "
                        + "fruitSeeds." + StringUtility.firstLowerCase(attribute.getFruitSeedsAttribute().getName()) + "Array;" + eol;
            }
        }

        content += offset + t + "this.reactiveForm = new FormGroup({" + eol;
        for (int i = 0; i < attributesOfPlant.size(); i++) {
            UseCaseDataAttribute attribute = attributesOfPlant.get(i);
            if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.Primitive)) {
                content += offset + t + t + StringUtility.firstLowerCase(attribute.getName()) + ": new FormControl(" + StringUtility.firstLowerCase(attribute.getName()) + "FruitSeeds" + ")";
            }
            if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEnum)) {
                content += offset + t + t + StringUtility.firstLowerCase(attribute.getName()) + "Enum: new FormControl(" + StringUtility.firstLowerCase(attribute.getName()) + "EnumFruitSeeds.value" + ")";
            }
            if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEntity)) {
                content += offset + t + t + StringUtility.firstLowerCase(attribute.getName()) + ": new FormControl(" + StringUtility.firstLowerCase(attribute.getName()) + "FruitSeeds.value" + ")";
            }
            content += (i < attributesOfPlant.size() - 1) ? "," : "";
            content += eol;
        }
        content += offset + t + "});" + eol;
        return content;
    }

    public String getSelectVariables(UseCase useCase) throws GetPlantException {
        String content = "";
        UseCaseData fruitSeeds = useCaseService.getFruitSeeds(useCase);
        List<UseCaseDataAttribute> attributes = fruitSeeds.getUseCaseDataAttributes();
        for (UseCaseDataAttribute attribute : attributes) {
            if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEnum)) {
                content += t + StringUtility.firstLowerCase(attribute.getName()) + "EnumArray: Array<SelectEnum>;" + eol;
            }
            if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEntity)) {
                content += t + StringUtility.firstLowerCase(attribute.getName()) + "Array: Array<SelectEntity>;" + eol;
            }
        }
        return content;
    }
}
