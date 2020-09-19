package ir.afarinesh.realhope.entities.sample;

import ir.afarinesh.realhope.core.domain.AbstractDataEntity;
import ir.afarinesh.realhope.entities.sample.enums.SampleStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table
public class SampleB extends AbstractDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Date createDate;

    @Column(nullable = false)
    private Long value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SampleStatusEnum sampleStatus;

    @JoinColumn(nullable = false)
    @ManyToOne
    private SampleA sampleA;

    @Override
    public String title(String locale) {
        return getName();
    }
}
