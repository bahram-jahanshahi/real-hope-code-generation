package ir.afarinesh.realhope.core.usecase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class PagedResultFruit {

    private long totalElements;

    private int totalPages;
}
