package com.sonim.bft.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.sonim.bft.adb.AdbCommunication;
import com.sonim.bft.automation.FlashingAutomation;
import com.sonim.bft.compress.TAR;
import com.sonim.bft.restapi.model.DeviceModel;
import com.sonim.bft.restapi.model.Status;
 

public class FTPFunctions extends Thread{
    
	public FTPFunctions() {}
     // Creating FTP Client instance
    FTPClient ftp = null;
	private static final int BUFFER_SIZE = 4096 ; 
    
    private String source;
    private String binaryName;
    private String buildPath;
    // Constructor to connect to the FTP Server
    public FTPFunctions(String host, int port, String username, String password,String source,String binaryName, String buildPath) throws Exception{
    	
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftp.connect(host,port);
        System.out.println("FTP URL is:"+ftp.getDefaultPort());
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.login(username, password);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();   
        this.source = source;
        this.binaryName = binaryName;
        this.buildPath = buildPath;
    }    

    public FTPFunctions(String host, int port, String username, String password) throws Exception{
    	
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftp.connect(host,port);
        System.out.println("FTP URL is:"+ftp.getDefaultPort());
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.login(username, password);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();   
    }    

    // Method to upload the File on the FTP Server
    public void upload(String localFileFullName, String fileName, String hostDir)
            throws Exception
            {
                try {
                InputStream input = new FileInputStream(new File(localFileFullName));
 
                this.ftp.storeFile(hostDir + fileName, input);
                }
                catch(Exception e){
 
                }
            }
 // Method to download the File on the FTP Server
    public void run() {
    	try {
    		long startTime = System.currentTimeMillis() ; 
            FTPFile file = ftp.mlistFile(source);
            Long fileSize= file.getSize();
            System.out.println("Total file size"+fileSize/1024);
            InputStream inputStream = ftp.retrieveFileStream(source);
    		FileOutputStream outputStream = new FileOutputStream(buildPath+binaryName) ; 
    		byte[] buffer = new byte[BUFFER_SIZE] ; 
    		int bytesRead = -1 ; 
    		Long totalBytesRead = 0L ; 
    		while ((bytesRead = inputStream.read(buffer)) != -1) { 
    			outputStream.write(buffer, 0, bytesRead) ; 
    			totalBytesRead+=bytesRead;
    			DecimalFormat format = new DecimalFormat("#0.###");
    			Status.MESSAGE = new StringBuffer("Download in progress. File Name:"+file.getName()+" [Downloaded : "+totalBytesRead+"/"+fileSize+" : "+format.format((double)totalBytesRead/fileSize*100)+"% ]");
    			//System.out.print("\r"+STATUS);
    			} 
    		long endTime = System.currentTimeMillis() ; 
    		System.out.println("File downloaded") ; 
    		Status.MESSAGE = new StringBuffer("Download time in sec. is:-" + (endTime-startTime)/1000) ; 
    		outputStream.close() ; inputStream.close() ; 
    		File downlodedFile = new File(buildPath+binaryName);
    		if (!downlodedFile.exists() || !downlodedFile.isFile()) {
    			Status.MESSAGE = new StringBuffer("Download failed, Please try again"); 
    			return;
    		}
    		else if(downlodedFile.length() != fileSize) {
    			Status.MESSAGE = new StringBuffer("Download failed, Please try again"); 
    			downlodedFile.delete();
    			return;   			
    		}
    		
    		System.out.println(Status.MESSAGE); 
    		Status.MESSAGE = new StringBuffer("File unzip process started"); 
    		System.out.println(Status.MESSAGE); 
    		//TAR.decompress(buildPath+binaryName, new File(buildPath));
    		TAR.unTar(buildPath+binaryName,buildPath);
 			Status.MESSAGE = new StringBuffer("File unzip process completed");
 			System.out.println(Status.MESSAGE); 
			AdbCommunication adb = new AdbCommunication();
			adb.rebootEDL(Status.DEVICE1_ID.toString());
 			Status.MESSAGE = new StringBuffer("Starting flashing");
 			System.out.println(Status.MESSAGE); 
			FlashingAutomation automation = new FlashingAutomation(binaryName.replaceAll(".tar.gz", ""),buildPath);
			automation.start();


    	}
    	catch (Exception e) {
    		e.printStackTrace();
			// TODO: handle exception
		}
    }
    
    
    // list the files in a specified directory on the FTP
    public boolean listFTPFiles(String directory, String fileName) throws IOException {
        // lists files and directories in the current working directory
        boolean verificationFilename = false;        
        FTPFile[] files = ftp.listFiles(directory);
        for (FTPFile file : files) {
            String details = file.getName();                
            System.out.println(details);            
            if(details.equals(fileName))
            {
                System.out.println("Correct Filename");
                verificationFilename=details.equals(fileName);
               //assertTrue("Verification Failed: The filename is not updated at the CDN end.",details.equals(fileName));                
            }
        }  
        
         return verificationFilename;
    }
    
    // Disconnect the connection to FTP
    public void disconnect(){
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
                // do nothing as file is already saved to server
            }
        }
    }
    
     
}