package ir.afarinesh.realhope.modules.generation.features.crud.application.ports.in;

import ir.afarinesh.realhope.core.annotations.UseCase;
import ir.afarinesh.realhope.core.usecase.UseCaseFruit;
import ir.afarinesh.realhope.core.usecase.UseCaseFruitSeeds;
import ir.afarinesh.realhope.core.usecase.UseCasePlant;
import ir.afarinesh.realhope.core.usecase.UseCaseSeedsCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@UseCase
public interface GenerateCrudByProjectManager {

    UseCaseFruit<Fruit> cultivate(UseCasePlant<Plant> plant) throws CultivateException;

    UseCaseFruitSeeds<FruitSeeds> prepare(UseCaseSeedsCommand<SeedsCommands> seedsCommand) throws PrepareException;

    @Data
    class Fruit {
        @NonNull
        private Boolean isSuccessful;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Plant {
        @NonNull
        private Long crudId;
    }

    class CultivateException extends Exception {
        public CultivateException(String message) {
            super(message);
        }
    }

    @Data
    class FruitSeeds {
        @NonNull
        private Object sample;
    }

    @Data
    @NoArgsConstructor
    class SeedsCommands {
        @NonNull
        private Object sample;
    }

    class PrepareException extends Exception {
        public PrepareException(String message) {
            super(message);
        }
    }
}
