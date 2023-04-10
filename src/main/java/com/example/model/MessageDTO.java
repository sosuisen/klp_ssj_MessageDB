package com.example.model;

import javax.ws.rs.FormParam;

@lombok.Data
@lombok.AllArgsConstructor
public class MessageDTO {
	private int id;
	@FormParam("message")
	private String message;
	@FormParam("name")
	private String name;
}

