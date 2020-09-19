package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.feature.DomainEntityAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainEntityAttributeSpringJpaRepository extends JpaRepository<DomainEntityAttribute, Long> {
    void deleteByDomainEntity_CrudCodeGeneration_Id(Long crudCodeGeneration_Id);
}
