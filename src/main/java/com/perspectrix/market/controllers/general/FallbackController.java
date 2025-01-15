package com.perspectrix.market.controllers.general;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author William Stone
 * @since 2025-01-15
 */
@RestController
public class FallbackController {

    /**
     * Global fallback handler for undefined endpoints.
     *
     * @return ResponseEntity with 404 status and error message
     * @see HttpStatus#NOT_FOUND
     */
    @RequestMapping("/**")
    public ResponseEntity<String> handleFallback() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Endpoint not found");
    }
}
