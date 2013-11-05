package com.example.MultipartSpice.responses;

/**
 * Copyright (c) 2013 MadeByMany. All rights reserved.
 * Project: MultipartSpice
 * Package: com.example.MultipartSpice.responses
 * User: Nelson Sachse
 */
public class UploadResponse {
    private String mErrors;

    public UploadResponse(String errors){

        if(errors != null){
            mErrors = errors;
        }
    }

    public boolean hasErrors(){
        return mErrors != null;
    }

    public String getErrors(){
        return mErrors;
    }
}
