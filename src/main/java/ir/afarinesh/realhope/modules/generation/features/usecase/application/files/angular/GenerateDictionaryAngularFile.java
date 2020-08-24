package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular;

import ir.afarinesh.realhope.entities.feature.*;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
import ir.afarinesh.realhope.entities.project.SoftwareApplicationPanel;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.exceptions.GenerateDictionaryAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetGridListFruitDomainEntityException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetPlantException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetViewDomainEntityException;
import ir.afarinesh.realhope.shares.repositories.SoftwareApplicationPanelSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.UseCaseSpringJpaRepository;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateDictionaryAngularFile {
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final UseCaseService useCaseService;
    final SoftwareApplicationPanelSpringJpaRepository softwareApplicationPanelSpringJpaRepository;
    final UseCaseSpringJpaRepository useCaseSpringJpaRepository;

    public GenerateDictionaryAngularFile(FileManagementService fileManagementService,
                                         UseCasePathService useCasePathService,
                                         UseCaseService useCaseService,
                                         SoftwareApplicationPanelSpringJpaRepository softwareApplicationPanelSpringJpaRepository,
                                         UseCaseSpringJpaRepository useCaseSpringJpaRepository) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.useCaseService = useCaseService;
        this.softwareApplicationPanelSpringJpaRepository = softwareApplicationPanelSpringJpaRepository;
        this.useCaseSpringJpaRepository = useCaseSpringJpaRepository;
    }

    public void generate() throws GenerateDictionaryAngularFileException {
        this.softwareApplicationPanelSpringJpaRepository
                .findAll()
                .forEach(softwareApplicationPanel -> {
                    try {
                        fileManagementService
                                .createFile(
                                        this.getPath(softwareApplicationPanel),
                                        this.getFileName(),
                                        this.getContent(softwareApplicationPanel)
                                );
                    } catch (CreateFileException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                });
    }

    private String getPath(SoftwareApplicationPanel panel) {
        String pathSeparator = fileManagementService.pathSeparator();
        return useCasePathService.getAngularCliAssetsPath(panel) + pathSeparator
                + "i18n" + pathSeparator;
    }

    private String getFileName() {
        return "fa.gen.json";
    }

    private String getContent(SoftwareApplicationPanel panel) {
        String t = StringUtility.space(2);
        List<UseCase> useCaseList = this.useCaseSpringJpaRepository.findAllBySoftwareApplicationPanel_Id(panel.getId());
        String dictionaryContent = "{" + "\n";
        for (int i = 0; i < useCaseList.size(); i++) {
            UseCase useCase = useCaseList.get(i);
            String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
            dictionaryContent += ""
                    + t + "\"" + useCaseTitle + "\": {" + "\n"
                    + t + t + "\"" + "Title" + "\": " + "\"" + useCase.getFaTitle() + "\"" + "\n"
                    + this.getGridListDictionary(useCase)
                    + this.getViewDictionary(useCase)
                    + t + "}";
            dictionaryContent += (i < useCaseList.size() - 1) ? "," : "";
            dictionaryContent += "\n";
        }
        dictionaryContent += "}";
        return dictionaryContent;
    }

    private String getGridListDictionary(UseCase useCase) {
        String t = StringUtility.space(2);
        String content = "";
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
            try {
                DomainEntity gridListFruitDomainEntity = this.useCaseService.getGridListFruitDomainEntity(useCase);
                List<DomainEntityAttribute> attributes = gridListFruitDomainEntity.getDomainEntityAttributes();
                for (DomainEntityAttribute attribute : attributes) {
                    content += t + t + "," + "\"" + attribute.getName() + "Column" + "\": " + "\"" + attribute.getFaTitle() + "\"" + "\n";
                }

                UseCaseData plant = this.useCaseService.getPlant(useCase);
                List<UseCaseDataAttribute> plantAttributes = plant.getUseCaseDataAttributes();
                for (UseCaseDataAttribute plantAttribute : plantAttributes) {
                    content += t + t + "," + "\"" + plantAttribute.getName() + "SearchField" + "\": " + "\"" + plantAttribute.getFaTitle() + "\"" + "\n";
                }

            } catch (GetGridListFruitDomainEntityException | GetPlantException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    private String getViewDictionary(UseCase useCase) {
        String t = StringUtility.space(2);
        String eol = "\n";
        String content = "";
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.View)) {
            content += t + t + "," + "\"Profile\": \"نمایه\"" + eol;
            try {
                DomainEntity viewDomainEntity = this.useCaseService.getViewDomainEntity(useCase);
                List<DomainEntityAttribute> attributes = viewDomainEntity.getDomainEntityAttributes();
                for (DomainEntityAttribute attribute : attributes) {
                    content += t + t + "," + "\"" + attribute.getName() + "\": " + "\"" + attribute.getFaTitle() + "\"" + eol;
                }
            } catch (GetViewDomainEntityException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

}
