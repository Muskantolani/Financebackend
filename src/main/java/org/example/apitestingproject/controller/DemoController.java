package org.example.apitestingproject.controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoController {
    @GetMapping("/public/ping") public String pub() { return "pong"; }
    @GetMapping("/user/hello") public String user() { return "hello user"; }
    @GetMapping("/admin/hello") public String admin() { return "hello admin"; }
}

