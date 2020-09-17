package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.project.SoftwareFeature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoftwareFeatureSpringJpaRepository extends JpaRepository<SoftwareFeature, Long> {
}
