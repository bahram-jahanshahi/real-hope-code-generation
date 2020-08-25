package ir.afarinesh.realhope.modules.generation.features.usecase.application.shares;

import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
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
                    + this.getFormFieldInput(useCaseTitle, attribute, offset + t + t)
                    + offset + t + t + "<mat-error *ngIf=\"this.reactiveForm.get('" + StringUtility.firstLowerCase(attribute.getName()) + "').invalid\">" + eol
                    + offset + t + t + t + "{{'" + useCaseTitle + "." + attribute.getName() + "Error' | translate}}" + eol
                    + offset + t + t + "</mat-error>" + eol
                    + offset + t + "</mat-form-field>" + eol
                    + offset + "</div>" + eol;
        }
        return content;
    }

    protected String getFormFieldInput(String useCaseTitle, UseCaseDataAttribute attribute, String offset) {
        String content = "";
        String attVarName = StringUtility.firstLowerCase(attribute.getName());
        if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.String)) {
            content += offset + "<input matInput formControlName='" + attVarName + "'>" + eol;
        }
        if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.JavaDate)) {
            content += offset + "<input matInput [matDatepicker]='" + attVarName + "Datepicker' formControlName='" + attVarName + "'>" + eol;
            content += offset + "<mat-datepicker-toggle matSuffix [for]='" + attVarName + "Datepicker'></mat-datepicker-toggle>" + eol;
            content += offset + "<mat-datepicker touchUi #" + attVarName + "Datepicker></mat-datepicker>";
        }
        if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Boolean)) {
            content += offset + "<mat-select formControlName='" + attVarName + "'>" + eol;
            content += offset + "<mat-option [value]='false'>{{'" + useCaseTitle + ".BooleanNo' | translate}}</mat-option>";
            content += offset + "<mat-option [value]='true'>{{'" + useCaseTitle + ".BooleanYes' | translate}}</mat-option>";
            content += offset + "</mat-select>";
        }
        if (attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Integer) || attribute.getPrimitiveAttributeType().equals(PrimitiveAttributeTypeEnum.Long)) {
            content += offset + "<input matInput type='number' formControlName='" + attVarName + "'>" + eol;
        }

        return content;
    }
}
