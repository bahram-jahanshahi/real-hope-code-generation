package ir.afarinesh.realhope.entities.feature;

import ir.afarinesh.realhope.core.domain.AbstractDataEntity;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseDataTypeEnum;
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
public class UseCaseData extends AbstractDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // has to be english and camel case

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UseCaseDataTypeEnum useCaseDataType; // Plant, Fruit, FruitSeeds, SeedsCommand

    @Column(nullable = false)
    private String description;

    @JoinColumn(nullable = false)
    @ManyToOne
    private UseCase useCase;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "useCaseData")
    private List<UseCaseDataAttribute> useCaseDataAttributes;

    @Override
    public String title(String locale) {
        return getUseCase().getName() + "." + this.getName();
    }
}
