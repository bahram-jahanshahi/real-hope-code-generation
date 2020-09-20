package ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.adapter.repositories;

import ir.afarinesh.realhope.entities.feature.UseCaseDataAttribute;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Date;

public interface ListUseCaseDataAttributeByProjectManagerRepositoryImpl extends CrudRepository<UseCaseDataAttribute, Long> {
    @Query(value="select Entity from UseCaseDataAttribute as Entity where 1 = 1 " +
            ""
    )
    Page<UseCaseDataAttribute> findAll(
        Pageable pageable
    );
}