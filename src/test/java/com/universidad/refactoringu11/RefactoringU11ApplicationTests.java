package com.universidad.refactoringu11;

import com.universidad.refactoringu11.inventario.InventarioService;
import com.universidad.refactoringu11.inventario.Producto;
import com.universidad.refactoringu11.inventario.TipoProducto;
import com.universidad.refactoringu11.reportes.ReporteBajoStockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RefactoringU11ApplicationTests {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ReporteBajoStockService reporteBajoStockService;

    @Test
    void contextLoads() {
    }

    @Test
    void springInyectaElNotificadorEnInventario() {
        inventarioService.agregarProducto(new Producto(7L, "Cafe", TipoProducto.ALIMENTO, 15.0, 6, 5));
        assertEquals("OK_BAJO_STOCK_REORDEN_11", inventarioService.actualizarInventario(7L, 2, "SALIDA"));
        assertEquals(1, reporteBajoStockService.getAlertas().size());
    }
}
