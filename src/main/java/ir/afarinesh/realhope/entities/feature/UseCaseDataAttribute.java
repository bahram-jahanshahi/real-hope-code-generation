package ir.afarinesh.realhope.entities.feature;

import ir.afarinesh.realhope.entities.feature.enums.PrimitiveAttributeTypeEnum;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeCategoryEnum;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeQuantityEnum;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseUsageEnum;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table
public class UseCaseDataAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // has to be english and camel case

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String faTitle;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UseCaseUsageEnum useCaseUsageEnum; // ViewId, ViewEntity, GridListEntity

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityAttributeQuantityEnum attributeQuantity; // Mono, List;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityAttributeCategoryEnum attributeCategory; // Primitive, DomainEntity

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private PrimitiveAttributeTypeEnum primitiveAttributeType; // if attribute type is primitive

    @JoinColumn(nullable = true)
    @ManyToOne
    private DomainEntity domainEntityAttributeType; // if attribute type is a domain entity

    @JoinColumn(nullable = false)
    @ManyToOne
    private UseCaseData useCaseData;
}
