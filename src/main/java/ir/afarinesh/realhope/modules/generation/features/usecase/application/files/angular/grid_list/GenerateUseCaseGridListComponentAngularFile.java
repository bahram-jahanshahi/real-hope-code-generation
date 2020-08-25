package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.grid_list;

import ir.afarinesh.realhope.entities.feature.*;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeQuantityEnum;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseDataTypeEnum;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
import ir.afarinesh.realhope.entities.project.SoftwareFeature;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.grid_list.exceptions.GenerateUseCaseGridListComponentAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.UseCaseRelationSpringJpaRepository;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenerateUseCaseGridListComponentAngularFile {
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;
    final UseCaseRelationSpringJpaRepository useCaseRelationSpringJpaRepository;

    public GenerateUseCaseGridListComponentAngularFile(FileManagementService fileManagementService,
                                                       UseCasePathService useCasePathService,
                                                       DomainEntitySpringJpaRepository domainEntitySpringJpaRepository,
                                                       UseCaseRelationSpringJpaRepository useCaseRelationSpringJpaRepository) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
        this.useCaseRelationSpringJpaRepository = useCaseRelationSpringJpaRepository;
    }

    public void generate(UseCase useCase) throws GenerateUseCaseGridListComponentAngularFileException {
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
        } catch (CreateFileException e) {
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
    protected String getComponentContent(UseCase useCase) {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String imports = ""
                + "import {Component, OnInit, ViewChild} from '@angular/core';" + "\n"
                + "import {MatPaginator} from '@angular/material/paginator';" + "\n"
                + "import {MatTableDataSource} from '@angular/material/table';" + "\n"
                + "import {FormControl} from '@angular/forms';" + "\n"
                + "import {debounceTime, distinctUntilChanged} from 'rxjs/operators';" + "\n"
                + "import {NavigationService} from '../../../../../../core/services/navigation.service';" + "\n"
                + "import {UtilityDialogService} from '../../../../../../shares/utilities/utility-dialog.service';" + "\n"
                + "import {LocaleService} from '../../../../../../core/services/locale.service';" + "\n"
                + "import {ResponsiveService} from '../../../../../../core/services/responsive.service';" + "\n"
                + "import {UseCaseCommand} from '../../../../../../core/domain/use-case-command';" + "\n"
                + "import {PaginationCommand} from '../../../../../../core/domain/pagination-command';" + "\n"
                + "import {ErrorDialogData} from '../../../../../../shares/components/dialogs/error-dialog/error-dialog.component';" + "\n"
                + "import {" + "\n"
                + "  " + useCaseTitle + "Plant" + "," + "\n"
                + "  " + useCaseTitle + "Service" + "\n"
                + "} from '../../services/" + StringUtility.convertCamelToDash(useCaseTitle) + ".service';" + "\n"
                + this.getDomainEntitiesImports(useCase.getSoftwareFeature())
                + this.getRelationImports(useCase);
        String componentContent = "" +
                "@Component({" + "\n"
                + "  selector: 'app-" + StringUtility.convertCamelToDash(useCaseTitle) + "'," + "\n"
                + "  templateUrl: './" + StringUtility.convertCamelToDash(useCaseTitle) + ".component.html'," + "\n"
                + "  styleUrls: ['./" + StringUtility.convertCamelToDash(useCaseTitle) + ".component.css']" + "\n"
                + "})" + "\n"
                + "export class " + useCaseTitle + "Component implements OnInit {" + "\n"
                + "\n"
                + StringUtility.space(2) + "loading = false;\n"
                + StringUtility.space(2) + "@ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;" + "\n"
                + "\n"
                + this.getDisplayedColumns(useCase)
                + "\n"
                + StringUtility.space(2) + "dataSource = new MatTableDataSource<" + this.getDisplayedFruitDomainEntity(useCase).getName() + ">();" + "\n"
                + StringUtility.space(2) + "dataArray = new Array<" + this.getDisplayedFruitDomainEntity(useCase).getName() + ">();" + "\n"
                + "\n"
                + StringUtility.space(2) + "realTimeSearchEnabled = true;" + "\n"
                + StringUtility.space(2) + "// form controls" + "\n"
                + this.getSearchFormControls(useCase)
                + StringUtility.space(2) + "isWebMedium = false;" + "\n"
                + "\n"
                + StringUtility.space(2) + "constructor(private useCase: " + useCaseTitle + "Service," + "\n"
                + StringUtility.space(14) + "private navigationService: NavigationService," + "\n"
                + StringUtility.space(14) + "private dialogService: UtilityDialogService," + "\n"
                + StringUtility.space(14) + "private localeService: LocaleService," + "\n"
                + StringUtility.space(14) + "private responsiveService: ResponsiveService) {" + "\n"
                + StringUtility.space(4) + "this.responsiveService.isWebMedium.subscribe(value => this.isWebMedium = value);" + "\n"
                + StringUtility.space(2) + "}" + "\n"
                + "\n"
                + StringUtility.space(2) + "ngOnInit(): void {" + "\n"
                + StringUtility.space(4) + "this.paginator.pageSize = 5;" + "\n"
                + StringUtility.space(4) + "this.paginator.page.subscribe(() => this.search());" + "\n"
                + StringUtility.space(4) + "this.prepare();" + "\n"
                + this.getMakeRealTimeFormControls(useCase, 4)
                + StringUtility.space(2) + "}" + "\n"
                + "\n"
                + StringUtility.space(2) + "realTime(formControl: FormControl): void {\n" +
                "    formControl.valueChanges.pipe(debounceTime(800), distinctUntilChanged())\n" +
                "      .subscribe(() => {\n" +
                "        if (this.realTimeSearchEnabled) {\n" +
                "          this.search();\n" +
                "        }\n" +
                "      });\n" +
                "  }" + "\n"
                + "\n"
                + StringUtility.space(2) + "search(): void {" + "\n"
                + StringUtility.space(4) + "this.loading = true;" + "\n"
                + StringUtility.space(4) + "this.useCase" + "\n"
                + StringUtility.space(6) + ".cultivate(" + "\n"
                + StringUtility.space(8) + "new UseCaseCommand<" + useCaseTitle + "Plant>(" + "\n"
                + StringUtility.space(10) + "new " + useCaseTitle + "Plant(" + "\n"
                + this.getPlantInputs(useCase, 12)
                + StringUtility.space(12) + "new PaginationCommand(this.paginator.pageIndex, this.paginator.pageSize)" + "\n"
                + StringUtility.space(10) + ")," + "\n"
                + StringUtility.space(10) + "this.localeService.getLocale().getValue()" + "\n"
                + StringUtility.space(8) + ")" + "\n"
                + StringUtility.space(6) + ")" + "\n"
                + StringUtility.space(6) + ".subscribe(fruit => {" + "\n"
                + StringUtility.space(8) + "this.loading = false;" + "\n"
                + StringUtility.space(8) + "if (fruit.isSuccessful) {" + "\n"
                + StringUtility.space(10) + "this.dataSource.data = fruit.result.dataArray" + ";" + "\n"
                + StringUtility.space(10) + "this.dataArray = fruit.result.dataArray;" + "\n"
                + StringUtility.space(10) + "this.paginator.length = fruit.result.pagedResultFruit.totalElements;" + "\n"
                + StringUtility.space(8) + "} else {" + "\n"
                + StringUtility.space(10) + "this.dialogService.showErrorDialog(new ErrorDialogData('', Array.of(fruit.message)));" + "\n"
                + StringUtility.space(8) + "}" + "\n"
                + StringUtility.space(6) + "}, error => {" + "\n"
                + StringUtility.space(8) + "this.loading = false;" + "\n"
                + StringUtility.space(8) + "this.dialogService.showQuickServerErrorDialog('Error: ' + error.message);" + "\n"
                + StringUtility.space(6) + "});" + "\n"
                + StringUtility.space(2) + "}" + "\n"
                + "\n"
                + StringUtility.space(2) + "prepare(): void {" + "\n"
                + StringUtility.space(4) + "this.search();" + "\n"
                + StringUtility.space(2) + "}" + "\n"
                + "\n"
                + StringUtility.space(2) + "resetSearchForm(): void {" + "\n"
                + StringUtility.space(4) + "this.search();" + "\n"
                + StringUtility.space(2) + "}" + "\n"
                + "\n"
                + StringUtility.space(2) + "realTimeSearchChanged($event: boolean): void {" + "\n"
                + StringUtility.space(4) + "this.realTimeSearchEnabled = $event;" + "\n"
                + StringUtility.space(2) + "}"
                + "\n"
                + StringUtility.space(2) + "view(row: any) {" + "\n"
                + this.getPopupView(useCase)
                + StringUtility.space(2) + "}" + "\n"
                + "}" + "\n";
        return imports
                + "\n"
                + componentContent;
    }

    private String getMakeRealTimeFormControls(UseCase useCase, int offset) {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String content = "";
        UseCaseData plant = this.getPlant(useCase);
        for (int i = 0; i < plant.getUseCaseDataAttributes().size(); i++) {
            UseCaseDataAttribute attribute = plant.getUseCaseDataAttributes().get(i);
            content += StringUtility.space(offset) + "this.realTime(this." + StringUtility.firstLowerCase(attribute.getName()) + "FormControl);" + "\n";
        }
        return content;
    }

    // html content
    protected String getHtmlContent(UseCase useCase) {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String htmlContent = ""
                + "<mat-card [ngStyle]='!isWebMedium ? {padding: 4} : {}'>" + "\n"
                + StringUtility.space(2) + "<mat-card-header *ngIf='isWebMedium'>" + "\n"
                + StringUtility.space(4) + "<mat-card-title>" + "\n"
                + StringUtility.space(6) + "{{'" + useCaseTitle + ".FormTitle' | translate}}" + "\n"
                + StringUtility.space(4) + "</mat-card-title>" + "\n"
                + StringUtility.space(2) + "</mat-card-header>" + "\n"
                + StringUtility.space(2) + "<mat-divider *ngIf='isWebMedium'></mat-divider>" + "\n"
                + StringUtility.space(2) + "<mat-card-content style='padding-top: 8px; padding-bottom: 92px'>" + "\n"
                + StringUtility.space(4) + "<!-- Search -->" + "\n"
                + StringUtility.space(4) + "<fieldset style='background-color: #f9f9f9; border-radius: 16px;'>" + "\n"
                + StringUtility.space(6) + "<legend>جستجو</legend>" + "\n"
                + this.getSearchFields(useCase, 6)
                + StringUtility.space(4) + "<app-search-button-bar\n" +
                "        [loading]=\"loading\"\n" +
                "        (clickOnSearch)=\"search()\"\n" +
                "        (realTimeSearchChanged)=\"realTimeSearchChanged($event)\"\n" +
                "        (resetSearch)=\"resetSearchForm()\">\n" +
                "      </app-search-button-bar>" + "\n"
                + StringUtility.space(4) + "</fieldset>" + "\n"
                + StringUtility.space(4) + "<!-- Table -->" + "\n"
                + StringUtility.space(4) + "<div fxLayout.gt-sm='row' fxLayout.lt-md='column' *ngIf='isWebMedium'>" + "\n"
                + StringUtility.space(6) + "<table mat-table [dataSource]='dataSource' style='width: 100%'>" + "\n"
                + this.getTableHtmlColumns(useCase, 6)
                + StringUtility.space(6) + "<mat-header-row *matHeaderRowDef=\"displayedColumns\"></mat-header-row>\n" +
                "        <mat-row *matRowDef=\"let row; columns: displayedColumns;\" mat-ripple\n" +
                "                 (click)=\"view(row)\"\n" +
                "                 style=\"cursor: pointer\">\n" +
                "        </mat-row>" + "\n"
                + StringUtility.space(6) + "</table>" + "\n"
                + StringUtility.space(4) + "</div>" + "\n"
                + StringUtility.space(4) + "<!-- No result -->" + "\n"
                + StringUtility.space(4) + "<div *ngIf='dataArray.length === 0'>" + "\n"
                + StringUtility.space(6) + "<span style='color: #cf1414'>" + "\n"
                + StringUtility.space(8) + "نتیجه ای یافت نشد!" + "\n"
                + StringUtility.space(6) + "</span>" + "\n"
                + StringUtility.space(4) + "</div>" + "\n"
                + StringUtility.space(4) + "<!-- paginator -->" + "\n"
                + StringUtility.space(4) + "<mat-paginator [pageSizeOptions]='[5, 10, 20]' showFirstLastButtons></mat-paginator>" + "\n"
                + StringUtility.space(2) + "</mat-card-content>" + "\n"
                + "</mat-card>";
        return htmlContent;
    }

    private String getTableHtmlColumns(UseCase useCase, int offset) {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String content = "";
        DomainEntity fruitDomainEntity = getDisplayedFruitDomainEntity(useCase);
        List<DomainEntityAttribute> attributes = fruitDomainEntity.getDomainEntityAttributes();
        for (DomainEntityAttribute attribute : attributes) {
            content += StringUtility.space(offset + 2) + "<ng-container matColumnDef='" + attribute.getName() + "'>" + "\n";
            content += StringUtility.space(offset + 4) + "<mat-header-cell *matHeaderCellDef>" + "\n";
            content += StringUtility.space(offset + 6) + "{{ '" + useCaseTitle + "." + attribute.getName() + "Column' | translate }}" + "\n";
            content += StringUtility.space(offset + 4) + "</mat-header-cell>" + "\n";
            content += StringUtility.space(offset + 4) + "<mat-cell *matCellDef='let element'>" + "\n";
            content += StringUtility.space(offset + 6) + "{{element." + StringUtility.firstLowerCase(attribute.getName()) + "}}" + "\n";
            content += StringUtility.space(offset + 4) + "</mat-cell>" + "\n";
            content += StringUtility.space(offset + 2) + "</ng-container>" + "\n";
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
                    + "'../../domain/" + StringUtility.convertCamelToDash(domainEntity.getName()) + "';" + "\n";
        }
        return content;
    }

    private String getDisplayedColumns(UseCase useCase) {
        String content = StringUtility.space(2) + "displayedColumns = [" + "\n";
        DomainEntity displayedDomainEntity = this.getDisplayedFruitDomainEntity(useCase);
        if (displayedDomainEntity != null) {
            for (int i = 0; i < displayedDomainEntity.getDomainEntityAttributes().size(); i++) {
                DomainEntityAttribute attribute = displayedDomainEntity.getDomainEntityAttributes().get(i);
                content += StringUtility.space(4) + "'" + attribute.getName() + "'";
                if (i < displayedDomainEntity.getDomainEntityAttributes().size() - 1) {
                    content += ",";
                }
                content += "\n";
            }
        }
        content += StringUtility.space(2) + "];" + "\n";
        return content;
    }

    private String getSearchFormControls(UseCase useCase) {
        String content = "";
        UseCaseData plant = this.getPlant(useCase);
        for (int i = 0; i < plant.getUseCaseDataAttributes().size(); i++) {
            UseCaseDataAttribute attribute = plant.getUseCaseDataAttributes().get(i);
            content += StringUtility.space(2) + StringUtility.firstLowerCase(attribute.getName()) + "FormControl = new FormControl(null);" + "\n";
        }
        return content;
    }

    private String getSearchFields(UseCase useCase, int offset) {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String content = "";
        UseCaseData plant = this.getPlant(useCase);
        for (int i = 0; i < plant.getUseCaseDataAttributes().size(); i++) {
            UseCaseDataAttribute attribute = plant.getUseCaseDataAttributes().get(i);
            content += StringUtility.space(offset) + "<div fxLayout.lt-md='column' fxLayout.gt-sm='row' fxLayoutGap.gt-sm='16px' fxLayoutGap.lt-md='8px'>" + "\n";
            content += StringUtility.space(offset + 2) + "<mat-form-field appearance='outline' fxFlex.gt-sm='33%' fxFlex.lt-md='100%'>" + "\n";
            content += StringUtility.space(offset + 4) + "<mat-label>" + "\n";
            content += StringUtility.space(offset + 6) + "{{'" + useCaseTitle + "." + attribute.getName() + "SearchField' | translate}}" + "\n";
            content += StringUtility.space(offset + 4) + "</mat-label>" + "\n";
            content += StringUtility.space(offset + 4) + "<input matInput [formControl]='" + StringUtility.firstLowerCase(attribute.getName()) + "FormControl'>" + "\n";
            content += StringUtility.space(offset + 2) + "</mat-form-field>" + "\n";
            content += StringUtility.space(offset) + "</div>" + "\n";
        }
        return content;
    }

    private String getPlantInputs(UseCase useCase, int offset) {
        String content = "";
        UseCaseData plant = this.getPlant(useCase);
        for (int i = 0; i < plant.getUseCaseDataAttributes().size(); i++) {
            UseCaseDataAttribute attribute = plant.getUseCaseDataAttributes().get(i);
            content += StringUtility.space(offset) + "this." + StringUtility.firstLowerCase(attribute.getName()) + "FormControl.value," + "\n";
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

    private UseCaseData getPlant(UseCase useCase) {
        return useCase
                .getUseCaseDataSet()
                .stream()
                .filter(useCaseData -> useCaseData.getUseCaseDataType().equals(UseCaseDataTypeEnum.Plant))
                .findFirst()
                .get();
    }

    private String getRelationImports(UseCase useCase) {
        String t = StringUtility.space(2);
        String eol = "\n";
        String content = "";
        List<UseCaseRelation> relations = this.useCaseRelationSpringJpaRepository.findAllBySource_Id(useCase.getId());
        for (UseCaseRelation relation : relations) {
            String destinationUseCaseTitle = relation.getDestination().getName() + "By" + relation.getDestination().getSoftwareRole().getName();
            content += ""
                    + "import {" + destinationUseCaseTitle + "Component} "
                    + "from '../" + StringUtility.convertCamelToDash(destinationUseCaseTitle) + "/"
                    + StringUtility.convertCamelToDash(destinationUseCaseTitle) + ".component';" + "\n";
        }
        return content;
    }

    private String getPopupView(UseCase useCase) {
        String t = StringUtility.space(2);
        String eol = "\n";
        String content = "";
        List<UseCaseRelation> relations = this.useCaseRelationSpringJpaRepository.findAllBySource_Id(useCase.getId());
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
                    + t + t + t + ".afterClosed()"
                    + t + t + t + ".subscribe(value => this.search());"
                    + eol;
        }
        return content;
    }
}
