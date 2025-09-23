package org.example.apitestingproject.service.exceptions;

public class TransactionNotFoundException extends RuntimeException
{
    public TransactionNotFoundException(String message)
    {
        super(message);
    }
}
