public class TravelNode implements Comparable<TravelNode> {

    private static final double heuristicWeight = 1.0;
    private Pixel currentPixel;
    private Pixel previousPixel;
    private double g;
    private double h;

    public TravelNode(Pixel previousPixel, Pixel currentPixel) {
        this.currentPixel = currentPixel;
        this.previousPixel = previousPixel;
    }

    @Override
    public String toString() {
        return this.currentPixel.toString();
    }

    @Override
    public int compareTo(TravelNode o) {
        return Double.compare((this.g + ( heuristicWeight * this.h)),(o.g + (heuristicWeight * o.h)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TravelNode)) return false;

        TravelNode that = (TravelNode) o;

        return getCurrentPixel().equals(that.getCurrentPixel());
    }

    @Override
    public int hashCode() {
        return getCurrentPixel().hashCode();
    }

//    public Pixel getStartPixel() {
//        return startPixel;
//    }

//    public Pixel getEndPixel() {
//        return endPixel;
//    }

    public Pixel getCurrentPixel() {
        return currentPixel;
    }

    public Pixel getPreviousPixel() {
        return previousPixel;
    }

    public void setPreviousPixel(Pixel previousPixel) {
        this.previousPixel = previousPixel;
    }

    public double getG() {
        return g;
    }

    public double getH() {
        return h;
    }

//    public void setStartPixel(Pixel startPixel) {
//        this.startPixel = startPixel;
//    }

//    public void setEndPixel(Pixel endPixel) {
//        this.endPixel = endPixel;
//    }

    public void setCurrentPixel(Pixel currentPixel) {
        this.currentPixel = currentPixel;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void setH(double h) {
        this.h = h;
    }
}
