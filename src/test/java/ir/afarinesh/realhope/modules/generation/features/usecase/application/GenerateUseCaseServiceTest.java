package ir.afarinesh.realhope.modules.generation.features.usecase.application;

import ir.afarinesh.realhope.core.usecase.UseCaseFruit;
import ir.afarinesh.realhope.core.usecase.UseCasePlant;
import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.ports.in.GenerateUseCase;
import ir.afarinesh.realhope.shares.repositories.UseCaseSpringJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GenerateUseCaseServiceTest {

    @Autowired
    GenerateUseCase generateUseCase;

    @Autowired
    UseCaseSpringJpaRepository useCaseSpringJpaRepository;

    @Test
    void cultivate() {
        List<UseCase> useCaseList = useCaseSpringJpaRepository.findAll();
        for (int i = 0; i < useCaseList.size(); i++) {
            try {
                UseCaseFruit<GenerateUseCase.Fruit> fruit = generateUseCase
                        .cultivate(
                                new UseCasePlant<>(
                                        new GenerateUseCase.Plant(useCaseList.get(i).getId()),
                                        "fa"
                                )
                        );
                assertTrue(fruit.getIsSuccessful());
            } catch (GenerateUseCase.CultivateException e) {
                e.printStackTrace();
            }
        }
    }
}
