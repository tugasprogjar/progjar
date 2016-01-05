package mangiareserver;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{
    
    private static final long serialVersionUID = 1L;
    public String type, sender, content, recipient;
    public Player player;
    public ArrayList<Player> blob;
    public ArrayList<Food> food;
    public Message(String type, String sender, String content, String recipient,ArrayList<Player> player,ArrayList<Food> food ){
        this.type = type; 
        this.blob=player;
        this.food=food;
        this.sender = sender; 
        this.content = content; 
        this.recipient = recipient;
    }
    
    @Override
    public String toString(){
        return "{type='"+type+"', sender='"+sender+"', content='"+content+"', recipient='"+recipient+"'}";
    }
}
