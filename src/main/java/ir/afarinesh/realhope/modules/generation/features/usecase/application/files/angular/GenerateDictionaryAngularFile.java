package ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular;

import ir.afarinesh.realhope.entities.feature.*;
import ir.afarinesh.realhope.entities.feature.enums.FrontendActionTypeEnum;
import ir.afarinesh.realhope.entities.feature.enums.UserInterfaceTypeEnum;
import ir.afarinesh.realhope.entities.project.SoftwareApplicationPanel;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.files.angular.exceptions.GenerateDictionaryAngularFileException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCasePathService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.UseCaseService;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetGridListFruitDomainEntityException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetPlantException;
import ir.afarinesh.realhope.modules.generation.features.usecase.application.shares.exceptions.GetViewDomainEntityException;
import ir.afarinesh.realhope.shares.repositories.SoftwareApplicationPanelSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.UseCaseRelationSpringJpaRepository;
import ir.afarinesh.realhope.shares.repositories.UseCaseSpringJpaRepository;
import ir.afarinesh.realhope.shares.services.FileManagementService;
import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import ir.afarinesh.realhope.shares.utilities.StringUtility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateDictionaryAngularFile {
    String t = StringUtility.space(2);
    String eol = "\n";
    final FileManagementService fileManagementService;
    final UseCasePathService useCasePathService;
    final UseCaseService useCaseService;
    final SoftwareApplicationPanelSpringJpaRepository softwareApplicationPanelSpringJpaRepository;
    final UseCaseSpringJpaRepository useCaseSpringJpaRepository;
    final UseCaseRelationSpringJpaRepository useCaseRelationSpringJpaRepository;

    public GenerateDictionaryAngularFile(FileManagementService fileManagementService,
                                         UseCasePathService useCasePathService,
                                         UseCaseService useCaseService,
                                         SoftwareApplicationPanelSpringJpaRepository softwareApplicationPanelSpringJpaRepository,
                                         UseCaseSpringJpaRepository useCaseSpringJpaRepository,
                                         UseCaseRelationSpringJpaRepository useCaseRelationSpringJpaRepository) {
        this.fileManagementService = fileManagementService;
        this.useCasePathService = useCasePathService;
        this.useCaseService = useCaseService;
        this.softwareApplicationPanelSpringJpaRepository = softwareApplicationPanelSpringJpaRepository;
        this.useCaseSpringJpaRepository = useCaseSpringJpaRepository;
        this.useCaseRelationSpringJpaRepository = useCaseRelationSpringJpaRepository;
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
        List<UseCase> useCaseList = this.useCaseSpringJpaRepository.findAllBySoftwareApplicationPanel_Id(panel.getId());
        String dictionaryContent = "{" + eol;
        for (int i = 0; i < useCaseList.size(); i++) {
            UseCase useCase = useCaseList.get(i);
            String useCaseTitle = useCase.getName() + "By" + useCase.getSoftwareRole().getName();
            dictionaryContent += ""
                    + t + "\"" + useCaseTitle + "\": {" + eol
                    + t + t + "\"" + "FormTitle" + "\": " + "\"" + useCase.getFaTitle() + "\"" + eol
                    + t + t + ",\"Cancel\": \"بازگشت\"" + eol
                    + this.getGridListDictionary(useCase)
                    + this.getViewDictionary(useCase)
                    + this.getUpdateDictionary(useCase)
                    + this.getPopupDictionary(useCase)
                    + t + "}";
            dictionaryContent += (i < useCaseList.size() - 1) ? "," : "";
            dictionaryContent += eol;
        }
        dictionaryContent += "}";
        return dictionaryContent;
    }

    private String getGridListDictionary(UseCase useCase) {
        String content = "";
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.GridList)) {
            try {
                DomainEntity gridListFruitDomainEntity = this.useCaseService.getGridListFruitDomainEntity(useCase);
                List<DomainEntityAttribute> attributes = gridListFruitDomainEntity.getDomainEntityAttributes();
                for (DomainEntityAttribute attribute : attributes) {
                    content += t + t + "," + "\"" + attribute.getName() + "Column" + "\": " + "\"" + attribute.getFaTitle() + "\"" + eol;
                }

                UseCaseData plant = this.useCaseService.getPlant(useCase);
                List<UseCaseDataAttribute> plantAttributes = plant.getUseCaseDataAttributes();
                for (UseCaseDataAttribute plantAttribute : plantAttributes) {
                    content += t + t + "," + "\"" + plantAttribute.getName() + "SearchField" + "\": " + "\"" + plantAttribute.getFaTitle() + "\"" + eol;
                }

            } catch (GetGridListFruitDomainEntityException | GetPlantException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    private String getViewDictionary(UseCase useCase) {
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

    private String getUpdateDictionary(UseCase useCase) {
        String content = "";
        if (useCase.getUserInterfaceType().equals(UserInterfaceTypeEnum.Update)) {
            content += ",\"BooleanYes\": \"بلی\"" + eol;
            content += ",\"BooleanNo\": \"خیر\"" + eol;
            try {
                UseCaseData plant = this.useCaseService.getPlant(useCase);
                List<UseCaseDataAttribute> attributes = plant.getUseCaseDataAttributes();
                for (UseCaseDataAttribute attribute : attributes) {
                    content += t + t + "," + "\"" + attribute.getName() + "\": " + "\"" + attribute.getFaTitle() + "\"" + eol;
                    content += t + t + "," + "\"" + attribute.getName() + "Error\": " + "\"" + attribute.getDescription() + "\"" + eol;
                }
            } catch (GetPlantException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    private String getPopupDictionary(UseCase useCase) {
        String content = "";
        List<UseCaseRelation> relations = this.useCaseRelationSpringJpaRepository.findAllBySource_Id(useCase.getId());
        for (UseCaseRelation relation : relations) {
            if (relation.getFrontendActionType().equals(FrontendActionTypeEnum.PopupForm)) {
                content += t + t + "," + "\"" + relation.getName() + "\"" + ": " + "\"" + relation.getFaTitle() + "\"" + eol;
            }
        }
        return content;
    }

}
