import java.util.*;
class Jornada {
 enum Estado { PREPARACION, INICIADA, CERRADA }
 final String codigo;
 final Cuadrilla cuadrilla;
 private Estado estado=Estado.PREPARACION;
 private final LinkedHashMap<String,Orden> ordenes=new LinkedHashMap<>();
 private final Set<String> resueltas=new HashSet<>();
 Jornada(String c,Cuadrilla q){if(c==null||c.isBlank())throw new IllegalArgumentException("Codigo vacio");codigo=c.trim();cuadrilla=q;q.ocupar(this);}
 Estado estado(){return estado;}
 Collection<Orden> ordenes(){return Collections.unmodifiableCollection(ordenes.values());}
 double horas(){return ordenes.values().stream().mapToDouble(o->o.horas).sum();}
 double disponible(){return cuadrilla.capacidad-horas();}
 void asignar(Orden o){preparacion();o.exigir(Orden.Estado.PENDIENTE);if(!o.compatible(cuadrilla))throw new IllegalArgumentException("Cuadrilla incompatible");if(o.horas>disponible()+1e-9)throw new IllegalArgumentException("Capacidad excedida");if(ordenes.containsKey(o.codigo))throw new IllegalArgumentException("Orden duplicada");o.asignar();ordenes.put(o.codigo,o);}
 void retirar(String c){preparacion();Orden o=ordenes.get(c);if(o==null)throw new IllegalArgumentException("Orden no asignada");o.retirar();ordenes.remove(c);}
 double iniciar(){preparacion();if(ordenes.isEmpty())throw new IllegalStateException("Jornada vacia");double cargo=0;for(Orden o:ordenes.values()){if(o.visitas()>0)cargo+=40;o.iniciar();}estado=Estado.INICIADA;return cargo;}
 void resultado(String c,boolean exito){if(estado!=Estado.INICIADA)throw new IllegalStateException("Jornada no iniciada");Orden o=ordenes.get(c);if(o==null||resueltas.contains(c))throw new IllegalArgumentException("Orden inexistente o ya resuelta");o.finalizar(exito);resueltas.add(c);if(resueltas.size()==ordenes.size()){estado=Estado.CERRADA;cuadrilla.liberar(this);}}
 void preparacion(){if(estado!=Estado.PREPARACION)throw new IllegalStateException("Jornada no esta en preparacion");}
 public String toString(){return codigo+" | "+estado+" | cuadrilla "+cuadrilla.codigo+" | horas "+horas()+" | disponibles "+disponible();}
}
