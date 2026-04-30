/*package com.tienda.monolito.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parametros")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Parametros {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  @Column(nullable = false)
  private String bbdd; //FIXME - Poner los parámetros reales de configuración de la bbdd

  @Column(nullable = false)
  private Integer maximo_intentos_login;

  @Column(nullable = false)
  private Integer maximo_tiempo_sesion;

  @Column(nullable = false)
  private Integer limites_carga_biletera_virtual;

  @Column(nullable = false)
  private String moneda_billetera_virtual; //FIXME - Tabla y entidad monedas, probablemente

  @Column(nullable = false)
  private Integer maximo_resultados_busqueda; //FIXME - la cantidad máxima de resultados debería depender del listado

  @Column(nullable = false)
  private String notificaciones; //FIXME - No especifica

  @Column(nullable = false)
  private Integer firebase_project_id;

  @Column(nullable = false)
  private String API_key;

}
  */
