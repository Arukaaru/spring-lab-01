package kz.iitu.springlab01;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        var encoder = new BCryptPasswordEncoder();

        System.out.println("USER HASH: " + encoder.encode("user123"));
        System.out.println("USER SECOND HASH: " + encoder.encode("user123"));
        System.out.println("ADMIN HASH: " + encoder.encode("admin123"));
    }
}