package ir.afarinesh.realhope.entities.code_generation;

import ir.afarinesh.realhope.entities.code_generation.enums.CodeGenerationConfigTypeEnum;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table
public class CodeGenerationConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long useCaseId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CodeGenerationConfigTypeEnum codeGenerationConfigType;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(nullable = true)
    private CodeGenerationConfigContent codeGenerationConfigContent;
}
