package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java;

import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.exceptions.GenerateUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.GenerateDeleteUseCaseServiceJavaFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.GenerateListUseCaseServiceJavaFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.GenerateUpdateUseCaseServiceJavaFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.GenerateViewUseCaseServiceJavaFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.exceptions.GenerateDeleteUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.exceptions.GenerateListUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.exceptions.GenerateUpdateUseCaseServiceJavaFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.java.usecase_service.exceptions.GenerateViewUseCaseServiceJavaFileException;
import org.springframework.stereotype.Service;

@Service
public class GenerateUseCaseServiceJavaFile {

    final GenerateListUseCaseServiceJavaFile generateList;
    final GenerateViewUseCaseServiceJavaFile generateView;
    final GenerateUpdateUseCaseServiceJavaFile generateUpdate;
    final GenerateDeleteUseCaseServiceJavaFile generateDelete;

    public GenerateUseCaseServiceJavaFile(GenerateListUseCaseServiceJavaFile generateList,
                                          GenerateViewUseCaseServiceJavaFile generateView,
                                          GenerateUpdateUseCaseServiceJavaFile generateUpdate,
                                          GenerateDeleteUseCaseServiceJavaFile generateDelete) {
        this.generateList = generateList;
        this.generateView = generateView;
        this.generateUpdate = generateUpdate;
        this.generateDelete = generateDelete;
    }

    public void generate(UseCase useCase) throws GenerateUseCaseServiceJavaFileException {
        // Grid List
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
            try {
                this.generateList.generate(useCase);
            } catch (GenerateListUseCaseServiceJavaFileException e) {
                throw new GenerateUseCaseServiceJavaFileException(e.getMessage());
            }
        }
        // View
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.View)) {
            try {
                this.generateView.generate(useCase);
            } catch (GenerateViewUseCaseServiceJavaFileException e) {
                throw new GenerateUseCaseServiceJavaFileException(e.getMessage());
            }
        }
        // Update
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.Update)) {
            try {
                this.generateUpdate.generate(useCase);
            } catch (GenerateUpdateUseCaseServiceJavaFileException e) {
                throw new GenerateUseCaseServiceJavaFileException(e.getMessage());
            }
        }
        // AddNew
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.AddNew)) {
            try {
                this.generateUpdate.generate(useCase);
            } catch (GenerateUpdateUseCaseServiceJavaFileException e) {
                throw new GenerateUseCaseServiceJavaFileException(e.getMessage());
            }
        }
        // Delete
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.Delete)) {
            try {
                this.generateDelete.generate(useCase);
            } catch (GenerateDeleteUseCaseServiceJavaFileException e) {
                throw new GenerateUseCaseServiceJavaFileException(e.getMessage());
            }
        }
    }
}
