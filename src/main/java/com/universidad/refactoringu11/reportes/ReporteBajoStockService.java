package com.universidad.refactoringu11.reportes;

import com.universidad.refactoringu11.inventario.InventarioService;
import com.universidad.refactoringu11.inventario.Producto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteBajoStockService {

    private final InventarioService inventarioService;

    public ReporteBajoStockService(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    public String generarReporte() {
        List<Producto> bajoStock = inventarioService.productosBajoStockMinimo();
        if (bajoStock.isEmpty()) {
            return "REPORTE BAJO STOCK: sin productos bajo el minimo";
        }
        StringBuilder sb = new StringBuilder("REPORTE BAJO STOCK (" + bajoStock.size() + "):");
        for (Producto p : bajoStock) {
            sb.append(System.lineSeparator())
              .append(" - ").append(p.getNombre())
              .append(": stock ").append(p.getStock())
              .append(" / minimo ").append(p.getStockMinimo());
        }
        return sb.toString();
    }
}
