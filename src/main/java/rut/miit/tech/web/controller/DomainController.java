package rut.miit.tech.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class DomainController {

    @GetMapping("/redirection")
    public String redirectByRole(@AuthenticationPrincipal UserDetails userDetails) {
        String redirectUrl = "";
        if(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            redirectUrl = "/admin";
        }
        else if(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("CLIENT"))) {
            redirectUrl = "/client";
        }
        else if(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("EMPLOYEE"))) {
            redirectUrl = "/employee";
        }
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/access_denied")
    public String accessDeniedPage() {
        return "domain/AccessDenied";
    }

}
