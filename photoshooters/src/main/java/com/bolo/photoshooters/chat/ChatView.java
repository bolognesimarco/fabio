package com.bolo.photoshooters.chat;

import java.io.Serializable;
import java.util.List;

import org.primefaces.context.RequestContext;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import com.bolo.photo.web.entity.ChatMessaggio;
import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.service.ServiziComuni;
import com.bolo.photoshooters.service.ServiziComuniImpl;
import com.bolo.photoshooters.web.EMF;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
 
@ManagedBean
@ViewScoped
public class ChatView implements Serializable {

	private static final long serialVersionUID = -6017508556279921559L;

	//private final PushContext pushContext = PushContextFactory.getDefault().getPushContext();
 
    
	private final EventBus eventBus = EventBusFactory.getDefault().eventBus();
 
	private ServiziComuni serv = new ServiziComuniImpl();
	
	@ManagedProperty("#{chatUsers}")
    private ChatUsers users;
 
    private String privateMessage;
     
    private String globalMessage;
     
    private String username;
     
    private boolean loggedIn;
     
    private String privateUser;
     
    private final static String CHANNEL = "/{room}/";
    
    private ChatMessaggio chatMessaggio = new ChatMessaggio();
    
    
    public void sendGlobal() {
//        eventBus.publish(CHANNEL + "*", username + ": " + globalMessage);
    	eventBus.publish(CHANNEL + "*", username + ": " + globalMessage);       
        System.out.println("sendGlobal message=="+globalMessage);
        globalMessage = null;
    }
     
    public void sendPrivate() {
        eventBus.publish(CHANNEL + privateUser, "[PM] " + username + ": " + privateMessage);
        ChatMessaggio chatMessaggio = new ChatMessaggio();
        chatMessaggio.setDestinatarioChat(privateUser);
        chatMessaggio.setMittenteChat(username);
        chatMessaggio.setMessaggioChat(privateMessage);
        try {
			serv.persist(chatMessaggio);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("sendPrivate message=="+privateMessage);
        privateMessage = null;
    }
     
    public void login() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
         
        if(users.contains(username)) {
            loggedIn = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username taken", "Try with another username."));
            requestContext.update("growl");
        }
        else {
            users.add(username);
            requestContext.execute("PF('subscriber').connect('/" + username + "')");
            loggedIn = true;
        }
    }
     
    public void disconnect() {
        //remove user and update ui
        users.remove(username);
        RequestContext.getCurrentInstance().update("chatform:users");
         
        //push leave information
        eventBus.publish(CHANNEL + "*", username + " left the channel.");
         
        //reset state
        loggedIn = false;
        username = null;
    }
    
    
    
//    GETTERS&SETTERS     ********************************************************************************
 
    
    
    
    public ChatUsers getUsers() {
        return users;
    }
 
    public ChatMessaggio getChatMessaggio() {
		return chatMessaggio;
	}

	public void setChatMessaggio(ChatMessaggio chatMessaggio) {
		this.chatMessaggio = chatMessaggio;
	}

	public void setUsers(ChatUsers users) {
        this.users = users;
    }
     
    public String getPrivateUser() {
        return privateUser;
    }
 
    public void setPrivateUser(String privateUser) {
        this.privateUser = privateUser;
    }
 
    public String getGlobalMessage() {
        return globalMessage;
    }
 
    public void setGlobalMessage(String globalMessage) {
        this.globalMessage = globalMessage;
    }
 
    public String getPrivateMessage() {
        return privateMessage;
    }
 
    public void setPrivateMessage(String privateMessage) {
        this.privateMessage = privateMessage;
    }
     
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
     
    public boolean isLoggedIn() {
        return loggedIn;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
 

}
