/**
 * Created by sbenkhaoua on 28/04/15.
 */
public class Segment {
    private double extr1;
    private double  extr2;

    public Segment(double e1,double e2){
        extr1 = e1;
        extr2 = e2;
        ordered();

    }

    private void ordered() {
        if (extr1 > extr2){
            double tampon;

            tampon = extr1;
            extr1 = extr2;
            extr2 = tampon;
        }
    }
    public void setExtr1(double a) {
        extr1 = a;
        ordered();
    }

    public double getExtr1() {
        return extr1;
    }

    public void setExtr2(double a) {
        extr2 = a;
        ordered();
    }

    public double getExtr2() {
        return extr2;
    }

    public double longueur() {
        return extr2 - extr1;
    }

    public boolean appartient(double x) {
        return (x >= extr1) && (x <= extr2) ;
    }

    public String toString() {
        return "segment [" + extr1 + ", " + extr2 + "]";
    }
}
