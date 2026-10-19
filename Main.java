import java.util.*;
public class Main {
 static final Scanner in=new Scanner(System.in);
 static final Sistema s=new Sistema();
 static String pedir(String p){System.out.print(p+": ");return in.nextLine().trim();}
 static double numero(String p){double v=Double.parseDouble(pedir(p));if(!Double.isFinite(v)||v<=0)throw new IllegalArgumentException("Numero debe ser positivo");return v;}
 static int entero(String p){int v=Integer.parseInt(pedir(p));if(v<=0)throw new IllegalArgumentException("Entero positivo requerido");return v;}
 static Orden.Zona zona(){return switch(pedir("Zona (M=metropolitana, P=periferica)").toUpperCase()){case "M"->Orden.Zona.METROPOLITANA;case "P"->Orden.Zona.PERIFERICA;default->throw new IllegalArgumentException("Zona invalida");};}
 static Orden capturar(){String t=pedir("Categoria (D diagnostico, I instalacion, E certificada, A aire)").toUpperCase();if(!List.of("D","I","E","A").contains(t))throw new IllegalArgumentException("Categoria invalida");String c=pedir("Codigo");if(s.ordenes.containsKey(c))throw new IllegalArgumentException("Codigo repetido");String n=pedir("Cliente"),d=pedir("Direccion");double h=numero("Horas estimadas");Orden.Zona z=zona();return switch(t){case "D"->new Diagnostico(c,n,d,h,z);case "I"->new Instalacion(c,n,d,h,z,entero("Puntos"));case "E"->new InstalacionCertificada(c,n,d,h,z,entero("Puntos"),numero("Valor materiales Q"));default->new MantenimientoAire(c,n,d,h,z,entero("BTU"));};}
 static void menu(){System.out.println("\n1 Cotizar | 2 Registrar orden | 3 Registrar cuadrilla | 4 Crear jornada | 5 Asignar | 6 Retirar | 7 Iniciar | 8 Resultado | 9 Consultar | 10 Reportes | 0 Salir");}
 public static void main(String[] args){s.semilla();while(true){try{menu();String op=pedir("Opcion");switch(op){
 case "0":return;
 case "1":{Orden o=capturar();System.out.printf(Locale.US,"Cotizacion: Q%.2f, %d dias habiles (no registrada)%n",o.costo(),o.dias());break;}
 case "2":{Orden o=capturar();s.registrar(o);System.out.printf(Locale.US,"Registrada y cobrada Q%.2f. Estado: pendiente%n",o.costo());break;}
 case "3":s.registrarCuadrilla(new Cuadrilla(pedir("Codigo"),numero("Capacidad horas"),pedir("Certificada refrigeracion (S/N)").equalsIgnoreCase("S")));System.out.println("Cuadrilla registrada");break;
 case "4":System.out.println(s.crearJornada(pedir("Codigo jornada"),pedir("Codigo cuadrilla")));break;
 case "5":s.jornada(pedir("Jornada")).asignar(s.orden(pedir("Orden")));System.out.println("Asignada");break;
 case "6":s.jornada(pedir("Jornada")).retirar(pedir("Orden"));System.out.println("Retirada");break;
 case "7":s.iniciar(pedir("Jornada"));System.out.println("Jornada iniciada");break;
 case "8":{Jornada j=s.jornada(pedir("Jornada"));String id=pedir("Orden");String r=pedir("Resultado (C completada, F fallida)").toUpperCase();if(!r.equals("C")&&!r.equals("F"))throw new IllegalArgumentException("Resultado invalido");j.resultado(id,r.equals("C"));System.out.println("Resultado guardado: "+s.orden(id).estado());break;}
 case "9":s.ordenes.values().forEach(System.out::println);s.cuadrillas.values().forEach(System.out::println);s.jornadas.values().forEach(System.out::println);break;
 case "10":s.reporte();break;
 default:System.out.println("Opcion no valida");
 }}catch(RuntimeException ex){System.out.println("Operacion rechazada: "+ex.getMessage());}}}
}
