package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.feature.DomainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DomainEntitySpringJpaRepository extends JpaRepository<DomainEntity, Long> {
    List<DomainEntity> findAllBySoftwareFeature_Id(Long softwareFeatureId);
}
