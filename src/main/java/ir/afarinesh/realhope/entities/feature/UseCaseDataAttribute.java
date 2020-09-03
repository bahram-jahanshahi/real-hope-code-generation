package ir.afarinesh.realhope.entities.feature;

import ir.afarinesh.realhope.core.domain.AbstractDataEntity;
import ir.afarinesh.realhope.entities.data_model.DataEntity;
import ir.afarinesh.realhope.entities.data_model.DataEnum;
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
public class UseCaseDataAttribute extends AbstractDataEntity {

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
    private Long uiRow;

    @Column(nullable = false)
    private Long uiColumn;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UseCaseUsageEnum useCaseUsageEnum; // ViewId, ViewEntity, GridListEntity

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityAttributeQuantityEnum attributeQuantity; // Mono, List;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityAttributeCategoryEnum attributeCategory; //Primitive, DomainEntity, SelectEntity, SelectEnum;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private PrimitiveAttributeTypeEnum primitiveAttributeType; // if attribute type is primitive

    @Column(nullable = true)
    private String setterOfUpdatePath;

    @Column(nullable = true)
    private String getterOfUpdatePath;

    @Column(nullable = false)
    private Boolean nullable; // validation

    @Column(nullable = false)
    private Boolean required; // validation

    @Column(nullable = true)
    private Long minLength; // validation

    @Column(nullable = true)
    private Long maxLength; // validation

    @Column(nullable = true)
    private Long min; // validation

    @Column(nullable = true)
    private Long max; // validation

    @Column(nullable = true)
    private String errorTip; // validation

    @JoinColumn(nullable = true)
    @ManyToOne
    private DomainEntity domainEntityAttributeType; // if attribute type is a domain entity

    @JoinColumn(nullable = true)
    @ManyToOne
    private DataEntity dataEntityAttributeType; // if attribute type is a select entity

    @JoinColumn(nullable = false)
    @ManyToOne
    private UseCaseData useCaseData;

    @JoinColumn(nullable = true)
    @ManyToOne
    private UseCaseDataAttribute fruitSeedsAttribute;

    @JoinColumn(nullable = true)
    @ManyToOne
    private DataEnum dataEnum;

    @Override
    public String title(String locale) {
        if (locale.equals("fa")) {
            return getFaTitle();
        }
        if (locale.equals("en")) {
            return getTitle();
        }
        return getName();
    }

    // Utilities
    public boolean isPrimitive() {
        return attributeCategory.equals(EntityAttributeCategoryEnum.Primitive);
    }

    public boolean isDomainEntity() {
        return attributeCategory.equals(EntityAttributeCategoryEnum.DomainEntity);
    }

    public boolean isSelectEntity() {
        return attributeCategory.equals(EntityAttributeCategoryEnum.SelectEntity);
    }

    public boolean isSelectEnum() {
        return attributeCategory.equals(EntityAttributeCategoryEnum.SelectEnum);
    }
}
