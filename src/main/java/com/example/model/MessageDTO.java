package com.example.model;

import javax.ws.rs.FormParam;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class MessageDTO {
	@FormParam("id")
	private int id;
	@FormParam("message")
	private String message;
	@FormParam("name")
	private String name;
}

