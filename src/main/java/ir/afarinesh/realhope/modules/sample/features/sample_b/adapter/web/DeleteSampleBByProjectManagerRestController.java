package ir.afarinesh.realhope.modules.sample.features.sample_b.adapter.web;

import ir.afarinesh.realhope.core.usecase.*;
import org.springframework.web.bind.annotation.*;
import ir.afarinesh.realhope.modules.sample.features.sample_b.application.ports.in.DeleteSampleBByProjectManagerUseCase;
import ir.afarinesh.realhope.modules.sample.features.sample_b.application.ports.in.DeleteSampleBByProjectManagerUseCase.*;

@RestController
@RequestMapping("/app/admin/project-manager/sample-b/delete-sample-b-by-project-manager")
public class DeleteSampleBByProjectManagerRestController{

	final DeleteSampleBByProjectManagerUseCase useCase; 

	public DeleteSampleBByProjectManagerRestController(DeleteSampleBByProjectManagerUseCase useCase) {
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