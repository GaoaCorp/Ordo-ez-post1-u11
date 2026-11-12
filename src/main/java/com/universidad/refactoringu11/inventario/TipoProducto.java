package com.universidad.refactoringu11.inventario;

/**
 * Cada tipo de producto encapsula su propio comportamiento (Replace Conditional
 * with Polymorphism). Agregar un tipo nuevo obliga, en tiempo de compilacion,
 * a implementar ambos metodos abstractos, sin tocar ClasificadorProducto ni
 * EtiquetadoProducto.
 */
public enum TipoProducto {

    LIBRO {
        @Override
        public double recargoEnvio(double precio) { return 0.0; }

        @Override
        public String etiqueta(String nombre) { return "[LIBRO] " + nombre + " - Exento de IVA"; }
    },
    ELECTRONICO {
        @Override
        public double recargoEnvio(double precio) { return precio * 0.08; }

        @Override
        public String etiqueta(String nombre) { return "[ELECTRO] " + nombre + " - Garantia 1 ano"; }
    },
    ROPA {
        @Override
        public double recargoEnvio(double precio) { return precio * 0.03; }

        @Override
        public String etiqueta(String nombre) { return "[ROPA] " + nombre + " - Ver talla"; }
    },
    ALIMENTO {
        @Override
        public double recargoEnvio(double precio) { return 2.50; }

        @Override
        public String etiqueta(String nombre) { return "[ALIMENTO] " + nombre + " - Perecedero"; }
    };

    /** Recargo de envio a sumar al precio del producto. */
    public abstract double recargoEnvio(double precio);

    /** Texto de la etiqueta del producto. */
    public abstract String etiqueta(String nombre);
}
