package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.sample.SampleA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleASpringJpaRepository extends JpaRepository<SampleA, Long> {
}
