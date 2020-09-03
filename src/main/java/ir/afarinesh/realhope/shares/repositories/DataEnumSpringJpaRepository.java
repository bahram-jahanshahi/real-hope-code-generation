package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.data_model.DataEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataEnumSpringJpaRepository extends JpaRepository<DataEnum, Long> {
}
