package ir.afarinesh.realhope.modules.software_design.features.software.domain;

import ir.afarinesh.realhope.core.annotations.FeatureDomain;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@FeatureDomain
public class Software4ProjectManager{
	private Long id; // The id of software
	private String name; // The name of software
	private String title; // The title of software
}