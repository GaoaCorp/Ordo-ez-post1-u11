package com.universidad.refactoringu11.inventario;

import com.universidad.refactoringu11.reportes.ReporteBajoStockService; // inventario ahora importa reportes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    private static final String ENTRADA = "ENTRADA";
    private static final String SALIDA = "SALIDA";
    private static final int FACTOR_REORDEN = 3;

    private final List<Producto> productos = new ArrayList<>();
    private final List<String> movimientos = new ArrayList<>();

    private ReporteBajoStockService reporteBajoStockService; // colaborador opcional, sin final

    // @Lazy: Spring Boot 2.6+ prohibe referencias circulares entre beans; el proxy
    // perezoso permite arrancar el contexto, pero NO elimina el ciclo entre paquetes.
    @Autowired(required = false)
    public void setReporteBajoStockService(@Lazy ReporteBajoStockService reporteBajoStockService) {
        this.reporteBajoStockService = reporteBajoStockService;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> productosBajoStockMinimo() {
        return productos.stream().filter(this::estaBajoStockMinimo).toList();
    }

    public String actualizarInventario(Long productoId, int cantidad, String tipoMovimiento) {
        if (!esSolicitudValida(productoId, cantidad, tipoMovimiento)) {
            return "ERROR_DATOS_INVALIDOS";
        }
        return buscarProducto(productoId)
                .map(producto -> aplicarMovimiento(producto, cantidad, tipoMovimiento))
                .orElse("ERROR_PRODUCTO_NO_EXISTE");
    }

    // --- Validacion de entrada ---
    private boolean esSolicitudValida(Long productoId, int cantidad, String tipoMovimiento) {
        return productoId != null && cantidad != 0
                && (tipoMovimiento.equals(ENTRADA) || tipoMovimiento.equals(SALIDA));
    }

    // --- Busqueda ---
    private Optional<Producto> buscarProducto(Long productoId) {
        return productos.stream().filter(p -> p.getId().equals(productoId)).findFirst();
    }

    // --- Orquestacion del movimiento sobre un producto existente ---
    private String aplicarMovimiento(Producto producto, int cantidad, String tipoMovimiento) {
        int nuevoStock = calcularNuevoStock(producto.getStock(), cantidad, tipoMovimiento);
        if (nuevoStock < 0) {
            return "ERROR_STOCK_INSUFICIENTE";
        }
        producto.setStock(nuevoStock);
        registrarMovimiento(producto, cantidad, tipoMovimiento);
        if (estaBajoStockMinimo(producto)) {
            int cantidadReorden = calcularCantidadReorden(producto);
            alertarBajoStock(producto, cantidadReorden);
            return "OK_BAJO_STOCK_REORDEN_" + cantidadReorden;
        }
        return "OK_" + nuevoStock;
    }

    // --- Actualizacion de stock ---
    private int calcularNuevoStock(int stockActual, int cantidad, String tipoMovimiento) {
        return tipoMovimiento.equals(ENTRADA) ? stockActual + cantidad : stockActual - cantidad;
    }

    // --- Politica de reorden (Decision de diseno 1: metodo privado, ver README) ---
    private boolean estaBajoStockMinimo(Producto producto) {
        return producto.getStock() < producto.getStockMinimo();
    }

    private int calcularCantidadReorden(Producto producto) {
        return producto.getStockMinimo() * FACTOR_REORDEN - producto.getStock();
    }

    // --- Auditoria ---
    private void registrarMovimiento(Producto producto, int cantidad, String tipoMovimiento) {
        String registro = tipoMovimiento + " de " + cantidad + " unidades de "
                + producto.getNombre() + " -> stock final: " + producto.getStock();
        movimientos.add(registro);
        System.out.println(registro);
    }

    // --- Notificacion ---
    private void alertarBajoStock(Producto producto, int cantidadReorden) {
        if (reporteBajoStockService != null) {
            System.out.println(reporteBajoStockService.generarReporte());
        }
        System.out.println("ALERTA: " + producto.getNombre()
                + " bajo stock minimo. Reordenar " + cantidadReorden + " unidades.");
    }
}
