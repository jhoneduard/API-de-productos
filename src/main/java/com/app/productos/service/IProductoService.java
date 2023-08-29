/**
 * 
 */
package com.app.productos.service;

import java.util.List;

import com.app.productos.entity.CategoriaProducto;
import com.app.productos.entity.Producto;

/**
 * 
 */
public interface IProductoService {
	List<Producto> listarProductos();

	List<CategoriaProducto> listarCategorias();

	Producto guardarOActualizarProducto(Producto producto);

	List<Producto> obtenerProductoPorNombre(String nombre);

	public void eliminarProducto(Long id);

	public Producto findById(Long id);
}
