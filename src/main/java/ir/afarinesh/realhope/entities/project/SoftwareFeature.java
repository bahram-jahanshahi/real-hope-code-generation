package ir.afarinesh.realhope.entities.project;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table
public class SoftwareFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // has to be english and camel case

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @JoinColumn(nullable = false)
    @ManyToOne
    private SoftwareModule softwareModule;
}
