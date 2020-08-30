package ir.afarinesh.realhope.modules.generation.features.usecase.application.shares;

import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeCategoryEnum;
import ir.afarinesh.realhope.entities.feature.enums.PrimitiveAttributeTypeEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetPlantException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UseCaseAngularFormService {

    String t = StringUtility.space(2);
    String eol = "\n";

    final UseCaseService useCaseService;

    public UseCaseAngularFormService(UseCaseService useCaseService) {
        this.useCaseService = useCaseService;
    }

    public String getFormFields(UseCase useCase, String offset) throws GetPlantException {
        String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
        String content = "";
        UseCaseData plant = useCaseService.getPlant(useCase);
        List<UseCaseDataAttribute> attributes = plant.getUseCaseDataAttributes();
        for (UseCaseDataAttribute attribute : attributes) {
            content += ""
                    + offset + "<div fxLayout.gt-sm='row' fxLayout.lt-md='column' fxLayoutGap.gt-sm='16px' fxLayoutGap.lt-md='8px'>" + eol
                    + offset + t + "<mat-form-field appearance='outline' fxFlex.gt-sm='50%' fxFlex.lt-md='100%'>" + eol
                    + offset + t + t + "<mat-label>" + eol
                    + offset + t + t + t + "{{'" + useCaseTitle + "." + attribute.getName() + "' | translate}}" + eol
                    + offset + t + t + "</mat-label>" + eol
                    + this.getFormFieldInput(useCaseTitle, attribute, offset + t + t, false)
                    + offset + t + "</mat-form-field>" + eol
                    + offset + "</div>" + eol;
        }
        return content;
    }

    public String getFormFieldInput(String useCaseTitle, UseCaseDataAttribute attribute, String offset, boolean isSearchForm) {
        String attVarName = StringUtility.firstLowerCase(attribute.getName());
        String formControlAttributeOfTag = "";
        if (isSearchForm) {
            formControlAttributeOfTag = "[formControl]='" + attVarName + "FormControl'";
        } else {
            if (attribute.isSelectEnum()) {
                formControlAttributeOfTag = "formControlName='" + attVarName + "Enum'";
            } else {
                formControlAttributeOfTag = "formControlName='" + attVarName + "'";
            }
        }
        String content = "";
        if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.Primitive)) {
            if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.String)) {
                content += offset + "<input matInput " + formControlAttributeOfTag + ">" + eol;
            }
            if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.JavaDate)) {
                content += offset + "<input matInput [matDatepicker]='" + attVarName + "Datepicker' " + formControlAttributeOfTag + ">" + eol;
                content += offset + "<mat-datepicker-toggle matSuffix [for]='" + attVarName + "Datepicker'></mat-datepicker-toggle>" + eol;
                content += offset + "<mat-datepicker touchUi #" + attVarName + "Datepicker></mat-datepicker>";
            }
            if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Boolean)) {
                content += offset + "<mat-select " + formControlAttributeOfTag + ">" + eol;
                content += offset + "<mat-option [value]='false'>{{'" + useCaseTitle + ".BooleanNo' | translate}}</mat-option>";
                content += offset + "<mat-option [value]='true'>{{'" + useCaseTitle + ".BooleanYes' | translate}}</mat-option>";
                content += offset + "</mat-select>";
            }
            if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Integer) || attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Long)) {
                content += offset + "<input matInput type='number' " + formControlAttributeOfTag + ">" + eol;
            }
            if (!isSearchForm) {
                content += offset + "<mat-error *ngIf=\"this.reactiveForm.get('" + StringUtility.firstLowerCase(attribute.getName()) + "').invalid\">" + eol;
                content += offset + t + "{{'" + useCaseTitle + "." + attribute.getName() + "Error' | translate}}" + eol;
                content += offset + "</mat-error>" + eol;
            }
        }
        if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEnum)) {
            content += offset + "<mat-select " + formControlAttributeOfTag + ">" + eol;
            content += offset + t + "<mat-option *ngFor='let " + attVarName + "Enum of " + attVarName + "EnumArray' [value]='" + attVarName + "Enum.value'>" + eol;
            content += offset + t + t + "{{" + attVarName + "Enum.title}}" + eol;
            content += offset + t + "</mat-option>" + eol;
            content += offset + "</mat-select>" + eol;
            if (!isSearchForm) {
                content += offset + "<mat-error *ngIf=\"this.reactiveForm.get('" + StringUtility.firstLowerCase(attribute.getName()) + "Enum').invalid\">" + eol;
                content += offset + t + "{{'" + useCaseTitle + "." + attribute.getName() + "Error' | translate}}" + eol;
                content += offset + "</mat-error>" + eol;
            }
        }
        if (attribute.getAttributeCategory().equals(EntityAttributeCategoryEnum.SelectEntity)) {
            content += offset + "<mat-select " + formControlAttributeOfTag + ">" + eol;
            content += offset + t + "<mat-option *ngFor='let " + attVarName + " of " + attVarName + "Array' [value]='" + attVarName + ".value'>" + eol;
            content += offset + t + t + "{{" + attVarName + ".title}}" + eol;
            content += offset + t + "</mat-option>" + eol;
            content += offset + "</mat-select>" + eol;
            if (!isSearchForm) {
                content += offset + "<mat-error *ngIf=\"this.reactiveForm.get('" + StringUtility.firstLowerCase(attribute.getName()) + "').invalid\">" + eol;
                content += offset + t + "{{'" + useCaseTitle + "." + attribute.getName() + "Error' | translate}}" + eol;
                content += offset + "</mat-error>" + eol;
            }
        }
        return content;
    }
}
