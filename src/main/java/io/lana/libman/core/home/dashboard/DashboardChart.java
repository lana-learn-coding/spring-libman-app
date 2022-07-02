package io.lana.libman.core.home.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardChart {
    private long readersCount;

    private long booksCount;

    private long borrowsCount;

    private long overDuesCount;
}
