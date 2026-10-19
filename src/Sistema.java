import java.util.*;
class Sistema {
 final Map<String,Orden> ordenes=new LinkedHashMap<>();
 final Map<String,Cuadrilla> cuadrillas=new LinkedHashMap<>();
 final Map<String,Jornada> jornadas=new LinkedHashMap<>();
 private double ingresosRegistro=0,ingresosVisitas=0;
 private final Map<String,Double> ingresosCategoria=new TreeMap<>();
 void semilla(){
  registrarInicial(new Diagnostico("D1","Ana","Zona 1",1,Orden.Zona.METROPOLITANA));
  registrarInicial(new Diagnostico("D2","Luis","Mixco",1.5,Orden.Zona.PERIFERICA));
  registrarInicial(new Instalacion("I1","Marta","Zona 4",2.5,Orden.Zona.METROPOLITANA,5));
  registrarInicial(new Instalacion("I2","Pedro","Villa Nueva",2,Orden.Zona.PERIFERICA,2));
  registrarInicial(new InstalacionCertificada("E1","Sofia","Zona 10",2.5,Orden.Zona.METROPOLITANA,5,2000));
  registrarInicial(new InstalacionCertificada("E2","Jose","Amatitlan",3,Orden.Zona.PERIFERICA,3,1000));
  registrarInicial(new MantenimientoAire("A1","Rosa","Zona 9",2,Orden.Zona.METROPOLITANA,12000));
  registrarInicial(new MantenimientoAire("A2","Carlos","Zona 14",1.5,Orden.Zona.METROPOLITANA,24000));
  registrarCuadrilla(new Cuadrilla("C1",8,true));registrarCuadrilla(new Cuadrilla("C2",6,false));registrarCuadrilla(new Cuadrilla("C3",4,false));
 }
 void registrarInicial(Orden o){if(ordenes.putIfAbsent(o.codigo,o)!=null)throw new IllegalArgumentException("Codigo repetido");}
 void registrar(Orden o){registrarInicial(o);ingresosRegistro+=o.costo();ingresosCategoria.merge(o.categoria(),o.costo(),Double::sum);}
 void registrarCuadrilla(Cuadrilla c){if(cuadrillas.putIfAbsent(c.codigo,c)!=null)throw new IllegalArgumentException("Cuadrilla repetida");}
 Jornada crearJornada(String codigo,String id){if(jornadas.containsKey(codigo))throw new IllegalArgumentException("Jornada repetida");Cuadrilla c=cuadrilla(id);Jornada j=new Jornada(codigo,c);jornadas.put(codigo,j);return j;}
 Orden orden(String id){Orden o=ordenes.get(id);if(o==null)throw new IllegalArgumentException("Orden no encontrada");return o;}
 Cuadrilla cuadrilla(String id){Cuadrilla c=cuadrillas.get(id);if(c==null)throw new IllegalArgumentException("Cuadrilla no encontrada");return c;}
 Jornada jornada(String id){Jornada j=jornadas.get(id);if(j==null)throw new IllegalArgumentException("Jornada no encontrada");return j;}
 void iniciar(String id){Jornada j=jornada(id);double cargo=j.iniciar();ingresosVisitas+=cargo;for(Orden o:j.ordenes())if(o.visitas()>1)ingresosCategoria.merge(o.categoria(),40.0,Double::sum);}
 double registro(){return ingresosRegistro;}
 double adicionales(){return ingresosVisitas;}
 void reporte(){
  System.out.println("ORDENES POR CATEGORIA Y ESTADO:");Map<String,Map<Orden.Estado,Integer>> conteo=new TreeMap<>();
  for(Orden o:ordenes.values())conteo.computeIfAbsent(o.categoria(),k->new EnumMap<>(Orden.Estado.class)).merge(o.estado(),1,Integer::sum);
  conteo.forEach((k,v)->System.out.println(k+": "+v));
  System.out.printf(Locale.US,"Ingresos registro: Q%.2f | visitas: Q%.2f | total: Q%.2f%n",ingresosRegistro,ingresosVisitas,ingresosRegistro+ingresosVisitas);
  System.out.println("Ingresos por categoria: "+ingresosCategoria);
  System.out.println("Jornadas abiertas:");jornadas.values().stream().filter(j->j.estado()!=Jornada.Estado.CERRADA).forEach(System.out::println);
 }
}
