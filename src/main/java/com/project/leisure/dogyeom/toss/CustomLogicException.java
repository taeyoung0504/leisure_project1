package com.project.leisure.dogyeom.toss;

public class CustomLogicException extends RuntimeException{
	
	private final ExceptionCode exceptionCode;
	
	public CustomLogicException(ExceptionCode code) {
        exceptionCode = code; 
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
	
	
}
