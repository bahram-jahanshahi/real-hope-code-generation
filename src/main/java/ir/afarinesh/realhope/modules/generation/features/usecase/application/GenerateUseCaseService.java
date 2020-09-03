package ir.afarinesh.realhope.modules.generation.features.usecase.application;

import ir.afarinesh.realhope.core.usecase.UseCaseFruit;
import ir.afarinesh.realhope.core.usecase.UseCaseFruitSeeds;
import ir.afarinesh.realhope.core.usecase.UseCasePlant;
import ir.afarinesh.realhope.core.usecase.UseCaseSeedsCommand;
import ir.afarinesh.realhope.entities.feature.DomainEntity;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.GenerateDictionaryAngularFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.GenerateFeatureDomainAngularFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.GenerateUseCaseComponentAngularFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.GenerateUseCaseServiceAngularFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.exceptions.GenerateDictionaryAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.exceptions.GenerateFeatureDomainAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.exceptions.GenerateUseCaseComponentAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.exceptions.GenerateUseCaseServiceAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.GenerateFeatureDomainJavaFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.GenerateUseCaseInterfaceJavaFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.GenerateUseCaseRestControllerJavaFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.GenerateUseCaseServiceJavaFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.exceptions.GenerateFeatureDomainJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.exceptions.GenerateUseCaseInterfaceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.exceptions.GenerateUseCaseRestControllerJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.exceptions.GenerateUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.ports.in.GenerateUseCase;
import ir.afarinesh.realhope.shares.repositories.DomainEntitySpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.UseCaseSpringJpaRepository;
import ir.afarinesh.realhope.shares.services.CodeGenerationConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateUseCaseService implements GenerateUseCase {
    final CodeGenerationConfigService codeGenerationConfigService;
    final UseCaseSpringJpaRepository useCaseSpringJpaRepository;
    final DomainEntitySpringJpaRepository domainEntitySpringJpaRepository;
    final GenerateUseCaseInterfaceJavaFile generateUseCaseInterfaceJavaFile;
    final GenerateUseCaseRestControllerJavaFile generateUseCaseRestControllerJavaFile;
    final GenerateFeatureDomainJavaFile generateFeatureDomainJavaFile;
    final GenerateUseCaseServiceAngularFile generateUseCaseServiceAngularFile;
    final GenerateFeatureDomainAngularFile generateFeatureDomainAngularFile;
    final GenerateUseCaseComponentAngularFile generateUseCaseComponentAngularFile;
    final GenerateUseCaseServiceJavaFile generateUseCaseServiceJavaFile;
    final GenerateDictionaryAngularFile generateDictionaryAngularFile;

    public GenerateUseCaseService(CodeGenerationConfigService codeGenerationConfigService,
                                  UseCaseSpringJpaRepository useCaseSpringJpaRepository,
                                  DomainEntitySpringJpaRepository domainEntitySpringJpaRepository,
                                  GenerateUseCaseInterfaceJavaFile generateUseCaseInterfaceJavaFile,
                                  GenerateUseCaseRestControllerJavaFile generateUseCaseRestControllerJavaFile,
                                  GenerateFeatureDomainJavaFile generateFeatureDomainJavaFile,
                                  GenerateUseCaseServiceAngularFile generateUseCaseServiceAngularFile,
                                  GenerateFeatureDomainAngularFile generateFeatureDomainAngularFile,
                                  GenerateUseCaseComponentAngularFile generateUseCaseComponentAngularFile,
                                  GenerateUseCaseServiceJavaFile generateUseCaseServiceJavaFile,
                                  GenerateDictionaryAngularFile generateDictionaryAngularFile) {
        this.codeGenerationConfigService = codeGenerationConfigService;
        this.useCaseSpringJpaRepository = useCaseSpringJpaRepository;
        this.domainEntitySpringJpaRepository = domainEntitySpringJpaRepository;
        this.generateUseCaseInterfaceJavaFile = generateUseCaseInterfaceJavaFile;
        this.generateUseCaseRestControllerJavaFile = generateUseCaseRestControllerJavaFile;
        this.generateFeatureDomainJavaFile = generateFeatureDomainJavaFile;
        this.generateUseCaseServiceAngularFile = generateUseCaseServiceAngularFile;
        this.generateFeatureDomainAngularFile = generateFeatureDomainAngularFile;
        this.generateUseCaseComponentAngularFile = generateUseCaseComponentAngularFile;
        this.generateUseCaseServiceJavaFile = generateUseCaseServiceJavaFile;
        this.generateDictionaryAngularFile = generateDictionaryAngularFile;
    }

    @Override
    public UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException {
        List<UseCase> useCaseList = useCaseSpringJpaRepository.findAll();
        List<DomainEntity> domainEntityList = domainEntitySpringJpaRepository.findAll();
        for (UseCase useCase : useCaseList) {
            if (useCase.getId() > 0L && useCase.getSoftwareFeature().getSoftwareModule().getSoftware().getId() == 2L) {
                System.out.println("Try to generate use case: " + useCase.getName());
                try {
                    // generate use case interface java
                    this.generateUseCaseInterfaceJavaFile.generate(useCase);
                    // generate rest controller java
                    this.generateUseCaseRestControllerJavaFile.generate(useCase);
                    // generate use case service java
                    this.generateUseCaseServiceJavaFile.generate(useCase);
                    // generate use case angular cli service
                    this.generateUseCaseServiceAngularFile.generate(useCase);
                    // generate component for angular cli
                    this.generateUseCaseComponentAngularFile.generate(useCase);
                    // generate dictionary for angular cli
                    this.generateDictionaryAngularFile.generate();
                } catch (GenerateUseCaseInterfaceJavaFileException | GenerateUseCaseRestControllerJavaFileException | GenerateUseCaseServiceAngularFileException | GenerateUseCaseComponentAngularFileException | GenerateUseCaseServiceJavaFileException | GenerateDictionaryAngularFileException e) {
                    throw new CultivateException(e.getMessage());
                }
                System.out.println("Done.");
            }
        }

        for (DomainEntity domainEntity : domainEntityList) {
            try {
                // generate domain entity java
                this.generateFeatureDomainJavaFile.generate(domainEntity);
                // generate domain entity for angular cli
                this.generateFeatureDomainAngularFile.generate(domainEntity);
            } catch (GenerateFeatureDomainJavaFileException | GenerateFeatureDomainAngularFileException e) {
                e.printStackTrace();
            }
        }

        return new UseCaseFruit<>(
                new Fruit(true),
                true,
                ""
        );
    }

    @Override
    public UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException {
        return null;
    }
}
