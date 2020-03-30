package com.example.secondmaildemo;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@Service
public class EmailUtility {
    public byte[] Base64ToBytes(String base64){
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        return decodedBytes;
    }

    public InputStream BytesToStream(byte[] input){
        return new ByteArrayInputStream(input);
    }

    public InputStream Base64ToInputStream(String base64){
        return BytesToStream(Base64ToBytes(base64));
    }

}
