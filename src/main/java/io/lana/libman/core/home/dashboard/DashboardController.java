package io.lana.libman.core.home.dashboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Precision;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@AllArgsConstructor
class DashboardController {
    private final DashboardRepo dashboardRepo;

    private final ObjectMapper objectMapper;

    @GetMapping("/library/dashboard")
    @PreAuthorize("hasAnyAuthority('LIBRARIAN')")
    public ModelAndView index() {
        final var summary = dashboardRepo.countDashboardSummary();
        return new ModelAndView("/home/dashboard", Map.of(
                "summary", summary,
                "overDues", dashboardRepo.getTopOverDueBorrow(),
                "borrow7Days", convertCountLast7DaysToChart(dashboardRepo.countBorrowByDayInLast7Days()),
                "reader7Days", convertCountLast7DaysToChart(dashboardRepo.countReaderByDayInLast7Days()),
                "income7Days", convertCountLast7DaysToChart(dashboardRepo.countIncomeByDayInLast7Days()),
                "income30Days", convertCountLast30DaysToChartData(dashboardRepo.countIncomeByDayInLast30Days())
        ));
    }

    private Map<String, Object> convertCountLast7DaysToChart(Map<String, Double> raw) {
        final var dataList = IntStream.range(0, 8)
                .mapToObj(i -> {
                    final var date = LocalDate.now().minusDays(i).toString();
                    return Map.of("x", date, "y", raw.getOrDefault(date, 0D));
                })
                .collect(Collectors.toList());
        try {
            return Map.of(
                    "today", raw.getOrDefault(LocalDate.now().toString(), 0D),
                    "data", objectMapper.writeValueAsString(dataList)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> convertCountLast30DaysToChartData(Map<String, Map<String, Double>> raw) {
        final Map<String, List<Map<String, Object>>> dataList = Map.of("totalCost", new ArrayList<>(),
                "borrowCost", new ArrayList<>(), "overDueCost", new ArrayList<>());

        final Map<String, Double> defaultData = Map.of("totalCost", 0d,
                "borrowCost", 0d, "overDueCost", 0d);

        dataList.forEach((key, list) -> {
            IntStream.range(1, 32)
                    .filter(i -> i % 2 != 0)
                    .forEach(i -> {
                        final var date = LocalDate.now().minusDays(i).toString();
                        final var preDate = LocalDate.now().minusDays(i + 1).toString();
                        final var sum = Precision.round(raw.getOrDefault(date, defaultData).get(key) + raw.getOrDefault(preDate, defaultData).get(key), 2);
                        list.add(Map.of("x", date, "y", sum));
                    });
        });
        return dataList.keySet().stream().collect(Collectors.toMap(Function.identity(), k -> {
            try {
                return objectMapper.writeValueAsString(dataList.get(k));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
