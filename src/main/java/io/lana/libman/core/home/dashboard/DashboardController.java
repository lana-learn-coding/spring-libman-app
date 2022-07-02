package io.lana.libman.core.home.dashboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Map;
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
                "income7Days", convertCountLast7DaysToChart(dashboardRepo.countIncomeByDayInLast7Days())
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
}
