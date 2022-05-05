public class Specifications {
    private double Default;
    private double min;
    private double max;
    public Specifications(double Default, double min, double max){
        this.Default = Default;
        this.min = min;
        this.max = max;
    }

    public double getDefault() {
        return Default;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
