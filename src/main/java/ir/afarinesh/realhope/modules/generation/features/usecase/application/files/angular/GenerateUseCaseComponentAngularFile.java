package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular;

import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.delete.GenerateUseCaseDeleteComponentAngularFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.delete.exceptions.GenerateUseCaseDeleteComponentAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.exceptions.GenerateUseCaseComponentAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.grid_list.GenerateUseCaseGridListComponentAngularFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.grid_list.exceptions.GenerateUseCaseGridListComponentAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.update.GenerateUseCaseUpdateComponentAngularFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.update.exceptions.GenerateUseCaseUpdateComponentAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.view.GenerateUseCaseViewComponentAngularFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.view.exceptions.GenerateUseCaseViewComponentAngularFileException;
import org.springframework.stereotype.Service;

@Service
public class GenerateUseCaseComponentAngularFile {

    final GenerateUseCaseGridListComponentAngularFile generateUseCaseGridListComponentAngularFile;
    final GenerateUseCaseViewComponentAngularFile generateUseCaseViewComponentAngularFile;
    final GenerateUseCaseUpdateComponentAngularFile generateUseCaseUpdateComponentAngularFile;
    final GenerateUseCaseDeleteComponentAngularFile generateUseCaseDeleteComponentAngularFile;

    public GenerateUseCaseComponentAngularFile(GenerateUseCaseGridListComponentAngularFile generateUseCaseGridListComponentAngularFile,
                                               GenerateUseCaseViewComponentAngularFile generateUseCaseViewComponentAngularFile,
                                               GenerateUseCaseUpdateComponentAngularFile generateUseCaseUpdateComponentAngularFile,
                                               GenerateUseCaseDeleteComponentAngularFile generateUseCaseDeleteComponentAngularFile) {
        this.generateUseCaseGridListComponentAngularFile = generateUseCaseGridListComponentAngularFile;
        this.generateUseCaseViewComponentAngularFile = generateUseCaseViewComponentAngularFile;
        this.generateUseCaseUpdateComponentAngularFile = generateUseCaseUpdateComponentAngularFile;
        this.generateUseCaseDeleteComponentAngularFile = generateUseCaseDeleteComponentAngularFile;
    }

    public void generate(UseCase useCase) throws GenerateUseCaseComponentAngularFileException {

        // Grid List Entities
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
            try {
                this.generateUseCaseGridListComponentAngularFile.generate(useCase);
            } catch (GenerateUseCaseGridListComponentAngularFileException e) {
                throw new GenerateUseCaseComponentAngularFileException(e.getMessage());
            }
        }
        // View Entity
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.View)) {
            try {
                this.generateUseCaseViewComponentAngularFile.generate(useCase);
            } catch (GenerateUseCaseViewComponentAngularFileException e) {
                throw new GenerateUseCaseComponentAngularFileException(e.getMessage());
            }
        }
        // Update Entity
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.Update)) {
            try {
                this.generateUseCaseUpdateComponentAngularFile.generate(useCase);
            } catch (GenerateUseCaseUpdateComponentAngularFileException e) {
                throw new GenerateUseCaseComponentAngularFileException(e.getMessage());
            }

        }
        // AddNew Entity (same as update!)
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.AddNew)) {
            try {
                this.generateUseCaseUpdateComponentAngularFile.generate(useCase);
            } catch (GenerateUseCaseUpdateComponentAngularFileException e) {
                throw new GenerateUseCaseComponentAngularFileException(e.getMessage());
            }

        }

        // Delete
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.Delete)) {
            try {
                this.generateUseCaseDeleteComponentAngularFile.generate(useCase);
            } catch (GenerateUseCaseDeleteComponentAngularFileException e) {
                throw new GenerateUseCaseComponentAngularFileException(e.getMessage());
            }

        }
    }
}
