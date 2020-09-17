package ir.afarinesh.realhope.modules.software_design.features.use_case.domain;

import ir.afarinesh.realhope.core.annotations.FeatureDomain;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@FeatureDomain
public class UseCase4ProjectManager{
	private Long id; // The id of use case
	private String useCaseName; // The name of use case
	private String title; // The title of use case
	private String faTitle; // The farsi title of use case
	private String description; // The description of use case
	private String userInterfaceType; // The user interface type
	private String softwareName; // The software name
	private String softwareModuleName; // The software module name
	private String softwareFeatureName; // The software feature name
	private String softwareApplicationPanelName; // The software application panel
	private String softwareRole; // The software role
	private String dataEntity; // The data entity
	private Boolean generationEnable; // The generation enable
}