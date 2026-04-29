package com.tienda.monolito.cart.service.impl;

import com.tienda.monolito.cart.entity.Carrito;
import com.tienda.monolito.cart.entity.CarritoItem;
import com.tienda.monolito.cart.repository.CarritoItemRepository;
import com.tienda.monolito.cart.repository.CarritoRepository;
import com.tienda.monolito.cart.service.CarritoService;
import com.tienda.monolito.common.exception.BusinessException;
import com.tienda.monolito.common.exception.ResourceNotFoundException;
import com.tienda.monolito.product.entity.Producto;
import com.tienda.monolito.product.repository.ProductoRepository;
import com.tienda.monolito.user.entity.Usuario;
import com.tienda.monolito.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public Carrito getOrCreateCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId).orElseGet(() -> {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
            return carritoRepository.save(Carrito.builder().usuario(usuario).build());
        });
    }

    @Override
    public Carrito getCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado para el usuario: " + usuarioId));
    }

    @Override
    public List<CarritoItem> getItems(Long usuarioId) {
        Carrito carrito = getCarrito(usuarioId);
        return carritoItemRepository.findByCarritoId(carrito.getId());
    }

    @Override
    @Transactional
    public CarritoItem addItem(Long usuarioId, Long productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a cero");
        }

        Carrito carrito = getOrCreateCarrito(usuarioId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));

        if (!producto.getActivo()) {
            throw new BusinessException("El producto no está disponible para la venta");
        }

        // Si el producto ya está en el carrito, suma la cantidad
        Optional<CarritoItem> existente = carritoItemRepository
                .findByCarritoIdAndProductoId(carrito.getId(), productoId);

        if (existente.isPresent()) {
            CarritoItem item = existente.get();
            int nuevaCantidad = item.getCantidad() + cantidad;
            if (producto.getStock() < nuevaCantidad) {
                throw new BusinessException("Stock insuficiente. Disponible: " + producto.getStock());
            }
            item.setCantidad(nuevaCantidad);
            return carritoItemRepository.save(item);
        }

        if (producto.getStock() < cantidad) {
            throw new BusinessException("Stock insuficiente. Disponible: " + producto.getStock());
        }

        CarritoItem item = CarritoItem.builder()
                .carrito(carrito)
                .producto(producto)
                .cantidad(cantidad)
                .precioUnitario(producto.getPrecio())
                .build();

        return carritoItemRepository.save(item);
    }

    @Override
    @Transactional
    public CarritoItem updateItem(Long carritoItemId, int cantidad) {
        if (cantidad <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a cero");
        }

        CarritoItem item = carritoItemRepository.findById(carritoItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado con id: " + carritoItemId));

        if (item.getProducto().getStock() < cantidad) {
            throw new BusinessException("Stock insuficiente. Disponible: " + item.getProducto().getStock());
        }

        item.setCantidad(cantidad);
        return carritoItemRepository.save(item);
    }

    @Override
    @Transactional
    public void removeItem(Long carritoItemId) {
        if (!carritoItemRepository.existsById(carritoItemId)) {
            throw new ResourceNotFoundException("Item no encontrado con id: " + carritoItemId);
        }
        carritoItemRepository.deleteById(carritoItemId);
    }
}
