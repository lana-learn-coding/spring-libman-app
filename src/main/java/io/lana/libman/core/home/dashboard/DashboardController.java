package io.lana.libman.core.home.dashboard;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@AllArgsConstructor
class DashboardController {
    private final DashboardRepo dashboardRepo;

    @GetMapping("/library/dashboard")
    public ModelAndView index() {
        final var summary = dashboardRepo.countDashboardSummary();
        return new ModelAndView("/home/dashboard", Map.of("summary", summary));
    }
}
