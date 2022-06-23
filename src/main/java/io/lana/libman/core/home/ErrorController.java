package io.lana.libman.core.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class ErrorController {
    @RequestMapping("/404")
    public String error404() {
        return "error/404";
    }

    @RequestMapping("/500")
    public String error500() {
        return "error/500";
    }

    @RequestMapping("/403")
    public String error403() {
        return "error/403";
    }
}
