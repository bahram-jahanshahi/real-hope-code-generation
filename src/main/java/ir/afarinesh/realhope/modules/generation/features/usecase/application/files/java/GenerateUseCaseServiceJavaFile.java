package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java;

import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.exceptions.GenerateUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.GenerateListUseCaseServiceJavaFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.GenerateViewUseCaseServiceJavaFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.exceptions.GenerateListUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.exceptions.GenerateViewUseCaseServiceJavaFileException;
import org.springframework.stereotype.Service;

@Service
public class GenerateUseCaseServiceJavaFile {

    final GenerateListUseCaseServiceJavaFile generateList;
    final GenerateViewUseCaseServiceJavaFile generateView;

    public GenerateUseCaseServiceJavaFile(GenerateListUseCaseServiceJavaFile generateList,
                                          GenerateViewUseCaseServiceJavaFile generateView) {
        this.generateList = generateList;
        this.generateView = generateView;
    }

    public void generate(UseCase useCase) throws GenerateUseCaseServiceJavaFileException {
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
            try {
                this.generateList.generate(useCase);
            } catch (GenerateListUseCaseServiceJavaFileException e) {
                throw new GenerateUseCaseServiceJavaFileException(e.getMessage());
            }
        }
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.View)) {
            try {
                this.generateView.generate(useCase);
            } catch (GenerateViewUseCaseServiceJavaFileException e) {
                throw new GenerateUseCaseServiceJavaFileException(e.getMessage());
            }
        }
    }
}
