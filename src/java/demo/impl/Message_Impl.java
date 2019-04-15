package demo.impl;

import demo.spec.Message;

public class Message_Impl implements Message, java.io.Serializable{
    
	private String user, message;

    @Override
    public String getContent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getOwner() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	
}

