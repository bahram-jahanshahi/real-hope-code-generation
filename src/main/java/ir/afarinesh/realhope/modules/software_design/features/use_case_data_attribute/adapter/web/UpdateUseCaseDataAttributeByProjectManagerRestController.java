package ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.adapter.web;

import ir.afarinesh.realhope.core.usecase.*;
import org.springframework.web.bind.annotation.*;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application.ports.in.UpdateUseCaseDataAttributeByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case_data_attribute.application.ports.in.UpdateUseCaseDataAttributeByProjectManagerUseCase.*;

@RestController
@RequestMapping("/app/admin/project-manager/use-case-data-attribute/update-use-case-data-attribute-by-project-manager")
public class UpdateUseCaseDataAttributeByProjectManagerRestController{

	final UpdateUseCaseDataAttributeByProjectManagerUseCase useCase; 

	public UpdateUseCaseDataAttributeByProjectManagerRestController(UpdateUseCaseDataAttributeByProjectManagerUseCase useCase) {
		this.useCase = useCase;
	}

	@PostMapping("/cultivate")
	public UseCaseFruit<Fruit> cultivate(@RequestBody UseCasePlant<Plant> plant) {
		try {
			return useCase.cultivate(plant);
		} catch (CultivateException e) {
			return new UseCaseFruit<>(null, false, e.getMessage());
		}
	}

	@PostMapping("/prepare")
	public UseCaseFruitSeeds<FruitSeeds> prepare(@RequestBody UseCaseSeedsCommand<SeedsCommands> seedsCommand) {
		try {
			return useCase.prepare(seedsCommand);
		} catch (PrepareException e) {
			return new UseCaseFruitSeeds<>(null, false, e.getMessage());
		}
	}
}