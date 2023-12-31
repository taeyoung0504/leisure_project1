package com.project.leisure;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity not found")
public class DataNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DataNotFoundException(String message) {
		super(message);

	}
}

/* 예외처리를 위해 생성 */
/* 에러방지를 위해 DataNotFoundException 클래스 사용 */