
package kz.iitu.springlab01.web;

import kz.iitu.springlab01.security.AppUser;
import kz.iitu.springlab01.security.AppUserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class AdminController {

    private final AppUserRepository repository;

    public AdminController(AppUserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {

        List<AppUser> users = repository.findAll()
                .stream()
                .sorted(Comparator.comparing(AppUser::getUsername))
                .toList();

        model.addAttribute("users", users);
        model.addAttribute("title", "Administration");

        return "admin/users";
    }
}
