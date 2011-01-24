package eu.heliovo.queryservice.common.util;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class BeanFactory implements ObjectFactory {

  public Object getObjectInstance(Object obj,
      Name name, Context nameCtx, Hashtable environment)
      throws NamingException {
      Reference ref = (Reference) obj;
      Enumeration addrs = ref.getAll();
      while (addrs.hasMoreElements()) {
          RefAddr addr = (RefAddr) addrs.nextElement();
          String propertyName = addr.getType();
          String propertyValue = (String) addr.getContent();
          //Checking 
          if (propertyName.equals("propertyValue")) {        	
        	  return propertyValue;
          }
      }
     return null;
     
  }
  


}
