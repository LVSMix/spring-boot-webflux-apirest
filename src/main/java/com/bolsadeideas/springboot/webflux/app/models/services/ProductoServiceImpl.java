package com.bolsadeideas.springboot.webflux.app.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.webflux.app.models.dao.CategoriaDAO;
import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDAO;
import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {
	
	@Autowired
	private ProductoDAO dao;
	
	@Autowired
	private CategoriaDAO categoriaDAO;

	@Override
	public Flux<Producto> findAll() {
		return dao.findAll();
	}

	@Override
	public Mono<Producto> findById(String id) {
		return dao.findById(id);
	}

	@Override
	public Mono<Producto> save(Producto producto) {
		return dao.save(producto);
	}

	@Override
	public Mono<Void> delete(Producto producto) {
		return dao.delete(producto);
	}

	@Override
	public Flux<Producto> findAllConNombreUpperCase() {
		return dao.findAll().map(producto ->{
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		});
	}
	
	@Override
	public Flux<Producto> findAllConNombreUpperCaseConRepeat() {
		return findAllConNombreUpperCase().repeat(5000);
	}

	@Override
	public Flux<Categoria> findAllCategoria() {
		return categoriaDAO.findAll();
	}

	@Override
	public Mono<Categoria> findCategoriaById(String id) {
		return categoriaDAO.findById(id);
	}

	@Override
	public Mono<Categoria> saveCategoria(Categoria categoria) {
		return categoriaDAO.save(categoria);
	}

}
