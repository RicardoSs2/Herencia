class Cuadrilla {
 final String codigo;
 final double capacidad;
 final boolean refrigeracion;
 private Jornada activa;
 Cuadrilla(String c,double h,boolean r){if(c==null||c.isBlank()||!Double.isFinite(h)||h<=0)throw new IllegalArgumentException("Cuadrilla invalida");codigo=c.trim();capacidad=h;refrigeracion=r;}
 boolean libre(){return activa==null;}
 void ocupar(Jornada j){if(!libre())throw new IllegalStateException("Cuadrilla ocupada");activa=j;}
 void liberar(Jornada j){if(activa!=j)throw new IllegalStateException("Jornada no pertenece a cuadrilla");activa=null;}
 public String toString(){return codigo+" | capacidad "+capacidad+" h | refrigeracion "+refrigeracion+" | libre "+libre();}
}
