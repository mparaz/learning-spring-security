package com.mparaz.lss;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LearningController {
    // Protected by a role that is assigned to a user.
    // Spring Security can inject the Principal.
    @RequestMapping("/go")
    @Secured("ROLE_javaeeRole")
    public ResponseEntity<String> go(Principal principal) {
        return new ResponseEntity<String>("arrived: " + principal.getName(), HttpStatus.OK);
    }
}
