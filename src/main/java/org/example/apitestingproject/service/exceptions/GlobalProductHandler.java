//package org.example.apitestingproject.service.exceptions;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.ErrorResponse;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ControllerAdvice
//public class GlobalProductHandler {
//    @ExceptionHandler(value = ProductDoesNotExistsException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public @ResponseBody ErrorResponse handleProductDoesNotExistsException(ProductDoesNotExistsException ex) {
//        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
//    }
//
//    @ExceptionHandler(value = ProductAlreadyExistsException.class)
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public @ResponseBody ErrorResponse handleProductAlreadyExistsException(ProductAlreadyExistsException ex) {
//        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
//    }
//
//    public GlobalProductHandler() {
//        System.out.println("Global account exception handler invoked");
//    }
//}
