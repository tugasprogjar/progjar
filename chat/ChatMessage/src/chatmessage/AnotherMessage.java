/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatmessage;

import java.io.Serializable;

/**
 *
 * @author baskoro
 */
public class AnotherMessage implements Serializable {
    private String pesan;

    /**
     * @return the pesan
     */
    public String getPesan() {
        return pesan;
    }

    /**
     * @param pesan the pesan to set
     */
    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
}
