import java.util.*;

abstract class Orden {
 enum Estado { PENDIENTE, ASIGNADA, EN_ATENCION, COMPLETADA, CANCELADA }
 enum Zona { METROPOLITANA, PERIFERICA }
 final String codigo, cliente, direccion;
 final double horas;
 final Zona zona;
 private Estado estado=Estado.PENDIENTE;
 private int visitas=0;
 Orden(String c,String n,String d,double h,Zona z){
  if(c==null||c.isBlank()||n==null||n.isBlank()||d==null||d.isBlank()||z==null||!Double.isFinite(h)||h<=0) throw new IllegalArgumentException("Datos de orden invalidos");
  codigo=c.trim();cliente=n.trim();direccion=d.trim();horas=h;zona=z;
 }
 abstract double costo();
 abstract int dias();
 abstract int maxVisitas();
 boolean compatible(Cuadrilla c){return true;}
 String categoria(){return getClass().getSimpleName();}
 Estado estado(){return estado;}
 int visitas(){return visitas;}
 void asignar(){exigir(Estado.PENDIENTE);estado=Estado.ASIGNADA;}
 void retirar(){exigir(Estado.ASIGNADA);estado=Estado.PENDIENTE;}
 void iniciar(){exigir(Estado.ASIGNADA);visitas++;estado=Estado.EN_ATENCION;}
 void finalizar(boolean exito){exigir(Estado.EN_ATENCION);estado=exito?Estado.COMPLETADA:(visitas>=maxVisitas()?Estado.CANCELADA:Estado.PENDIENTE);}
 void exigir(Estado e){if(estado!=e)throw new IllegalStateException("La orden "+codigo+" debe estar "+e+" (actual: "+estado+")");}
 public String toString(){return codigo+" | "+categoria()+" | "+cliente+" | "+zona+" | "+estado+" | visitas "+visitas+" | Q"+String.format(Locale.US,"%.2f",costo());}
}
class Diagnostico extends Orden {
 Diagnostico(String c,String n,String d,double h,Zona z){super(c,n,d,h,z);if(h>1.5)throw new IllegalArgumentException("Diagnostico: maximo 1.5 horas");}
 double costo(){return zona==Zona.METROPOLITANA?150:225;}
 int dias(){return zona==Zona.METROPOLITANA?1:2;}
 int maxVisitas(){return 3;}
}
class Instalacion extends Orden {
 final int puntos;
 Instalacion(String c,String n,String d,double h,Zona z,int p){super(c,n,d,h,z);if(p<=0)throw new IllegalArgumentException("Puntos deben ser positivos");puntos=p;}
 double costo(){return (zona==Zona.METROPOLITANA?100:175)+90*Math.ceil(Math.max(horas,puntos*.75));}
 int dias(){return zona==Zona.METROPOLITANA?3:5;}
 int maxVisitas(){return 3;}
}
class InstalacionCertificada extends Instalacion {
 final double materiales;
 InstalacionCertificada(String c,String n,String d,double h,Zona z,int p,double m){super(c,n,d,h,z,p);if(!Double.isFinite(m)||m<=0)throw new IllegalArgumentException("Materiales deben ser positivos");materiales=m;}
 @Override double costo(){return super.costo()+120+materiales*.05;}
 @Override int dias(){return super.dias()+2;}
 @Override int maxVisitas(){return 2;}
}
class MantenimientoAire extends Orden {
 final int btu;
 MantenimientoAire(String c,String n,String d,double h,Zona z,int b){super(c,n,d,h,z);if(z!=Zona.METROPOLITANA||b<9000||b>60000)throw new IllegalArgumentException("Aire: solo zona metropolitana y BTU 9000..60000");btu=b;}
 double costo(){return 100+110*horas+80;}
 int dias(){return 2;}
 int maxVisitas(){return 1;}
 @Override boolean compatible(Cuadrilla c){return c.refrigeracion;}
}
