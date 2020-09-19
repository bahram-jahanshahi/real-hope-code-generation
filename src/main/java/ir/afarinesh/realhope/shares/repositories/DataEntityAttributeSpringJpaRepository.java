package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.data_model.DataEntityAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataEntityAttributeSpringJpaRepository extends JpaRepository<DataEntityAttribute, Long> {
    List<DataEntityAttribute> findByDataEntity_Id(Long id);
}
