package com.bolsadeideas.springboot.webflux.app.controllers;

import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.models.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;

@RestController
@RequestMapping("api/productos")
public class ProductoController {

     @Autowired
     private ProductoService service;

     @GetMapping
     public Mono<ResponseEntity<Flux<Producto>>> listar(){
         return Mono.just(
                 ResponseEntity.ok()
                         .contentType(MediaType.APPLICATION_JSON_UTF8)
                         .body(service.findAll())
         );
     }

     @GetMapping("/{id}")
     public Mono<ResponseEntity<Producto>> ver(@PathVariable String id){
         return service.findById(id)
                 .map(p -> ResponseEntity.ok()
                         .contentType(MediaType.APPLICATION_JSON_UTF8)
                         .body(p)
                 ).defaultIfEmpty(ResponseEntity.notFound().build());
     }


     @PostMapping
     public Mono<ResponseEntity<Producto>> crear(@RequestBody Producto producto){
         if (producto.getCreateAt() == null){
             producto.setCreateAt(new Date());
         }
         return service.save(producto)
                 .map(p -> ResponseEntity.created(URI.create("/api/productos".concat(p.getId())))
                         .contentType(MediaType.APPLICATION_JSON_UTF8)
                         .body(p)
                 );
     }

     @PutMapping("/{id}")
     public Mono<ResponseEntity<Producto>> editar(@RequestBody Producto producto, @PathVariable String id){
         return this.service.findById(id)
                 .flatMap(p -> {
                     p.setNombre(producto.getNombre());
                     p.setPrecio(producto.getPrecio());
                     p.setCategoria(producto.getCategoria());
                     return service.save(p);
                 }).map(p-> ResponseEntity.created(URI.create("/api/productos".concat(p.getId())))
                         .contentType(MediaType.APPLICATION_JSON_UTF8)
                         .body(p)
                 ).defaultIfEmpty(ResponseEntity.notFound().build());
     }

     @DeleteMapping("/{id}")
     public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id){
         return service.findById(id).flatMap(p->{
             return service.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
         }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
     }



}
