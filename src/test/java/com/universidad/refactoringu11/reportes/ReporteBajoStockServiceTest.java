package com.universidad.refactoringu11.reportes;

import com.universidad.refactoringu11.inventario.InventarioService;
import com.universidad.refactoringu11.inventario.Producto;
import com.universidad.refactoringu11.inventario.TipoProducto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReporteBajoStockServiceTest {

    private InventarioService inventario;
    private ReporteBajoStockService reportes;

    @BeforeEach
    void setUp() {
        inventario = new InventarioService();
        reportes = new ReporteBajoStockService(inventario);
        inventario.setNotificador(reportes); // inyeccion manual de la abstraccion
        inventario.agregarProducto(new Producto(1L, "Clean Code", TipoProducto.LIBRO, 45.0, 20, 5));
        inventario.agregarProducto(new Producto(2L, "Teclado", TipoProducto.ELECTRONICO, 100.0, 10, 2));
    }

    @Test
    void sinMovimientos_reporteIndicaQueNoHayProductosBajoMinimo() {
        assertEquals("REPORTE BAJO STOCK: sin productos bajo el minimo", reportes.generarReporte());
        assertTrue(reportes.getAlertas().isEmpty());
    }

    @Test
    void salidaBajoElMinimo_notificaUnaVezConLaCantidadDeReorden() {
        assertEquals("OK_BAJO_STOCK_REORDEN_12", inventario.actualizarInventario(1L, 17, "SALIDA"));

        assertEquals(1, reportes.getAlertas().size());
        assertEquals("Clean Code -> reordenar 12 unidades", reportes.getAlertas().get(0));
        String reporte = reportes.generarReporte();
        assertTrue(reporte.contains("Clean Code: stock 3 / minimo 5"));
        assertFalse(reporte.contains("Teclado"));
    }

    @Test
    void salidaQueNoBajaDelMinimo_noNotifica() {
        assertEquals("OK_5", inventario.actualizarInventario(2L, 5, "SALIDA"));
        assertTrue(reportes.getAlertas().isEmpty());
    }
}
