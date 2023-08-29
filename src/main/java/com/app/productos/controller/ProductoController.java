/**
 * 
 */
package com.app.productos.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import com.app.productos.entity.CategoriaProducto;
import com.app.productos.entity.Producto;
import com.app.productos.service.IProductoService;

import jakarta.validation.Valid;

/**
 * 
 */
@RestController
@RequestMapping("/api")
public class ProductoController {

	@Autowired
	private IProductoService productoService;

	@GetMapping("/productos")
	public List<Producto> listarProductos() {
		return productoService.listarProductos();
	}

	@GetMapping("/categoria-productos")
	public List<CategoriaProducto> listarCategorias() {
		return productoService.listarCategorias();
	}

	@PostMapping("/producto")
	public ResponseEntity<?> guardarProducto(@Valid @RequestBody Producto producto, BindingResult result) {
		Producto productoNuevo = null;
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {

			// Se valida si existe un usuario con ese nombre
			List<Producto> listaProductosTemp = productoService.obtenerProductoPorNombre(producto.getNombre());
			if (listaProductosTemp.isEmpty()) {
				productoNuevo = productoService.guardarOActualizarProducto(producto);
			} else {
				response.put("mensaje", "Señor usuario intente con otro nombre de producto.");
				response.put("error", "Ya existe un producto con ese nombre");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (DataAccessException ex) {
			response.put("mensaje", "Errro al realizar el insert en la base de datos");
			response.put("error", ex.getCause() + " :" + ex.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El producto ha sido creado con exito!!");
		response.put("producto", productoNuevo);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/producto/{id}")
	public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {

			Producto producto = productoService.findById(id);
			if (producto == null) {
				response.put("mensaje", "No existe un producto con esa identificacion.");
				response.put("error", "Error producto ingresado no existe en el sistema.");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			productoService.eliminarProducto(id);
		} catch (DataAccessException ex) {
			response.put("mensaje", "Errro al realizar la  eliminacion del producto en la base de datos");
			response.put("error", ex.getCause() + " :" + ex.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El producto ha sido eliminado con exito!!!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PutMapping("/producto/{id}")
	public ResponseEntity<?> actualizarProducto(@Valid @RequestBody Producto producto, BindingResult result,
			@PathVariable Long id) {
		Producto productoActual = productoService.findById(id);
		Producto productoActualizado = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			// Creamos array
			List<String> errors = new ArrayList<>();
			// Obtenemos en una lista los errores con result.getFieldErrors
			// Almacenamos los errores mediante un array
			for (FieldError err : result.getFieldErrors()) {
				errors.add("El campo '" + err.getField() + "' " + err.getDefaultMessage());
			}
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (productoActual == null) {
			// Almacenamos en un map el mensaje de error
			response.put("mensaje", "Error:  no se pudo editar, El ID del producto : "
					.concat(String.valueOf(id).concat(" No existe en la base de datos!")));
			// retornamos codigo 404
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			// Modificamos los datos del producto...
			productoActual.setId(producto.getId());
			productoActual.setNombre(producto.getNombre());
			productoActual.setPrecio(producto.getPrecio());
			productoActual.setCategoria(producto.getCategoria());
			productoActualizado = productoService.guardarOActualizarProducto(productoActual);
		} catch (DataAccessException ex) {
			response.put("mensaje", "Errro al realizar el actualizar en la base de datos");
			response.put("error", ex.getCause() + " :" + ex.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El producto ha sido actualizado con exito!!");
		response.put("producto", productoActualizado);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
}
