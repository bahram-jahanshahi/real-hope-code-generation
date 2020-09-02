package ir.afarinesh.realhope.core.domain;

import ir.afarinesh.realhope.shares.utilities.CalendarUtility;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter @Setter
public class JavaDate {
    private Integer year;
    private Integer month;
    private Integer day;

    public Date toDate() {
        return CalendarUtility.getDate(this);
    }
}
