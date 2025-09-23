package org.example.apitestingproject.service.exceptions;

public class PurchaseNotFoundException extends RuntimeException
{
    public PurchaseNotFoundException(String message)
    {
        super(message);
    }
}
