package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UseCaseDataAttributeSpringJpaRepository extends JpaRepository<UseCaseDataAttribute, Long> {
    void deleteByUseCaseData_UseCase_CrudCodeGeneration_Id(Long crudCodeGeneration_Id);
}
