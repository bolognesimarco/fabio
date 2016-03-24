package com.bolo.photoshooters.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.bolo.photo.web.entity.Utente;
import com.bolo.photoshooters.web.UtenteBean;


@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter {


    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String key) {
        //FacesContext context = FacesContext.getCurrentInstance();
        UtenteBean userMB = (UtenteBean) arg0.getExternalContext().getSessionMap().get("utenteBean");
//        int id = Integer.parseInt(key);
//        Utente u = userMB.cercaUtenteById(id);
        Utente u = userMB.cercaUtente(key);
//        if (u!= null){
//        System.out.println("USERNAME SELECTED: "+u.getUsername());
//        }
        return u;
    }
 
    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
    	if(arg2 != null) {
//            return String.valueOf(((Utente) arg2).getId());
            return String.valueOf(((Utente) arg2).getUsername());
        }
        else {
            return null;
        }
    }
    
    
    
   
//   private static UtenteBean utentetrovato = new UtenteBean();
//    @Override
//    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
//        if (submittedValue.trim().equals("")) {
//            return null;
//        } else {
//            try {
//
////                for (Utente ut : utenteDB) {
//                    if (utentetrovato.cercaUtente(submittedValue).getUsername().equals(submittedValue)) {
//                        Utente ut = new Utente();
//                        ut = utentetrovato.cercaUtente(submittedValue);
//                    	return ut;
////                    }
//                }
//
//            } catch(NumberFormatException exception) {
//                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Utente non valido!"));
//            }
//        }
//        return null;
//    }
//    
//    @Override
//    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
//        if (value == null || value.equals("")) {
//            return "";
//        } else {
//            return ((Utente)value).getUsername();
//        }
//    }

	
//    private static Map<Object, String> entities = new WeakHashMap<Object, String>();
//   @Override
//    public String getAsString(FacesContext context, UIComponent component, Object entity) {
//        synchronized (entities) {
//          if (!entities.containsKey(entity)) {
//              String uuid = UUID.randomUUID().toString();
//               entities.put(entity, uuid);
//                return uuid;
//            } else {
//                return entities.get(entity);
//           }
//        }
//   }
//
//    @Override
//   public Object getAsObject(FacesContext context, UIComponent component, String uuid) {
//       for (Entry<Object, String> entry : entities.entrySet()) {
//           if (entry.getValue().equals(uuid)) {
//               return entry.getKey();
//           }
//       }
//       return null;
//  }
}
