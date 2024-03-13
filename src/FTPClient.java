import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

class FTPClient {

	public static void main(String argv[]) throws Exception { 
        String sentence; 
        String modifiedSentence; 
        boolean isOpen = true;
        int number=1;
        boolean notEnd = true;
		int port1=1221;
		int port = 1200;
		String statusCode;
		boolean clientgo = true;
	    
		System.out.println("Welcome to the simple FTP App   \n     Commands  \nconnect servername port# connects to a specified server \nlist: lists files on server \nget: fileName.txt downloads that text file to your current directory \nstor: fileName.txt Stores the file on the server \nclose terminates the connection to the server");
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); 
        sentence = inFromUser.readLine();
        StringTokenizer tokens = new StringTokenizer(sentence);


		if(sentence.startsWith("connect")) {
			String serverName = tokens.nextToken(); // pass the connect command
			serverName = tokens.nextToken();
			port1 = Integer.parseInt(tokens.nextToken());
        	System.out.println("You are connected to " + serverName);
        	Socket ControlSocket= new Socket(serverName, port1);

        	while(isOpen && clientgo) {      
	  			DataOutputStream outToServer = new DataOutputStream(ControlSocket.getOutputStream()); 
          		DataInputStream inFromServer = new DataInputStream(new BufferedInputStream(ControlSocket.getInputStream()));
          		sentence = inFromUser.readLine();
          
        		if(sentence.equals("list:")) {   
	    			port = port +2;
	    			System.out.println(port);
	    			ServerSocket welcomeData = new ServerSocket(port);

	    			System.out.println("\n \n \nThe files on this server are:");
	    			outToServer.writeBytes (port + " " + sentence + " " + '\n');

	    			Socket dataSocket =welcomeData.accept(); 
 	   				DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
            		while(notEnd) {
                		modifiedSentence = inData.readUTF();
                		if(modifiedSentence.equals("eof")) {
                    		break; 
						}
               			System.out.println("	" + modifiedSentence); 
            		}

	 				welcomeData.close();
	 				dataSocket.close();
	 				System.out.println("\nWhat would you like to do next: \nget: file.txt ||  stor: file.txt  || close");
        		}	

    			else if (sentence.startsWith("get: ")) {
    				String fileName = sentence.substring(5).trim(); // Extract the file name from the command
    				outToServer.writeBytes(port + " " + sentence + " " + '\n'); // Send the command to the server

    				// Setup data connection to receive file
 	    			port = port + 2;
    				ServerSocket welcomeData = new ServerSocket(port);
    				outToServer.writeBytes(port + " " + '\n'); // Inform server of data port

    				Socket dataSocket = welcomeData.accept();
    				DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));

					String response = inFromServer.readLine(); // Response from server: 200 or 550
				
					if (response.startsWith("200")) {
        			// File found, receive and save the file
						System.out.println("Downloading file...");
        			FileOutputStream fileOut = new FileOutputStream(fileName);
        			byte[] buffer = new byte[1024];
        			int bytesRead;
        			while ((bytesRead = inData.read(buffer)) != -1) {
        		    	fileOut.write(buffer, 0, bytesRead);
        			}
        			fileOut.close();

        			System.out.println("File '" + fileName + "' downloaded successfully.");
    				} 
					else if (response.startsWith("550")) {
        			// File not found
        				System.out.println("Error: File '" + fileName + "' not found on the server.");
    				}

    				welcomeData.close();
    				dataSocket.close();

    				System.out.println("\nWhat would you like to do next: \nlist: || get: file.txt || stor: file.txt || close");
				}

				// else if (response.startsWith("stor")) {

				else{
	     			if(sentence.equals("close")) {
						clientgo = false;
	     			}
	     			System.out.print("No server exists with that name or server not listening on that port try agian");         
    			}
			}
		}
	}
}
