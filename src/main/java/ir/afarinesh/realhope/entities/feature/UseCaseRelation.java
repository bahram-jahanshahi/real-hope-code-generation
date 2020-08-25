package ir.afarinesh.realhope.entities.feature;

import ir.afarinesh.realhope.entities.feature.enums.FrontendActionTypeEnum;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseRelationContextEnum;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table
public class UseCaseRelation {

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
    @Enumerated(EnumType.STRING)
    private UseCaseRelationContextEnum relationContext; // Backend, Frontend

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FrontendActionTypeEnum frontendActionType; // PopupForm

    @JoinColumn(nullable = false)
    @ManyToOne
    private UseCase source;

    @JoinColumn(nullable = false)
    @ManyToOne
    private UseCase destination;
}
