package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.feature.UseCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UseCaseSpringJpaRepository extends JpaRepository<UseCase, Long> {
    List<UseCase> findAllBySoftwareApplicationPanel_Id(Long panelId);
}
