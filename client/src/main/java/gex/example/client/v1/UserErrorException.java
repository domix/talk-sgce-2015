package gex.example.client.v1;


import gex.example.dto.v1.ErrorResponse;


public class UserErrorException extends RuntimeException {
  private ErrorResponse errorResponse;

  private String debugInfo;

  private int statusCode;


  public ErrorResponse getErrorResponse() {
    return errorResponse;
  }

  public UserErrorException(String message, ErrorResponse errorResponse, Integer statusCode, String debugInfo) {
    super(message);
    if(statusCode != null) {
        this.statusCode = statusCode;
    }
    this.errorResponse = errorResponse;
    this.debugInfo = debugInfo;
  }
}
