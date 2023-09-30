package com.example.webshop.core.jaxrs;

import java.io.IOException;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@PreMatching
@Priority(0)
public class PathFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(PathFilter.class);

    @Override
    public void filter(ContainerRequestContext arg0) throws IOException {
        logger.info("{}: {}.", arg0.getMethod(), arg0.getUriInfo().getAbsolutePath());
    }
}
