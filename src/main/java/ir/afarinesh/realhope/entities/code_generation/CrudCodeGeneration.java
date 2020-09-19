package ir.afarinesh.realhope.entities.code_generation;

import ir.afarinesh.realhope.core.domain.AbstractDataEntity;
import ir.afarinesh.realhope.entities.data_model.DataEntity;
import ir.afarinesh.realhope.entities.project.SoftwareApplicationPanel;
import ir.afarinesh.realhope.entities.project.SoftwareFeature;
import ir.afarinesh.realhope.entities.project.SoftwareRole;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table
public class CrudCodeGeneration extends AbstractDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @JoinColumn(nullable = false)
    @ManyToOne
    private DataEntity dataEntity;

    @JoinColumn(nullable = false)
    @ManyToOne
    private SoftwareRole softwareRole;

    @JoinColumn(nullable = false)
    @ManyToOne
    private SoftwareFeature softwareFeature;

    @JoinColumn(nullable = false)
    @ManyToOne
    private SoftwareApplicationPanel softwareApplicationPanel;

    @Override
    public String title(String locale) {
        return getTitle();
    }
}
