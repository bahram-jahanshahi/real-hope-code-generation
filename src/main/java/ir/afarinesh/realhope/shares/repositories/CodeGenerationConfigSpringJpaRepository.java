package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.code_generation.CodeGenerationConfig;
import ir.afarinesh.realhope.entities.code_generation.enums.CodeGenerationConfigTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeGenerationConfigSpringJpaRepository extends JpaRepository<CodeGenerationConfig, Long> {
    Optional<CodeGenerationConfig> findFirstByUseCaseIdAndCodeGenerationConfigType(Long useCaseId, CodeGenerationConfigTypeEnum codeGenerationConfigType);
}
