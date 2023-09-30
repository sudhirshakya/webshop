package com.example.webshop.core.jaxrs;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(BadRequestException exception) {
        var response = new ResponseError();
        response.error = exception.getMessage();
        return Response.status(Status.BAD_REQUEST)
                .entity(response)
                .build();
    }

    public static class ResponseError {
        public String error;
    }
}
