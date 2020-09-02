package ir.afarinesh.realhope.core.usecase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UseCaseSeedsCommand<S> {
    private S seedsCommand;
    private String locale;
}
