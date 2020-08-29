package ir.afarinesh.realhope.shares.services;

import ir.afarinesh.realhope.entities.code_generation.CodeGenerationConfig;
import ir.afarinesh.realhope.entities.code_generation.enums.CodeGenerationConfigTypeEnum;
import ir.afarinesh.realhope.shares.repositories.CodeGenerationConfigSpringJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CodeGenerationConfigService {
    
    final CodeGenerationConfigSpringJpaRepository repository;

    public CodeGenerationConfigService(CodeGenerationConfigSpringJpaRepository repository) {
        this.repository = repository;
    }

    public boolean isSpringBootServiceGenerationEnabled(Long useCaseId) {
        Optional<CodeGenerationConfig> configType = repository
                .findFirstByUseCaseIdAndCodeGenerationConfigType(useCaseId, CodeGenerationConfigTypeEnum.GenerateUseCaseSpringBootServiceEnabled);
        return configType
                .map(codeGenerationConfig -> codeGenerationConfig.getValue() != null && codeGenerationConfig.getValue().equals("1"))
                .orElse(true);
    }
}
