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
public class Software {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String springBootProjectPath;

    @Column(nullable = false)
    private String springBootProjectMainPackage;

    @Column(nullable = false)
    private String springBootJavaSrcPath;
}
