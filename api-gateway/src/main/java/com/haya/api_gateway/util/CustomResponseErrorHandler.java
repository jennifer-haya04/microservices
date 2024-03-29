package com.haya.api_gateway.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CustomResponseErrorHandler implements ResponseErrorHandler {
    StringBuilder body;

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (body==null)
            getBody(response);
    }
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode() != HttpStatus.OK;
    }
    public StringBuilder getBody(ClientHttpResponse response) throws IOException
    {

        body=new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            body.append(line);
            body.append('\n');
            line = bufferedReader.readLine();
        }
        return body;
    }
    public String getBody() {
        if (body==null)
            return null;
        return body.toString();
    }
    public void reset()
    {
        body=null;
    }
}
