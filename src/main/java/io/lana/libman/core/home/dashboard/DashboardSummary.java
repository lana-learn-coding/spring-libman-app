package io.lana.libman.core.home.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummary {
    private long readersCount;

    private long booksCount;

    private long borrowsCount;

    private long overDuesCount;
}
