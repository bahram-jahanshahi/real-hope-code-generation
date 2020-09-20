package ir.afarinesh.realhope.modules.software_design.features.use_case.adapter.repositories;

import ir.afarinesh.realhope.entities.feature.UseCase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Date;

public interface ListUseCaseByProjectManagerRepositoryImpl extends CrudRepository<UseCase, Long> {
    @Query(value="select Entity from UseCase as Entity where 1 = 1 " +
            ""
    )
    Page<UseCase> findAll(
        Pageable pageable
    );
}