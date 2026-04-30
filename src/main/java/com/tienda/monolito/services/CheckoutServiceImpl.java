package com.tienda.monolito.services;

import com.tienda.monolito.entities.Direccion;
import com.tienda.monolito.entities.Carrito;
import com.tienda.monolito.entities.CarritoItem;
import com.tienda.monolito.common.exception.BusinessException;
import com.tienda.monolito.common.exception.ResourceNotFoundException;
import com.tienda.monolito.entities.DeliveryOrder;
import com.tienda.monolito.entities.EstadoDelivery;
import com.tienda.monolito.entities.Producto;
import com.tienda.monolito.repositories.BilleteraRepository;
import com.tienda.monolito.repositories.ProductoRepository;
import com.tienda.monolito.repositories.CarritoItemRepository;
import com.tienda.monolito.repositories.CarritoRepository;
import com.tienda.monolito.repositories.DeliveryOrderRepository;
import com.tienda.monolito.repositories.DireccionRepository;
import com.tienda.monolito.repositories.TransaccionRepository;
import com.tienda.monolito.repositories.UsuarioRepository;
import com.tienda.monolito.services.interfaces.CheckoutService;
import com.tienda.monolito.entities.Usuario;
import com.tienda.monolito.entities.Billetera;
import com.tienda.monolito.entities.TipoTransaccion;
import com.tienda.monolito.entities.Transaccion;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ProductoRepository productoRepository;
    private final BilleteraRepository billeteraRepository;
    private final TransaccionRepository transaccionRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;
    private final DireccionRepository direccionRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void checkout(Long usuarioId, Long direccionEntregaId) {
        // 1. Obtener carrito y validar que no esté vacío
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado para el usuario: " + usuarioId));

        List<CarritoItem> items = carritoItemRepository.findByCarritoId(carrito.getId());
        if (items.isEmpty()) {
            throw new BusinessException("El carrito está vacío");
        }

        // 2. Validar stock disponible para cada producto
        for (CarritoItem item : items) {
            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                throw new BusinessException("Stock insuficiente para '" + producto.getNombre()
                        + "'. Disponible: " + producto.getStock()
                        + ", requerido: " + item.getCantidad());
            }
        }

        // 3. Calcular total de la compra
        BigDecimal total = items.stream()
                .map(item -> item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Verificar saldo suficiente en la billetera
        Usuario comprador = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        Billetera billetera = billeteraRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new BusinessException("El usuario no tiene billetera. Debe crearla antes de comprar"));

        if (billetera.getSaldo().compareTo(total) < 0) {
            throw new BusinessException("Saldo insuficiente. Saldo: " + billetera.getSaldo()
                    + ", total requerido: " + total);
        }

        // 5. Obtener dirección de entrega
        Direccion direccionEntrega = direccionRepository.findById(direccionEntregaId)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección de entrega no encontrada con id: " + direccionEntregaId));

        // 6. Generar un DeliveryOrder por cada item del carrito
        for (CarritoItem item : items) {
            deliveryOrderRepository.save(DeliveryOrder.builder()
                    .carritoItem(item)
                    .comprador(comprador)
                    .direccionEntrega(direccionEntrega)
                    .estado(EstadoDelivery.PENDIENTE)
                    .build());
        }

        // 7. Actualizar stock (disminuir según lo comprado)
        for (CarritoItem item : items) {
            Producto producto = item.getProducto();
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }

        // 8. Descontar importe total de la billetera
        billetera.setSaldo(billetera.getSaldo().subtract(total));
        billeteraRepository.save(billetera);

        // 9. Registrar transacción de compra
        transaccionRepository.save(Transaccion.builder()
                .billetera(billetera)
                .monto(total)
                .tipo(TipoTransaccion.COMPRA)
                .build());

        // 10. Vaciar el carrito: desvincula items sin eliminarlos (DeliveryOrder los referencia)
        carritoItemRepository.detachFromCarrito(carrito.getId());
    }
}
