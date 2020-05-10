package com.proit.todoapp;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.proit.todoapp.domain.ToDoItem;
import com.proit.todoapp.domain.ToDoItemResponse;
import com.proit.todoapp.page.AddItemForm;
import com.proit.todoapp.service.ToDoItemService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

/**
 * The main view contains a simple label element and a template element.
 */
@Route("")
@PWA(name = "Project Base for Vaadin Flow with CDI", shortName = "Project Base", enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

	@Inject
	private ToDoItemService itemService;

	@PostConstruct
	public void init() {
		ToDoItemResponse items = new ToDoItemResponse();
		if (itemService.getItems() != null)
			items = itemService.getItems();

		Button add = new Button("Add");
		add.addClickListener(listener -> {
			UI.getCurrent().navigate(AddItemForm.class);
		});
		add.getElement().setAttribute("theme", "primary");

		Grid<ToDoItem> grid = new Grid<>(ToDoItem.class);
		grid.setColumns("name", "description", "createdAt");
		grid.addColumn(new ComponentRenderer<>(item -> {
			Icon edit = new Icon(VaadinIcon.EDIT);
			edit.getStyle().set("cursor", "pointer");
			edit.getElement().setAttribute("theme", "primary");
			edit.setColor("#1676F3");
			edit.addClickListener(event -> {
				UI.getCurrent().navigate("edit/" + item.getId());
			});
			return edit;
		})).setHeader("Action");

		grid.setColumnReorderingAllowed(true);
		grid.setItemDetailsRenderer(
				new ComponentRenderer<>(item -> new VerticalLayout(new Span("Created Date: " + item.getCreatedAt()),
						new Span("Update At: " + item.getUpdatedAt()))));

		grid.setItems(items.getToDoItems());
		add(add, grid);
	}

}
