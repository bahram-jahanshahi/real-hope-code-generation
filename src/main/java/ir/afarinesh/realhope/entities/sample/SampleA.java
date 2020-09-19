package ir.afarinesh.realhope.entities.sample;

import ir.afarinesh.realhope.core.domain.AbstractDataEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table
public class SampleA extends AbstractDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Override
    public String title(String locale) {
        return getName();
    }
}
