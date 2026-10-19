import java.util.*;
public class Pruebas {
 static int ok=0;
 static void igual(double a,double b){if(Math.abs(a-b)>1e-8)throw new AssertionError(a+" != "+b);ok++;}
 static void es(boolean b){if(!b)throw new AssertionError("Condicion falsa");ok++;}
 static void falla(Runnable r){try{r.run();throw new AssertionError("Debio fallar");}catch(IllegalArgumentException|IllegalStateException expected){ok++;}}
 public static void main(String[] args){
  var m=Orden.Zona.METROPOLITANA;var p=Orden.Zona.PERIFERICA;
  igual(new Diagnostico("d","x","y",1,m).costo(),150);igual(new Diagnostico("e","x","y",1,p).costo(),225);
  var i=new Instalacion("i","x","y",2.5,m,5);igual(i.costo(),460);igual(new Instalacion("j","x","y",1.1,p,1).costo(),355);
  var e=new InstalacionCertificada("e","x","y",2.5,m,5,2000);igual(e.costo(),680);
  igual(new MantenimientoAire("a","x","y",2,m,12000).costo(),400);
  es(new Diagnostico("d","x","y",1,p).dias()==2);es(i.dias()==3);es(e.dias()==5);es(new InstalacionCertificada("f","x","y",1,p,1,100).dias()==7);es(new MantenimientoAire("a","x","y",1,m,9000).dias()==2);
  falla(()->new Diagnostico("d","x","y",1.6,m));falla(()->new MantenimientoAire("a","x","y",1,p,12000));
  Sistema s=new Sistema();s.semilla();igual(s.registro(),0);igual(s.adicionales(),0);
  Cuadrilla no=s.cuadrilla("C2");Jornada j=s.crearJornada("J1","C2");falla(()->j.asignar(s.orden("A1")));j.asignar(s.orden("I1"));es(s.orden("I1").estado()==Orden.Estado.ASIGNADA);j.retirar("I1");igual(s.registro(),0);j.asignar(s.orden("I1"));s.iniciar("J1");igual(s.adicionales(),0);j.resultado("I1",false);es(j.estado()==Jornada.Estado.CERRADA);Jornada j2=s.crearJornada("J2","C2");j2.asignar(s.orden("I1"));s.iniciar("J2");igual(s.adicionales(),40);j2.resultado("I1",false);Jornada j3=s.crearJornada("J3","C2");j3.asignar(s.orden("I1"));s.iniciar("J3");igual(s.adicionales(),80);j3.resultado("I1",false);es(s.orden("I1").estado()==Orden.Estado.CANCELADA);falla(()->j3.asignar(s.orden("I1")));igual(s.registro(),0);System.out.println("PRUEBAS CORRECTAS: "+ok);
 }
}
