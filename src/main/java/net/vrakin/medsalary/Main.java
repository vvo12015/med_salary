package net.vrakin.medsalary;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("!!!&&&dpemcyd"));

        System.out.println(LocalDate.now().withDayOfMonth(01));
    }
}
