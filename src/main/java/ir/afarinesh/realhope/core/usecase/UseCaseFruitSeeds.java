package ir.afarinesh.realhope.core.usecase;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UseCaseFruitSeeds<S> {
    private S fruitSeeds;
    private Boolean isSuccessful;
    private String message;
}
