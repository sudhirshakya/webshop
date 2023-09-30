package com.example.webshop.core.jaxrs;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger logger = LoggerFactory.getLogger(ConstraintViolationExceptionMapper.class);

    @Context
    private UriInfo uri;

    @Context
    private Request request;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<ResponseError> errors = exception.getConstraintViolations()
                .stream()
                .map(ResponseError::fromConstraintViolation)
                .collect(Collectors.toList());
        logger.warn("Validation error processing {} request to {}: {}.", request.getMethod(), uri.getPath(), errors);
        return Response.status(Status.BAD_REQUEST)
                .entity(errors)
                .build();
    }

    public static class ResponseError {
        public String path;
        public String message;
        public String value;

        public static ResponseError fromConstraintViolation(ConstraintViolation<?> violation) {
            var error = new ResponseError();
            String path = StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                    .filter(node -> node.getKind() == ElementKind.PROPERTY)
                    .map(Path.Node::getName)
                    .collect(Collectors.joining("."));
            error.path = path;
            error.message = violation.getMessage();
            if (violation.getInvalidValue() != null)
                error.value = violation.getInvalidValue().toString();
            return error;
        }

        @Override
        public String toString() {
            return String.format("path=%s, message=%s, value='%s'", path, message, value);
        }
    }
}
