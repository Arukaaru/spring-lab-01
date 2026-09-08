package kz.iitu.springlab01.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "world") String name) {
        return new Greeting(
                "Hello, " + name + "!",
                owner,
                LocalDateTime.now()
        );
    }

    @GetMapping("/info")
    public Info info() {
        return new Info(
                owner,
                System.getProperty("java.version"),
                Runtime.getRuntime().availableProcessors()
        );
    }

    public record Greeting(
            String message,
            String owner,
            LocalDateTime timestamp
    ) {}

    public record Info(
            String owner,
            String javaVersion,
            int cpuCores
    ) {}
    @GetMapping("/factorial")
    public FactorialResponse factorial(
            @RequestParam(defaultValue = "0") int n) {

        long result = 1;

        for (int i = 2; i <= n; i++) {
            result *= i;
        }

        return new FactorialResponse(n, result);
    }

    public record FactorialResponse(
            int number,
            long factorial
    ) {}
}