package ir.afarinesh.realhope.modules.software_design.features.use_case.adapter.web;

import ir.afarinesh.realhope.core.usecase.*;
import org.springframework.web.bind.annotation.*;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.ListUseCaseByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.software_design.features.use_case.application.ports.in.ListUseCaseByProjectManagerUseCase.*;

@RestController
@RequestMapping("/app/admin/project-manager/use-case/list-use-case-by-project-manager")
public class ListUseCaseByProjectManagerRestController{

	final ListUseCaseByProjectManagerUseCase useCase; 

	public ListUseCaseByProjectManagerRestController(ListUseCaseByProjectManagerUseCase useCase) {
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