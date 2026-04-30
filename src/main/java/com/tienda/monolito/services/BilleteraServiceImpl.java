package com.tienda.monolito.services;

import com.tienda.monolito.common.exception.BusinessException;
import com.tienda.monolito.common.exception.ResourceNotFoundException;
import com.tienda.monolito.repositories.BilleteraRepository;
import com.tienda.monolito.repositories.TransaccionRepository;
import com.tienda.monolito.repositories.UsuarioRepository;
import com.tienda.monolito.services.interfaces.BilleteraService;
import com.tienda.monolito.entities.Usuario;
import com.tienda.monolito.entities.Billetera;
import com.tienda.monolito.entities.TipoTransaccion;
import com.tienda.monolito.entities.Transaccion;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BilleteraServiceImpl implements BilleteraService {

    private final BilleteraRepository billeteraRepository;
    private final TransaccionRepository transaccionRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${app.wallet.max-carga}")
    private long maxCarga;

    @Override
    @Transactional
    public Billetera getOrCreateBilletera(Long usuarioId) {
        return billeteraRepository.findByUsuarioId(usuarioId).orElseGet(() -> {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
            return billeteraRepository.save(Billetera.builder()
                    .usuario(usuario)
                    .saldo(BigDecimal.ZERO)
                    .build());
        });
    }

    @Override
    public Billetera getBilletera(Long usuarioId) {
        return billeteraRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Billetera no encontrada para el usuario: " + usuarioId));
    }

    @Override
    @Transactional
    public Billetera addFunds(Long usuarioId, BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto a cargar debe ser mayor a cero");
        }
        if (monto.compareTo(BigDecimal.valueOf(maxCarga)) > 0) {
            throw new BusinessException("El monto excede el límite de carga permitido: " + maxCarga);
        }

        Billetera billetera = getOrCreateBilletera(usuarioId);
        billetera.setSaldo(billetera.getSaldo().add(monto));
        billeteraRepository.save(billetera);

        transaccionRepository.save(Transaccion.builder()
                .billetera(billetera)
                .monto(monto)
                .tipo(TipoTransaccion.CARGA)
                .build());

        return billetera;
    }

    @Override
    public List<Transaccion> getTransacciones(Long usuarioId) {
        Billetera billetera = getBilletera(usuarioId);
        return transaccionRepository.findByBilleteraIdOrderByFechaDesc(billetera.getId());
    }
}
