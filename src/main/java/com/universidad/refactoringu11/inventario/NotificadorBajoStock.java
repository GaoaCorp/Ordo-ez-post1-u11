package com.universidad.refactoringu11.inventario;

/**
 * Abstraccion propiedad del dominio (inventario). Los paquetes de menor nivel
 * (p. ej. reportes) la implementan; inventario nunca depende de ellos.
 */
public interface NotificadorBajoStock {
    void notificar(Producto producto, int cantidadReorden);
}
