package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.feature.UseCaseRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UseCaseRelationSpringJpaRepository extends JpaRepository<UseCaseRelation, Long> {
    void deleteByCrudCodeGeneration_Id(Long crudCodeGeneration_Id);
}
