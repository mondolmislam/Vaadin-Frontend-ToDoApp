package com.proit.todoapp.service;

import java.net.URL;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proit.todoapp.constants.ApiLink;
import com.proit.todoapp.domain.ToDoItem;
import com.proit.todoapp.domain.ToDoItemResponse;
import com.vaadin.cdi.annotation.VaadinServiceScoped;

@VaadinServiceScoped
public class ToDoItemService {
	private Client client;
	private WebTarget target;
	
	@PostConstruct
	public void init() {
		 client = ClientBuilder.newClient();
		    target = client.target(ApiLink.TODO);
	}
	
	public ToDoItemResponse getItems(){
		ObjectMapper mapper = new ObjectMapper();
		ToDoItemResponse items=null;
		try {
			items= mapper.readValue(new URL(ApiLink.TODO), ToDoItemResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return items;
	}
	
	public ToDoItem getItem(String id){
		ObjectMapper mapper = new ObjectMapper();
		ToDoItem item=new ToDoItem();
		try {
			item= mapper.readValue(new URL(ApiLink.TODO+"/"+id), ToDoItem.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return item;
	}
	
	public int addItem(ToDoItem toDoItem) {		
		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(toDoItem));
		return response.getStatus();
	}
	public int editItem(ToDoItem toDoItem, String id) {
		Response response = target
				.path("/"+id)
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.json(toDoItem));
		return response.getStatus();
	}
}
