package com.example.products;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.example.webshop.products.CategoryDto;
import com.example.webshop.products.CategoryService;

@Path("categories")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    CategoryService service;

    @PathParam("code")
    String code;

    @GET
    public List<CategoryDto> fetch(@QueryParam("since") long since) {
        return service.fetch(since);
    }

    @POST
    public CategoryDto add(@Valid final CategoryDto dto) {
        return service.add(dto);
    }

    @PUT
    @Path("{code}")
    public CategoryDto update(final CategoryDto dto) {
        return service.update(code, dto);
    }

    @DELETE
    @Path("{code}")
    public CategoryDto delete() {
        return service.delete(code);
    }

}
