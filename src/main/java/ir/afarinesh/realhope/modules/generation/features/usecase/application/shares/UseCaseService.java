package ir.afarinesh.realhope.modules.generation.features.usecase.application.shares;

import ir.afarinesh.realhope.entities.feature.*;
import ir.afarinesh.realhope.entities.feature.enums.EntityAttributeQuantityEnum;
import ir.afarinesh.realhope.entities.feature.enums.PrimitiveAttributeTypeEnum;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseDataTypeEnum;
import ir.afarinesh.realhope.entities.feature.enums.UseCaseUsageEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetFruitException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetGridListFruitDomainEntityException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetPlantException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetViewDomainEntityException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UseCaseService {

    public DomainEntity getViewDomainEntity(UseCase useCase) throws GetViewDomainEntityException {
        Optional<UseCaseData> first = useCase
                .getUseCaseDataSet()
                .stream()
                .filter(useCaseData -> useCaseData.getUseCaseDataType().equals(UseCaseDataTypeEnum.Fruit))
                .findFirst();
        if (first.isPresent()) {
            Optional<UseCaseDataAttribute> firstAttribute = first
                    .get()
                    .getUseCaseDataAttributes()
                    .stream()
                    .filter(useCaseDataAttribute -> useCaseDataAttribute.getUseCaseUsageEnum().equals(UseCaseUsageEnum.ViewEntity))
                    .findFirst();
            if (firstAttribute.isPresent()) {
                return firstAttribute.get().getDomainEntityAttributeType();
            }
        }
        throw new GetViewDomainEntityException("Cannot find view domain entity for use case = " + useCase.getName());
    }

    public DomainEntity getGridListFruitDomainEntity(UseCase useCase) throws GetGridListFruitDomainEntityException {
        Optional<UseCaseData> first = useCase
                .getUseCaseDataSet()
                .stream()
                .filter(useCaseData -> useCaseData.getUseCaseDataType().equals(UseCaseDataTypeEnum.Fruit))
                .findFirst();
        if (first.isPresent()) {
            Optional<UseCaseDataAttribute> firstAttribute = first
                    .get()
                    .getUseCaseDataAttributes()
                    .stream()
                    .filter(useCaseDataAttribute -> useCaseDataAttribute.getAttributeQuantity().equals(EntityAttributeQuantityEnum.List))
                    .findFirst();
            if (firstAttribute.isPresent()) {
                return firstAttribute.get().getDomainEntityAttributeType();
            }
        }
        throw new GetGridListFruitDomainEntityException("Cannot find grid list fruit domain entity for use case = " + useCase.getName());
    }

    public UseCaseData getFruit(UseCase useCase) throws GetFruitException {
        return useCase.getUseCaseDataSet()
                .stream()
                .filter(useCaseData -> useCaseData.getUseCaseDataType().equals(UseCaseDataTypeEnum.Fruit))
                .findFirst()
                .orElseThrow(() -> new GetFruitException("The use case fruit is not found for use case = " + useCase.getName()));
    }

    public UseCaseData getPlant(UseCase useCase) throws GetPlantException {
        return useCase.getUseCaseDataSet()
                .stream()
                .filter(useCaseData -> useCaseData.getUseCaseDataType().equals(UseCaseDataTypeEnum.Plant))
                .findFirst()
                .orElseThrow(() -> new GetPlantException("The use case plant is not found for use case = " + useCase.getName()));
    }

    public String getDomainEntityMapArguments(DomainEntity domainEntity) {
        String t = StringUtility.space(4);
        String content = "";
        for (int i = 0; i < domainEntity.getDomainEntityAttributes().size(); i++) {
            DomainEntityAttribute attribute = domainEntity.getDomainEntityAttributes().get(i);
            content += t + t + t + t +  this.getMapPathByGetters(attribute.getMapPath(), attribute.getMapPathAttributeType());
            if (i < domainEntity.getDomainEntityAttributes().size() - 1) {
                content += ",";
            }
            content += "\n";
        }
        return content;
    }

    private String getMapPathByGetters(String mapPath, PrimitiveAttributeTypeEnum primitiveAttributeType) {
        String content = "entity.";
        String[] parts = mapPath.split("\\.");
        if (primitiveAttributeType == PrimitiveAttributeTypeEnum.Date) {
            content = "CalendarUtility.format(entity.";
        }
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            content += "get" + part + "()";
            content += (i < parts.length - 1) ? "." : "";
        }
        if (primitiveAttributeType == PrimitiveAttributeTypeEnum.Enum) {
            content += ".name()";
        }
        if (primitiveAttributeType == PrimitiveAttributeTypeEnum.Date) {
            content += ", locale)";
        }
        return content;
    }
}
