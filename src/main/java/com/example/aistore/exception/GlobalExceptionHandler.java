package com.example.aistore.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNotFound(NoResourceFoundException e, Model model) {
        model.addAttribute("errorMessage", "The requested page could not be found.");
        return "error/404";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleBadRequest(IllegalArgumentException e, Model model) {
        log.warn("Illegal argument error: {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "error/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        log.error("Unhandled exception: ", e);
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        return "error/error";
    }
}
