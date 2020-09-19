package ir.afarinesh.realhope.modules.sample.features.sample_b.domain;

import ir.afarinesh.realhope.core.annotations.FeatureDomain;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@FeatureDomain
public class SampleB4ProjectManager{
	private Long id; // The sample B Id
	private String name; // Name
	private Boolean active; // Active
	private String createDate; // CreateDate
	private Long value; // Value
	private String sampleStatus; // SampleStatus
	private String sampleA; // SampleA
}