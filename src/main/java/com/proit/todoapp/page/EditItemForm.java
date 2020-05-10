package com.proit.todoapp.page;

import javax.inject.Inject;
import javax.ws.rs.HttpMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proit.todoapp.MainView;
import com.proit.todoapp.domain.ToDoItem;
import com.proit.todoapp.service.ToDoItemService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import com.vaadin.flow.server.RequestHandler;

@Route(value = "edit")
public class EditItemForm extends VerticalLayout implements HasUrlParameter<String> {
	private static final long serialVersionUID = 2L;
	private TextField id = new TextField("Id");
	private TextField name = new TextField("Name");	
	private TextArea description = new TextArea("Description");
	private ToDoItem item=new ToDoItem();
	@Inject
	private ToDoItemService itemService;
    
	public EditItemForm() {
		id.setVisible(false);
		Binder<ToDoItem> binder = new BeanValidationBinder<>(ToDoItem.class);
		binder.bindInstanceFields(this);
		binder.setBean(item);		

		Button save = new Button("Save");
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickListener(event -> {
			
			if (itemService.editItem(item, id.getValue())==200) {		
				new Notification("Successfully Updated", 3000).open();
				UI.getCurrent().navigate(MainView.class);
			}else {
				new Notification("Please try again!!!", 3000).open();
			}			
		});	
		
		HorizontalLayout buttonsLayout = new HorizontalLayout(save);
		
		VerticalLayout divLayout = new VerticalLayout(id, name,description, save);
        divLayout.setAlignSelf(Alignment.END, buttonsLayout);
        Div loginDiv = new Div(divLayout);
        setAlignItems(Alignment.CENTER);
        name.focus();
        add(loginDiv);
	}
	
	@Override
	public void setParameter(BeforeEvent event, @WildcardParameter String pathValue) {
		ToDoItem item=itemService.getItem(pathValue);
		id.setValue(pathValue);
		name.setValue(item.getName());
		description.setValue(item.getDescription());
	}	
}
