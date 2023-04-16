package com.example.model;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@lombok.Data
@Named
@SessionScoped
public class User implements Serializable {
	private String name = null;
}

