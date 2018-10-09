package com.ceva.crypto.exception;

public class CryptoException extends Exception
{
  private static final long serialVersionUID = 100L;

  public CryptoException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public CryptoException(String message)
  {
    super(message);
  }

  public CryptoException(Throwable cause)
  {
    super(cause);
  }

  public CryptoException()
  {
  }
}