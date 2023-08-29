package com.app.productos.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.productos.entity.CategoriaProducto;

public interface ICategoriaProductosDAO extends JpaRepository<CategoriaProducto, Long> {
	
}
