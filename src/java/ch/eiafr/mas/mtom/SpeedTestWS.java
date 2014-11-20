/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.eiafr.mas.mtom;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.MTOM;

/**
 *
 * @author sstauffe
 * Order of operations:
 *
 *  - Client Start timer
 *  - Client Upload in sync mode / Client Download file
 *  - Server release call
 *  - Stop timer
 *  - calc speed
 * 
  */

@WebService(serviceName = "SpeedTestWS")
@MTOM(enabled = true, threshold = 10240)
public class SpeedTestWS
{

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt)
    {
        return "Hello " + txt + " !";
    }
    
    @WebMethod(operationName = "upload")
    public void upload(String fileName, byte[] imageBytes) {
         
        String filePath = "c:/Temp/Upload/" + fileName;
         
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            try (BufferedOutputStream outputStream = new BufferedOutputStream(fos))
            {
                outputStream.write(imageBytes);
            }
             
            System.out.println("Received file: " + filePath);
             
        } catch (IOException ex) {
            System.err.println(ex);
            throw new WebServiceException(ex);
        }
    }
     
    @WebMethod(operationName = "download")
    public byte[] download(String fileName) {
        String filePath = "c:/Temp/Download/" + fileName;
        System.out.println("Sending file: " + filePath);
         
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            byte[] fileBytes;
            try (BufferedInputStream inputStream = new BufferedInputStream(fis))
            {
                fileBytes = new byte[(int) file.length()];
                inputStream.read(fileBytes);
            }
             
            return fileBytes;
        } catch (IOException ex) {
            System.err.println(ex);
            throw new WebServiceException(ex);
        }      
    }
    
}
