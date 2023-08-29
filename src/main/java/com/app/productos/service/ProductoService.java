/**
 * 
 */
package com.app.productos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.productos.entity.CategoriaProducto;
import com.app.productos.entity.Producto;
import com.app.productos.persistencia.ICategoriaProductosDAO;
import com.app.productos.persistencia.IProductoDAO;

/**
 * 
 */
@Service
public class ProductoService implements IProductoService {

	@Autowired
	private IProductoDAO productoDAO;

	@Autowired
	private ICategoriaProductosDAO categoriaProductosDAO;

	@Override
	@Transactional(readOnly = true)
	public List<Producto> listarProductos() {
		return productoDAO.listarProductos();
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoriaProducto> listarCategorias() {
		return categoriaProductosDAO.findAll();
	}

	@Override
	@Transactional
	public Producto guardarOActualizarProducto(Producto producto) {
		return productoDAO.save(producto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> obtenerProductoPorNombre(String nombre) {
		return productoDAO.obtenerProductoPorNombre(nombre);
	}

	@Override
	@Transactional
	public void eliminarProducto(Long id) {
		productoDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findById(Long id) {
		return productoDAO.findById(id).orElse(null);
	}
}
