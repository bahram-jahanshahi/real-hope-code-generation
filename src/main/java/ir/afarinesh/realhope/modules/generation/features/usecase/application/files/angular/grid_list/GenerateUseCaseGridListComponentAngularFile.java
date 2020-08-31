package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.grid_list;

import ir.afarinesh.realhope.entities.feature.*;
import ir.afarinesh.realhope.entities.feature.enums.*;
import ir.afarinesh.realhope.entities.project.SoftwareFeature;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.grid_list.exceptions.GenerateUseCaseGridListComponentAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseAngularFormService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetPlantException;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.UseCaseRelationService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenerateUseCaseGridListComponentAngularFile {
    String t = StringUtility.space(2);
    String eol = StringUtility.endOfLine();
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final UseCaseService useCaseService;
    final UseCaseAngularFormService useCaseAngularFormService;
    final UseCaseRelationService useCaseRelationService;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;

    public GenerateUseCaseGridListComponentAngularFile(FileManagementService fileManagementService,
                                                       UseCasePathService useCasePathService,
                                                       UseCaseService useCaseService,
                                                       UseCaseAngularFormService useCaseAngularFormService,
                                                       UseCaseRelationService useCaseRelationService,
                                                       DomainEntitySpringJpaRepository domainEntitySpringJpaRepository) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.useCaseService = useCaseService;
        this.useCaseAngularFormService = useCaseAngularFormService;
        this.useCaseRelationService = useCaseRelationService;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
    }

    public void generate(UseCase useCase) throws GenerateUseCaseGridListComponentAngularFileException {
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
        } catch (CreateFileException | GetPlantException e) {
            throw new GenerateUseCaseGridListComponentAngularFileException(e.getMessage());
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
    protected String getComponentContent(UseCase useCase) throws GetPlantException {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String imports = ""
                + "import {Component, OnInit, ViewChild} from '@angular/core';" + eol
                + "import {MatPaginator} from '@angular/material/paginator';" + eol
                + "import {MatTableDataSource} from '@angular/material/table';" + eol
                + "import {FormControl, FormGroup} from '@angular/forms';" + eol
                + "import {debounceTime, distinctUntilChanged} from 'rxjs/operators';" + eol
                + "import {NavigationService} from '../../../../../../core/services/navigation.service';" + eol
                + "import {UtilityDialogService} from '../../../../../../shares/utilities/utility-dialog.service';" + eol
                + "import {LocaleService} from '../../../../../../core/services/locale.service';" + eol
                + "import {ResponsiveService} from '../../../../../../core/services/responsive.service';" + eol
                + "import {UseCaseCommand} from '../../../../../../core/domain/use-case-command';" + eol
                + "import {UseCaseSeedsCommand} from '../../../../../../core/domain/use-case-seeds-command';" + eol
                + "import {PaginationCommand} from '../../../../../../core/domain/pagination-command';" + eol
                + "import {ErrorDialogData} from '../../../../../../shares/components/dialogs/error-dialog/error-dialog.component';" + eol
                + "import {SelectEnum} from '../../../../../../core/domain/select-enum';" + eol
                + "import {UtilityDateService} from '../../../../../../shares/utilities/utility-date.service';" + eol
                + "import {" + eol
                + t + useCaseTitle + "Plant" + "," + eol
                + t + useCaseTitle + "Service" + "," + eol
                + t + useCaseTitle + "FruitSeeds" + "," + eol
                + t + useCaseTitle + "SeedsCommand" + "," + eol
                + "} from '../../services/" + StringUtility.convertCamelToDash(useCaseTitle) + ".service';" + eol
                + this.getDomainEntitiesImports(useCase.getSoftwareFeature())
                + this.getRelationImports(useCase);
        String componentContent = "" +
                "@Component({" + eol
                + "  selector: 'app-" + StringUtility.convertCamelToDash(useCaseTitle) + "'," + eol
                + "  templateUrl: './" + StringUtility.convertCamelToDash(useCaseTitle) + ".component.html'," + eol
                + "  styleUrls: ['./" + StringUtility.convertCamelToDash(useCaseTitle) + ".component.css']" + eol
                + "})" + eol
                + "export class " + useCaseTitle + "Component implements OnInit {" + eol
                + eol
                + t + "loading = false;\n"
                + t + "@ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;" + eol
                + eol
                + this.getDisplayedColumns(useCase)
                + eol
                + t + "dataSource = new MatTableDataSource<" + this.getDisplayedFruitDomainEntity(useCase).getName() + ">();" + eol
                + t + "dataArray = new Array<" + this.getDisplayedFruitDomainEntity(useCase).getName() + ">();" + eol
                + eol
                + this.getEnumListVariables(useCase)
                + eol
                + t + "realTimeSearchEnabled = true;" + eol
                + t + "// form controls" + eol
                + this.getSearchFormControls(useCase)
                + eol
                + t + "isWebMedium = false;" + eol
                + eol
                + t + "constructor(private useCase: " + useCaseTitle + "Service," + eol
                + t + t + t + t + t + t + t + "private navigationService: NavigationService," + eol
                + t + t + t + t + t + t + t + "private dialogService: UtilityDialogService," + eol
                + t + t + t + t + t + t + t + "private localeService: LocaleService," + eol
                + t + t + t + t + t + t + t + "private dateService: UtilityDateService," + eol
                + t + t + t + t + t + t + t + "private responsiveService: ResponsiveService) {" + eol
                + t + t + "this.responsiveService.isWebMedium.subscribe(value => this.isWebMedium = value);" + eol
                + t + "}" + eol
                + eol
                + t + "ngOnInit(): void {" + eol
                + t + t + "this.paginator.pageSize = 5;" + eol
                + t + t + "this.paginator.page.subscribe(() => this.search());" + eol
                + t + t + "this.prepare();" + eol
                + this.getMakeRealTimeFormControls(useCase, 4)
                + t + "}" + eol
                + eol
                + t + "realTime(formControl: FormControl): void {\n" +
                "    formControl.valueChanges.pipe(debounceTime(800), distinctUntilChanged())\n" +
                "      .subscribe(() => {\n" +
                "        if (this.realTimeSearchEnabled) {\n" +
                "          this.search();\n" +
                "        }\n" +
                "      });\n" +
                "  }" + eol
                + eol
                + t + "search(): void {" + eol
                + t + t + "this.loading = true;" + eol
                + this.getPlantCultivateVariables(useCase, t + t)
                + t + t + "this.useCase" + eol
                + t + t + t + ".cultivate(" + eol
                + t + t + t + t + "new UseCaseCommand<" + useCaseTitle + "Plant>(" + eol
                + t + t + t + t + t + "new " + useCaseTitle + "Plant(" + eol
                + this.getPlantInputs(useCase, 12)
                + t + t + t + t + t + t + "new PaginationCommand(this.paginator.pageIndex, this.paginator.pageSize)" + eol
                + t + t + t + t + t + ")," + eol
                + t + t + t + t + t + "this.localeService.getLocale().getValue()" + eol
                + t + t + t + t + ")" + eol
                + t + t + t + ")" + eol
                + t + t + t + ".subscribe(fruit => {" + eol
                + t + t + t + t + "this.loading = false;" + eol
                + t + t + t + t + "if (fruit.isSuccessful) {" + eol
                + t + t + t + t + t + "this.dataSource.data = fruit.result.dataArray" + ";" + eol
                + t + t + t + t + t + "this.dataArray = fruit.result.dataArray;" + eol
                + t + t + t + t + t + "this.paginator.length = fruit.result.pagedResultFruit.totalElements;" + eol
                + t + t + t + t + "} else {" + eol
                + t + t + t + t + t + "this.dialogService.showErrorDialog(new ErrorDialogData('', Array.of(fruit.message)));" + eol
                + t + t + t + t + "}" + eol
                + t + t + t + "}, error => {" + eol
                + t + t + t + t + "this.loading = false;" + eol
                + t + t + t + t + "this.dialogService.showQuickServerErrorDialog('Error: ' + error.message);" + eol
                + t + t + t + "});" + eol
                + t + "}" + eol
                + eol
                + t + "prepare(): void {" + eol
                + t + t + "this.useCase" + eol
                + t + t + t + ".prepare(new UseCaseSeedsCommand<" + useCaseTitle + "SeedsCommand>(" + eol
                + t + t + t + t + "new " + useCaseTitle + "SeedsCommand(null)," + eol
                + t + t + t + t + "this.localeService.getLocale().getValue()" + eol
                + t + t + t + "))" + eol
                + t + t + t +".subscribe(fruitSeeds => {" + eol
                + t + t + t + t + "if (fruitSeeds.isSuccessful) {" + eol
                + this.getPrepareResult(useCase, t + t + t + t + t)
                + t + t + t + t + t + "this.search();" + eol
                + t + t + t + t + "} else {" + eol
                + t + t + t + t + t + "this.dialogService.showErrorDialog(new ErrorDialogData('', Array.of(fruitSeeds.message)));" + eol
                + t + t + t + t + "}" + eol
                + t + t + t + "}, error => {" + eol
                + t + t + t + t + "this.dialogService.showQuickServerErrorDialog(error.message);" + eol
                + t + t + t + "});" + eol
                + t + "}" + eol
                + eol
                + t + "resetSearchForm(): void {" + eol
                + t + t + "this.search();" + eol
                + t + "}" + eol
                + eol
                + t + "realTimeSearchChanged($event: boolean): void {" + eol
                + t + t + "this.realTimeSearchEnabled = $event;" + eol
                + t + "}"
                + eol
                + t + "view(row: any) {" + eol
                + this.getPopupView(useCase)
                + t + "}" + eol
                + eol
                + t + "addNew() {" + eol
                + this.getPopupAddNew(useCase)
                + t + "}" + eol
                + eol
                + "}" + eol;
        return imports
                + eol
                + componentContent;
    }

    private String getMakeRealTimeFormControls(UseCase useCase, int offset) throws GetPlantException {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String content = "";
        UseCaseData plant = useCaseService.getPlant(useCase);
        for (int i = 0; i < plant.getUseCaseDataAttributes().size(); i++) {
            UseCaseDataAttribute attribute = plant.getUseCaseDataAttributes().get(i);
            content += StringUtility.space(offset) + "this.realTime(this." + StringUtility.firstLowerCase(attribute.getName()) + "FormControl);" + eol;
        }
        return content;
    }

    // html content
    protected String getHtmlContent(UseCase useCase) throws GetPlantException {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String htmlContent = ""
                + "<mat-card [ngStyle]='!isWebMedium ? {padding: 4} : {}'>" + eol
                + t + "<mat-card-header *ngIf='isWebMedium'>" + eol
                + t + t + "<mat-card-title>" + eol
                + t + t + t + "{{'" + useCaseTitle + ".FormTitle' | translate}}" + eol
                + t + t + "</mat-card-title>" + eol
                + t + "</mat-card-header>" + eol
                + t + "<mat-divider *ngIf='isWebMedium'></mat-divider>" + eol
                + t + "<mat-card-content style='padding-top: 8px; padding-bottom: 92px'>" + eol
                + this.getAddNewHtml(useCase, t + t)
                + t + t + "<!-- Search -->" + eol
                + t + t + "<fieldset style='background-color: #f9f9f9; border-radius: 16px;'>" + eol
                + t + t + t + "<legend>جستجو</legend>" + eol
                + this.getSearchFields(useCase, 6)
                + t + t + "<app-search-button-bar\n" +
                "        [loading]=\"loading\"\n" +
                "        (clickOnSearch)=\"search()\"\n" +
                "        (realTimeSearchChanged)=\"realTimeSearchChanged($event)\"\n" +
                "        (resetSearch)=\"resetSearchForm()\">\n" +
                "      </app-search-button-bar>" + eol
                + t + t + "</fieldset>" + eol
                + t + t + "<!-- Table -->" + eol
                + t + t + "<div fxLayout.gt-sm='row' fxLayout.lt-md='column' *ngIf='isWebMedium'>" + eol
                + t + t + t + "<table mat-table [dataSource]='dataSource' style='width: 100%'>" + eol
                + this.getTableHtmlColumns(useCase, 6)
                + t + t + t + "<mat-header-row *matHeaderRowDef=\"displayedColumns\"></mat-header-row>\n" +
                "        <mat-row *matRowDef=\"let row; columns: displayedColumns;\" mat-ripple\n" +
                "                 (click)=\"view(row)\"\n" +
                "                 style=\"cursor: pointer\">\n" +
                "        </mat-row>" + eol
                + t + t + t + "</table>" + eol
                + t + t + "</div>" + eol
                + t + t + "<!-- No result -->" + eol
                + t + t + "<div *ngIf='dataArray.length === 0'>" + eol
                + t + t + t + "<span style='color: #cf1414'>" + eol
                + t + t + t + t + "نتیجه ای یافت نشد!" + eol
                + t + t + t + "</span>" + eol
                + t + t + "</div>" + eol
                + t + t + "<!-- paginator -->" + eol
                + t + t + "<mat-paginator [pageSizeOptions]='[5, 10, 20]' showFirstLastButtons></mat-paginator>" + eol
                + t + "</mat-card-content>" + eol
                + "</mat-card>";
        return htmlContent;
    }

    private String getTableHtmlColumns(UseCase useCase, int offset) {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String content = "";
        DomainEntity fruitDomainEntity = getDisplayedFruitDomainEntity(useCase);
        List<DomainEntityAttribute> attributes = fruitDomainEntity.getDomainEntityAttributes();
        for (DomainEntityAttribute attribute : attributes) {
            content += StringUtility.space(offset + 2) + "<ng-container matColumnDef='" + attribute.getName() + "'>" + eol;
            content += StringUtility.space(offset + 4) + "<mat-header-cell *matHeaderCellDef>" + eol;
            content += StringUtility.space(offset + 6) + "{{ '" + useCaseTitle + "." + attribute.getName() + "Column' | translate }}" + eol;
            content += StringUtility.space(offset + 4) + "</mat-header-cell>" + eol;
            content += StringUtility.space(offset + 4) + "<mat-cell *matCellDef='let element'>" + eol;
            content += StringUtility.space(offset + 6) + "{{element." + StringUtility.firstLowerCase(attribute.getName()) + "}}" + eol;
            content += StringUtility.space(offset + 4) + "</mat-cell>" + eol;
            content += StringUtility.space(offset + 2) + "</ng-container>" + eol;
        }
        return content;
    }

    // css content
    protected String getCssContent(UseCase useCase) {
        return "";
    }

    private String getDomainEntitiesImports(SoftwareFeature softwareFeature) {
        List<DomainEntity> domainEntities = this.domainEntitySpringJpaRepository.findAllBySoftwareFeature_Id(softwareFeature.getId());
        String content = "";
        for (DomainEntity domainEntity : domainEntities) {
            content += "import {" + domainEntity.getName() + "}"
                    + " from "
                    + "'../../domain/" + StringUtility.convertCamelToDash(domainEntity.getName()) + "';" + eol;
        }
        return content;
    }

    private String getDisplayedColumns(UseCase useCase) {
        String content = t + "displayedColumns = [" + eol;
        DomainEntity displayedDomainEntity = this.getDisplayedFruitDomainEntity(useCase);
        if (displayedDomainEntity != null) {
            for (int i = 0; i < displayedDomainEntity.getDomainEntityAttributes().size(); i++) {
                DomainEntityAttribute attribute = displayedDomainEntity.getDomainEntityAttributes().get(i);
                content += t + t + "'" + attribute.getName() + "'";
                if (i < displayedDomainEntity.getDomainEntityAttributes().size() - 1) {
                    content += ",";
                }
                content += "\n";
            }
        }
        content += t + "];" + eol;
        return content;
    }

    private String getSearchFormControls(UseCase useCase) throws GetPlantException {
        String content = "";
        UseCaseData plant = useCaseService.getPlant(useCase);
        for (int i = 0; i < plant.getUseCaseDataAttributes().size(); i++) {
            UseCaseDataAttribute attribute = plant.getUseCaseDataAttributes().get(i);
            content += t + t + StringUtility.firstLowerCase(attribute.getName()) + "FormControl = new FormControl(null);" + eol;
        }
        return content;
    }

    private String getSearchFields(UseCase useCase, int offset) throws GetPlantException {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String content = "";
        UseCaseData plant = useCaseService.getPlant(useCase);
        Long maxRow = getMaxRowOfUseCaseDataAttributes(plant.getUseCaseDataAttributes());
        for (long row = 1; row <= maxRow; row++ ){
            content += StringUtility.space(offset) + "<div fxLayout.lt-md='column' fxLayout.gt-sm='row' fxLayoutGap.gt-sm='16px' fxLayoutGap.lt-md='8px'>" + eol;
            for (long col = 1; col <= 4; col++) {
                UseCaseDataAttribute attribute = getAttributeByRowAndColumn(plant.getUseCaseDataAttributes(), row, col);
                if (attribute != null) {
                    content += StringUtility.space(offset + 2) + "<mat-form-field appearance='outline' fxFlex.gt-sm='25%' fxFlex.lt-md='100%'>" + eol;
                    content += StringUtility.space(offset + 4) + "<mat-label>" + eol;
                    content += StringUtility.space(offset + 6) + "{{'" + useCaseTitle + "." + attribute.getName() + "SearchField' | translate}}" + eol;
                    content += StringUtility.space(offset + 4) + "</mat-label>" + eol;
                    content += useCaseAngularFormService.getFormFieldInput(useCaseTitle, attribute, t + t + t + t, true);
                    content += StringUtility.space(offset + 2) + "</mat-form-field>" + eol;
                }
            }
            content += StringUtility.space(offset) + "</div>" + eol;
        }
        return content;
    }

    private String getPlantCultivateVariables(UseCase useCase, String offset) throws GetPlantException {
        String content = "";
        UseCaseData plant = useCaseService.getPlant(useCase);
        List<UseCaseDataAttribute> attributesOfPlant = plant.getUseCaseDataAttributes();
        for (UseCaseDataAttribute attribute : attributesOfPlant) {
            if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.Primitive)) {
                if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.JavaDate)) {
                    content += offset + "const " + StringUtility.firstLowerCase(attribute.getName()) + "Input = "
                            + "this.dateService.getJavaDateOfMoment(this." + StringUtility.firstLowerCase(attribute.getName()) + "FormControl.value);" + eol;

                } else {
                    content += offset + "const " + StringUtility.firstLowerCase(attribute.getName()) + "Input = "
                            + "this." + StringUtility.firstLowerCase(attribute.getName()) + "FormControl.value;" + eol;
                }
            }
            if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEnum)) {
                content += offset + "const " + StringUtility.firstLowerCase(attribute.getName()) + "Input = new SelectEnum(null, "
                        + "this." + StringUtility.firstLowerCase(attribute.getName()) + "FormControl.value);" + eol;
            }
            if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEntity)) {
                content += offset + "const " + StringUtility.firstLowerCase(attribute.getName()) + "Input = new SelectEntity(null, "
                        + "this." + StringUtility.firstLowerCase(attribute.getName()) + "FormControl.value);" + eol;
            }
        }
        return content;
    }

    private String getPlantInputs(UseCase useCase, int offset) throws GetPlantException {
        String content = "";
        UseCaseData plant = useCaseService.getPlant(useCase);
        for (int i = 0; i < plant.getUseCaseDataAttributes().size(); i++) {
            UseCaseDataAttribute attribute = plant.getUseCaseDataAttributes().get(i);
            content += StringUtility.space(offset) + StringUtility.firstLowerCase(attribute.getName()) + "Input," + eol;
        }
        return content;
    }

    private DomainEntity getDisplayedFruitDomainEntity(UseCase useCase) {
        Optional<UseCaseData> first = useCase
                .getUseCaseDataSet()
                .stream()
                .filter(useCaseData -> useCaseData.getUseCaseDataType().equals(UseCaseDataTypeEnum.Fruit))
                .findFirst();
        if (first.isPresent()) {
            Optional<UseCaseDataAttribute> firstAttribute = first
                    .get()
                    .getUseCaseDataAttributes()
                    .stream()
                    .filter(useCaseDataAttribute -> useCaseDataAttribute.getAttributeQuantity().equals(EntityAttributeQuantityEnum.List))
                    .findFirst();
            if (firstAttribute.isPresent()) {
                return firstAttribute.get().getDomainEntityAttributeType();
            }
        }
        return null;
    }

    private String getRelationImports(UseCase useCase) {
        String content = "";
        List<UseCaseRelation> relations = this.useCaseRelationService.findAllBySource(useCase);
        for (UseCaseRelation relation : relations) {
            String destinationUseCaseTitle = relation.getDestination().getName() + "By" + relation.getDestination().getSoftwareRole().getName();
            content += ""
                    + "import {" + destinationUseCaseTitle + "Component} "
                    + "from '../" + StringUtility.convertCamelToDash(destinationUseCaseTitle) + "/"
                    + StringUtility.convertCamelToDash(destinationUseCaseTitle) + ".component';" + eol;
        }
        return content;
    }

    private String getPopupView(UseCase useCase) {
        String content = "";
        List<UseCaseRelation> relations = this.useCaseRelationService.findAllBySource(useCase);
        UseCaseRelation viewRelation = null;
        for (UseCaseRelation relation : relations) {
            if (relation.getDestination().getUserInterfaceType().equals(UserInterfaceTypeEnum.View)) {
                viewRelation = relation;
            }
        }
        if (viewRelation != null) {
            String destinationUseCaseTitle = viewRelation.getDestination().getName() + "By" + viewRelation.getDestination().getSoftwareRole().getName();
            content = ""
                    + t + t + "this.dialogService" + eol
                    + t + t + t + ".quickPopupDialog(" + destinationUseCaseTitle + "Component, row.id)" + eol
                    + t + t + t + ".afterClosed()" + eol
                    + t + t + t + ".subscribe(value => this.search());" + eol;
        }
        return content;
    }

    private String getPopupAddNew(UseCase useCase) {
        String content = "";
        List<UseCaseRelation> relations = this.useCaseRelationService.findAllBySource(useCase);
        UseCaseRelation addNewRelation = null;
        for (UseCaseRelation relation : relations) {
            if (relation.getDestination().getUserInterfaceType().equals(UserInterfaceTypeEnum.AddNew)) {
                addNewRelation = relation;
            }
        }
        if (addNewRelation != null) {
            String destinationUseCaseTitle = addNewRelation.getDestination().getName() + "By" + addNewRelation.getDestination().getSoftwareRole().getName();
            content = ""
                    + t + t + "this.dialogService" + eol
                    + t + t + t + ".quickPopupDialog(" + destinationUseCaseTitle + "Component, null)" + eol
                    + t + t + t + ".afterClosed()" + eol
                    + t + t + t + ".subscribe(value => this.search());" + eol;
        }
        return content;
    }

    private String getAddNewHtml(UseCase useCase, String offset) {
        String content = "";
        List<UseCaseRelation> relations = this.useCaseRelationService.findAllBySource(useCase);
        UseCaseRelation addNewRelation = null;
        for (UseCaseRelation relation : relations) {
            if (relation.getDestination().getUserInterfaceType().equals(UserInterfaceTypeEnum.AddNew)) {
                addNewRelation = relation;
            }
        }
        if (addNewRelation != null) {
            content += ""
                    + offset + "<button mat-raised-button color='primary' (click)='addNew()'>"
                    + addNewRelation.getDestination().getFaTitle()
                    + "</button>" + eol;
        }
        return content;
    }

    public UseCaseDataAttribute getAttributeByRowAndColumn(List<UseCaseDataAttribute> attributes, Long row, Long column) {
        for (UseCaseDataAttribute attribute : attributes) {
            if (attribute.getUiRow().equals(row) && attribute.getUiColumn().equals(column)) {
                return attribute;
            }
        }
        return null;
    }

    public Long getMaxRowOfUseCaseDataAttributes(List<UseCaseDataAttribute> attributes) {
        Long max = 0L;
        for (UseCaseDataAttribute attribute : attributes) {
            if (attribute.getUiRow() > max) {
                max = attribute.getUiRow();
            }
        }
        return max;
    }

    private String getEnumListVariables(UseCase useCase) throws GetPlantException {
        String content = "";
        UseCaseData plant = useCaseService.getPlant(useCase);
        for (int i = 0; i < plant.getUseCaseDataAttributes().size(); i++) {
            UseCaseDataAttribute attribute = plant.getUseCaseDataAttributes().get(i);
            if (attribute.isSelectEnum()) {
                content += t + StringUtility.firstLowerCase(attribute.getName()) + "EnumArray = new Array<SelectEnum>();" + eol;
            }
        }
        return content;
    }

    private String getPrepareResult(UseCase useCase, String offset) throws GetPlantException {
        String content = "";
        UseCaseData fruitSeeds = useCaseService.getFruitSeeds(useCase);
        for (int i = 0; i < fruitSeeds.getUseCaseDataAttributes().size(); i++) {
            UseCaseDataAttribute attribute = fruitSeeds.getUseCaseDataAttributes().get(i);
            if (attribute.isSelectEnum()) {
                content += offset + "this." + StringUtility.firstLowerCase(attribute.getName()) + "EnumArray = fruitSeeds.fruitSeeds." + StringUtility.firstLowerCase(attribute.getName()) + "EnumArray;" + eol;
            }
        }
        return content;
    }
}
