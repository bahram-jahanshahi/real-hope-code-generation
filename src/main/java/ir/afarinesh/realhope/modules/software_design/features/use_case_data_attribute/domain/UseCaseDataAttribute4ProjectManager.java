package ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.domain;

import ir.afarinesh.realhope.core.annotations.FeatureDomain;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@FeatureDomain
public class UseCaseDataAttribute4ProjectManager{
	private Long id; // The id of use case data attribute
	private String useCaseName; // The name of use case
	private String useCaseDataName; // The name of use case data
	private String name; // The name of use case data attribute
	private String title; // The title of use case data attribute
	private String faTitle; // The farsi title of use case data attribute
	private String description; // The description of use case data attribute
	private String useCaseUsageEnum; // The use case usage
	private String attributeQuantity; // The attribute quantity
	private String attributeCategory; // The attribute category
	private String primitiveAttributeType; // The primitive attribute
	private String setterOfUpdatePath; // The setter of update path
	private String getterOfUpdatePath; // The getter of update path
	private String domainEntityAttributeType; // The domain entity attribute type
	private String dataEntityAttributeType; // The data entity attribute type
	private String useCaseData; // The use case data
	private String fruitSeedsAttribute; // The fruit seeds attribute
	private String dataEnum; // The data enum
	private Boolean nullable; // nullable
	private Boolean required; // required
	private Long minLength; // minLength
	private Long maxLength; // maxLength
	private Long min; // min
	private Long max; // max
	private String errorTip; // errorTip
}