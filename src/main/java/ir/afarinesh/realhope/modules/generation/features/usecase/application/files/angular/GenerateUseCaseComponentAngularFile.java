package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular;

import ir.afarinesh.realhope.entities.feature.UseCase;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.exceptions.GenerateUseCaseComponentAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.grid_list.GenerateUseCaseGridListComponentAngularFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.grid_list.exceptions.GenerateUseCaseGridListComponentAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.view.GenerateUseCaseViewComponentAngularFile;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.view.exceptions.GenerateUseCaseViewComponentAngularFileException;
import org.springframework.stereotype.Service;

@Service
public class GenerateUseCaseComponentAngularFile {

    final GenerateUseCaseGridListComponentAngularFile generateUseCaseGridListComponentAngularFile;
    final GenerateUseCaseViewComponentAngularFile generateUseCaseViewComponentAngularFile;

    public GenerateUseCaseComponentAngularFile(GenerateUseCaseGridListComponentAngularFile generateUseCaseGridListComponentAngularFile,
                                               GenerateUseCaseViewComponentAngularFile generateUseCaseViewComponentAngularFile) {
        this.generateUseCaseGridListComponentAngularFile = generateUseCaseGridListComponentAngularFile;
        this.generateUseCaseViewComponentAngularFile = generateUseCaseViewComponentAngularFile;
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
    }
}
