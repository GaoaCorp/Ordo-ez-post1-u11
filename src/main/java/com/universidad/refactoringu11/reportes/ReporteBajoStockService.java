package com.universidad.refactoringu11.reportes;

import com.universidad.refactoringu11.inventario.InventarioService;
import com.universidad.refactoringu11.inventario.NotificadorBajoStock;
import com.universidad.refactoringu11.inventario.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ReporteBajoStockService implements NotificadorBajoStock {

    private final InventarioService inventarioService;
    private final List<String> alertas = new ArrayList<>();

    public ReporteBajoStockService(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Override
    public void notificar(Producto producto, int cantidadReorden) {
        alertas.add(producto.getNombre() + " -> reordenar " + cantidadReorden + " unidades");
    }

    public List<String> getAlertas() {
        return Collections.unmodifiableList(alertas);
    }

    public String generarReporte() {
        List<Producto> bajoStock = inventarioService.productosBajoStockMinimo();
        StringBuilder sb = new StringBuilder();
        if (bajoStock.isEmpty()) {
            sb.append("REPORTE BAJO STOCK: sin productos bajo el minimo");
        } else {
            sb.append("REPORTE BAJO STOCK (").append(bajoStock.size()).append("):");
            for (Producto p : bajoStock) {
                sb.append(System.lineSeparator())
                  .append(" - ").append(p.getNombre())
                  .append(": stock ").append(p.getStock())
                  .append(" / minimo ").append(p.getStockMinimo());
            }
        }
        for (String alerta : alertas) {
            sb.append(System.lineSeparator()).append(" * alerta: ").append(alerta);
        }
        return sb.toString();
    }
}
