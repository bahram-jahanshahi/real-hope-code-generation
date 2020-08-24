package ir.afarinesh.realhope.core.usecase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class UseCaseFruit<T> {
    private T fruit;
    private Boolean isSuccessful;
    private String message;
}
