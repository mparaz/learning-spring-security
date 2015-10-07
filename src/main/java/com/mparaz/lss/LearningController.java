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
    // Spring can
    @RequestMapping("/go")
    @Secured("ROLE_mockRole")
    public ResponseEntity<String> go(Principal principal) {
        return new ResponseEntity<String>("arrived: " + principal.getName(), HttpStatus.OK);
    }

    @RequestMapping("/goCustom")
    @Secured("ROLE_mockRoleCustom")
    public ResponseEntity<String> goCustom() {
        return new ResponseEntity<String>("arrived", HttpStatus.OK);
    }

    // Protected by a role that is not assigned to a user.
    @RequestMapping("/goNot")
    @Secured("ROLE_mockRoleNot")
    public ResponseEntity<String> goNot() {
        return new ResponseEntity<String>("arrived", HttpStatus.OK);
    }

    // Not protected, but authentication still required.
    @RequestMapping("/goYes")
    public ResponseEntity<String> goYes() {
        return new ResponseEntity<String>("arrived", HttpStatus.OK);
    }

    // Not protected and no authentication required
    @RequestMapping("/goOpen")
    public ResponseEntity<String> goOpen() {
        return new ResponseEntity<String>("arrived", HttpStatus.OK);
    }

    // Protected and no authentication required - therefore will fail
    @RequestMapping("/goClosed")
    @Secured("ROLE_mockRoleCustom")
    public ResponseEntity<String> goClosed() {
        return new ResponseEntity<String>("not here", HttpStatus.OK);
    }

}
