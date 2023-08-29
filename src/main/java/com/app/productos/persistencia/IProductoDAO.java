/**
 * 
 */
package com.app.productos.persistencia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.productos.entity.Producto;

/**
 * 
 */
public interface IProductoDAO extends JpaRepository<Producto, Long> {
	@Query("from Producto")
	List<Producto> listarProductos();
	
	@Query("select p from Producto p where p.nombre = ?1")
	List<Producto> obtenerProductoPorNombre(String nombre);
	
}
