package com.bolsadeideas.springboot.webflux.app;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.models.services.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class SpringBootWebfluxApirestApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApirestApplication.class);

    @Autowired
    private ProductoService service;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApirestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        mongoTemplate.dropCollection("productos").subscribe();
        mongoTemplate.dropCollection("categorias").subscribe();

        Categoria electronico = new Categoria("Electronico");
        Categoria deporte = new Categoria("Deporte");
        Categoria computacion = new Categoria("Computacion");
        Categoria muebles = new Categoria("Muebles");

        Flux.just(electronico,deporte,computacion,muebles)
                .flatMap(this.service::saveCategoria)
                .doOnNext(c ->{
                    log.info( "Categoria creada: "+ c.getNombre() + ", Id : "+ c.getId());
                }).thenMany(

                        Flux.just(new Producto("TV",456.69,electronico),
                                        new Producto("Radio", 10.00, electronico),
                                        new Producto("Mesa",400.00, muebles),
                                        new Producto("Bianchi Bicicleta", 20.00, deporte),
                                        new Producto("TV PP",454.69,electronico),
                                        new Producto("Radio POP", 11.00,electronico))
                                .flatMap(producto -> {
                                    producto.setCreateAt(new Date());
                                    return service.save(producto);
                                })
                )
                .subscribe(producto -> log.info("Insert : "+ producto.getId() + "  "+ producto.getNombre()));;
    }
}
