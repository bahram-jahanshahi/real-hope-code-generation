package ir.afarinesh.realhope.entities.feature;

import ir.afarinesh.realhope.core.domain.AbstractDataEntity;
import ir.afarinesh.realhope.entities.code_generation.CrudCodeGeneration;
import ir.afarinesh.realhope.entities.project.SoftwareApplicationPanel;
import ir.afarinesh.realhope.entities.project.SoftwareFeature;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table
public class DomainEntity extends AbstractDataEntity {

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "domainEntity")
    List<DomainEntityAttribute> domainEntityAttributes;

    @JoinColumn(nullable = false)
    @ManyToOne
    private SoftwareApplicationPanel softwareApplicationPanel;

    @JoinColumn(nullable = false)
    @ManyToOne
    private SoftwareFeature softwareFeature;

    @Column(nullable = false)
    private Boolean generationEnable;

    @JoinColumn(nullable = true)
    @ManyToOne
    private CrudCodeGeneration crudCodeGeneration;

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
}
