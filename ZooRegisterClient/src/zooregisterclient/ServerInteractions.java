/*
 * Author:              Tristan Ackermann (FK6W29M15)
 * Date:                2017-08-10
 * NetBeans Version:    7.3.1
 * Java Version:        1.8.0_141-b15
 * File Description:    Class containing shared methods between the regular
 *                      client and administrator client in order to connect them
 *                      to the server for the Zoo Register Project.
 */

package zooregisterclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author FK6W29M15
 */
public class ServerInteractions {
    
    //<editor-fold defaultstate="collapsed" desc="Class Variable Declarations">
    
    /** When this client is unable to properly communicate with the server */
    private static final String SERVER_COMMUNICATION_ERROR = "Could not establish communication with the server";
    
    private static ArrayList packetFromServer;
    private static ObjectInputStream fromServer;
    private static ObjectOutputStream toServer;
    private static Socket socket;
    
    /**
     * The possible interactions the administrator client can make with the server
     */
    enum Interactions {
        DELETE, INSERT, LOGIN,
        REGULAR_SEARCH, SEARCH
    }
    
    /**
     * Stores the various search categories for the user
     */
    enum SearchCategory {
        ANIMALS, SPECIES
    }
    
    /**
     * The tables existing in the Zoo Register Database
     */
    enum Table {
        ANIMAL, SPECIES, USER
    }
    
    //</editor-fold>
    
    
    
    /**
     * Connects to the server, sends it this client's request therefrom, and
     * stores the server's response to be processed where needed.
     * <p>
     * Requires this client's packet for the server to be fully prepared for
     * sending, since this method simply connects, sends the packet for the
     * server, and stores the server's response packet in a class-private
     * ArrayList.
     * <p>
     * Since this method simply uses and modifies class variables, no
     * parameters nor returns exist for this class. This merely acts as the
     * voice and ear for this client.
     * 
     * @throws Exception if communication with the server failed, or if the
     * server returned any kind of error.
     */
    static ArrayList getServerResponse(String serverIP, int port, ArrayList packetToServer) throws ServerCommunicationException {
        
        try {
            
            /* Connect to the Server */
            socket = new Socket(serverIP, port);
            toServer = new ObjectOutputStream(socket.getOutputStream());
            fromServer = new ObjectInputStream(socket.getInputStream());
            
            /* Send the Client's packet to the Server */
            toServer.writeObject(packetToServer);
            System.out.println("sent packet to server");
            
            try {
                
                /* Accept the Server's response packet */
                packetFromServer = (ArrayList)fromServer.readObject();
                System.out.println("got server response");
                
                return packetFromServer;
                
            } catch (IOException | ClassNotFoundException e) {
                
                throw new ServerCommunicationException();
                
            }
            
        } catch (IOException e) {
            
            throw new ServerCommunicationException();
            
        }

    }
    
}
