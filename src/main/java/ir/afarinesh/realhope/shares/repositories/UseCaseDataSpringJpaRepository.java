package ir.afarinesh.realhope.shares.repositories;

import ir.afarinesh.realhope.entities.feature.UseCaseData;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseDataTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UseCaseDataSpringJpaRepository extends JpaRepository<UseCaseData, Long> {
    void deleteByUseCase_CrudCodeGeneration_Id(Long crudCodeGeneration_Id);
    UseCaseData findFirstByUseCase_IdAndUseCaseDataType(Long useCase_Id, UseCaseDataTypeEnum useCaseDataType);
}
