package ir.afarinesh.realhope.core.usecase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PaginationCommand {

    private int pageIndex;

    private int pageSize;
}
