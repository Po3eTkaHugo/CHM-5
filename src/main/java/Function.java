public class Function {
    public static double func(double x, double y) {
        return x / 5 - y; //Task1 y0(0.2) = 1.1
        //return Math.cos(y) / (1.5 + x) + 0.1 * y * y; //Task2 y0(0) = 0
    }

    public static double funcExact(double x) {
        return 63.0 / 50.0 * Math.exp(-x + 0.2) + (x - 1.0) / 5.0; //Task1
    }

    public static double f1(double x, double u1, double u2) { //Task3
        return -5 * u1 - u2 + Math.exp(x);
    }
    public static double f2(double x, double u1, double u2) {
        return u1 - 3 * u2 + Math.exp(2 * x);
    }
}
