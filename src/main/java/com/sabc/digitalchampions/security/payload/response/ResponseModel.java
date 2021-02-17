package com.sabc.digitalchampions.security.payload.response;


import org.springframework.http.HttpStatus;

public class ResponseModel<T> {

	private String message;
	private int status;
	private T data;

	public ResponseModel(String message, HttpStatus status) {
		this.message = message;
		this.status = status.value();
		this.data = null;
	}

	public ResponseModel(T data) {
		this.message = "Successfully";
		this.data = data;
		this.status = HttpStatus.OK.value();
	}

	public ResponseModel(String message, HttpStatus status, T data) {
		this.message = message;
		this.status = status.value();
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status.value();
	}

	@Override
	public String toString() {
		return "MessageResponse{" +
				"message='" + message + '\'' +
				", status=" + status +
				", data=" + data +
				'}';
	}
}
