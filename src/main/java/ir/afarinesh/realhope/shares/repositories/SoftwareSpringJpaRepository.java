package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.project.Software;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoftwareSpringJpaRepository extends JpaRepository<Software, Long> {
}
