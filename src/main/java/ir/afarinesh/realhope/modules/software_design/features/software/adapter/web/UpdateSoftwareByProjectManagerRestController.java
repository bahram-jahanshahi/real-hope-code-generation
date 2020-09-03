package ir.afarinesh.realhope.modules.software_design.features.software.adapter.web;

import ir.afarinesh.realhope.core.usecase.*;
import org.springframework.web.bind.annotation.*;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.UpdateSoftwareByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.software.application.ports.in.UpdateSoftwareByProjectManagerUseCase.*;

@RestController
@RequestMapping("/app/admin/project-manager/software/update-software-by-project-manager")
public class UpdateSoftwareByProjectManagerRestController{

	final UpdateSoftwareByProjectManagerUseCase useCase; 

	public UpdateSoftwareByProjectManagerRestController(UpdateSoftwareByProjectManagerUseCase useCase) {
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