package ir.afarinesh.realhope.shares.services;

import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.UseCaseRelation;
import ir.afarinesh.realhope.shares.repositories.UseCaseRelationSpringJpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UseCaseRelationService {
    final UseCaseRelationSpringJpaRepository useCaseRelationSpringJpaRepository;
    List<UseCaseRelation> allUseCaseRelations = new ArrayList<>();

    public UseCaseRelationService(UseCaseRelationSpringJpaRepository useCaseRelationSpringJpaRepository) {
        this.useCaseRelationSpringJpaRepository = useCaseRelationSpringJpaRepository;
        this.allUseCaseRelations = this.useCaseRelationSpringJpaRepository.findAll();
    }

    public List<UseCaseRelation> findAllBySource(UseCase source) {
        return allUseCaseRelations
                .stream()
                .filter(useCaseRelation -> useCaseRelation.getSource().getId().equals(source.getId()))
                .collect(Collectors.toList());
    }
}
