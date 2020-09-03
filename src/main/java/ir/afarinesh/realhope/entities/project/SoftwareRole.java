package ir.afarinesh.realhope.entities.project;

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
public class SoftwareRole extends AbstractDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String faTitle;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Software software;

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
