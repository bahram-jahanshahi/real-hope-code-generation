package ir.afarinesh.realhope.modules.generation.features.crud.application;

import ir.afarinesh.realhope.core.usecase.UseCasePlant;
import ir.afarinesh.realhope.modules.generation.features.crud.application.ports.in.GenerateCrudByProjectManager;
import ir.afarinesh.realhope.shares.repositories.CrudCodeGenerationSpringJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GenerateCrudByProjectManagerServiceTest {

    @Autowired
    private GenerateCrudByProjectManager generateCrudByProjectManager;
    @Autowired
    private CrudCodeGenerationSpringJpaRepository crudCodeGenerationSpringJpaRepository;

    @Test
    void cultivate() {
        crudCodeGenerationSpringJpaRepository
                .findAll()
                .forEach(crudCodeGeneration -> {
                    try {
                        generateCrudByProjectManager
                                .cultivate(
                                        new UseCasePlant<>(
                                                new GenerateCrudByProjectManager.Plant(crudCodeGeneration.getId()),
                                                "fa"
                                        )
                                );
                    } catch (GenerateCrudByProjectManager.CultivateException e) {
                        e.printStackTrace();
                    }
                });
    }
}
