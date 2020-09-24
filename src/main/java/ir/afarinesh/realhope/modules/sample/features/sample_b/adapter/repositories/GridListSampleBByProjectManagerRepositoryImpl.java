package ir.afarinesh.realhope.modules.sample.features.sample_b.adapter.repositories;

import ir.afarinesh.realhope.entities.sample.SampleB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;

public interface GridListSampleBByProjectManagerRepositoryImpl extends CrudRepository<SampleB, Long> {
    @Query(value="select Entity from SampleB as Entity where 1 = 1 " +
            " and ( :id is null or Entity.id = :id ) " +
            " and ( :name is null or Entity.name like %:name% ) " +
            " and ( :active is null or Entity.active = :active ) " +
            " and ( :createDateBegin is null or (Entity.createDate >= :createDateBegin)) " +
            " and ( :createDateEnd is null or (Entity.createDate <= :createDateEnd)) " +
            " and ( :valueBegin is null or (Entity.value >= :valueBegin)) " +
            " and ( :valueEnd is null or (Entity.value <= :valueEnd)) " +
            " and ( :sampleStatus is null or Entity.sampleStatus = :sampleStatus ) " +
            " and ( :sampleA is null or Entity.sampleA.id = :sampleA ) " +
            ""
    )
    Page<SampleB> findAll(
        @Param("id") Long id,
        @Param("name") String name,
        @Param("active") Boolean active,
        @Param("createDateBegin") Date createDateBegin,
        @Param("createDateEnd") Date createDateEnd,
        @Param("valueBegin") Long valueBegin,
        @Param("valueEnd") Long valueEnd,
        @Param("sampleStatus") String sampleStatus,
        @Param("sampleA") Long sampleA,
        Pageable pageable
    );
}