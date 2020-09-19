package ir.afarinesh.realhope.entities.feature;

import ir.afarinesh.realhope.core.domain.AbstractDataEntity;
import ir.afarinesh.realhope.entities.code_generation.CrudCodeGeneration;
import ir.afarinesh.realhope.entities.data_model.DataEntity;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
import ir.afarinesh.realhope.entities.project.SoftwareApplicationPanel;
import ir.afarinesh.realhope.entities.project.SoftwareFeature;
import ir.afarinesh.realhope.entities.project.SoftwareRole;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table
public class UseCase extends AbstractDataEntity {
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
    private UserInterfaceTypeEnum userInterfaceType;

    @JoinColumn(nullable = false)
    @ManyToOne
    private SoftwareFeature softwareFeature;

    @JoinColumn(nullable = false)
    @ManyToOne
    private SoftwareApplicationPanel softwareApplicationPanel;

    @JoinColumn(nullable = false)
    @ManyToOne
    private SoftwareRole softwareRole;

    @JoinColumn(nullable = false)
    @ManyToOne
    private DataEntity dataEntity;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "useCase")
    private Set<UseCaseData> useCaseDataSet;

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
