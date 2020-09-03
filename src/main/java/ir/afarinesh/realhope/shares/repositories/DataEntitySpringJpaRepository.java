package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.data_model.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataEntitySpringJpaRepository extends JpaRepository<DataEntity, Long> {
}
