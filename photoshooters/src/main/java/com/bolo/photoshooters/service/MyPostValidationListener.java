package com.bolo.photoshooters.service;

import javax.faces.component.UIInput;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ListenerFor;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

@ListenerFor(sourceClass=javax.faces.component.html.HtmlInputText.class, systemEventClass=javax.faces.event.PostValidateEvent.class)
public class MyPostValidationListener implements SystemEventListener {

    @Override
    public void processEvent(SystemEvent systemEvent) throws AbortProcessingException {
    	System.out.println("NEL MyPostValidationListener");
    	
        UIInput sourceOfTrouble = (UIInput) systemEvent.getSource();
 
        if(!sourceOfTrouble.isValid()) {
            sourceOfTrouble.getAttributes().put("styleClass" , "invalid-input");
        }
        else
        {
            sourceOfTrouble.getAttributes().put("styleClass" , "valid-input");
        }
    }

    @Override
    public boolean isListenerForSource(Object o) 
    { return true; }
}
