public class Function {
    public static double func(double x, double y) {
        //return x / 5 - y; //Task1
        return Math.cos(y) / (1.5 + x) + 0.1 * y * y; //Task2
    }
    public static double funcExact(double x) {
        return 63.0 / 50.0 * Math.exp(-x + 0.2) + (x - 1.0) / 5.0; //Task1
    }
}
