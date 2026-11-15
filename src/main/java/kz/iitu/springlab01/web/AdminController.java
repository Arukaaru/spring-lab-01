
package kz.iitu.springlab01.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admin")
    public String adminPage() {
        return "Admin area: access granted";
    }
}
