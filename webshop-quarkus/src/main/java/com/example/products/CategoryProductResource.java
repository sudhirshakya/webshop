package com.example.products;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.example.webshop.products.ProductDto;
import com.example.webshop.products.ProductService;

@Path("categories/{categoryCode}/products")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryProductResource {

    @Inject
    ProductService service;

    @PathParam("categoryCode")
    String categoryCode;

    @PathParam("code")
    String code;

    @GET
    public List<ProductDto> fetch() {
        return service.fetch(categoryCode);
    }

    @POST
    public ProductDto add(@Valid final ProductDto dto) {
        return service.add(categoryCode, dto);
    }

    @PATCH
    @Path("{code}")
    public ProductDto update(final JsonObject dto) {
        return service.update(categoryCode, code, dto);
    }

    @DELETE
    @Path("{code}")
    public ProductDto delete() {
        return service.delete(categoryCode, code);
    }
}
