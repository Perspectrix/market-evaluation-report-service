package com.perspectrix.market.controllers.general;

import com.perspectrix.market.domain.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for health check endpoints
 *
 * @author William Stone
 * @since 2025-01-15
 */
@RestController
@RequestMapping("/api/report")
public class HealthController {

    /**
     * Basic health check endpoint that returns current system timestamp.
     *
     * @return ResponseEntity containing current timestamp in milliseconds
     */

    @GetMapping("/health")
    public ResponseEntity<Long> healthCheck() {
        return ResponseEntity.ok(System.currentTimeMillis());
    }

    /**
     * Test endpoint that creates and returns a person's address.
     *
     * @return ResponseEntity containing a test address string
     */
    @GetMapping("/person-check")
    public ResponseEntity<String> check(){
        Person person = new Person();
        person.setAddress("123 Main St");
        return ResponseEntity.ok(person.getAddress());
    }

}
