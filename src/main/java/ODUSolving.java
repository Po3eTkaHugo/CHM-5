public class ODUSolving {
    double a;
    double b;
    double y0;
    int n;
    double[] x;
    double[] y;
    double eps;
    double h;
    Method method;

    public ODUSolving() {}

    public ODUSolving(double a, double b, double y0, int n, double eps, Method method) {
        this.a = a;
        this.b = b;
        this.y0 = y0;
        this.n = n;
        this.eps = eps;
        this.method = method;
        this.h = (b - a) / n;

        x = new double[n + 1];
        x[0] = a;
        for (int i = 1; i <= n; i++)
            x[i] = a + (h * i);

        y = new double[n + 1];
        y[0] = y0;

        switch (method) {
            case Euler: {
                Euler();
                break;
            }
            case RungeKutt: {
                RungeKutt();
                break;
            }
        }

    }

    private void Euler() {
        for (int i = 1; i <= n; i++) {
            y[i] = y[i-1] + h * Function.func(x[i-1], y[i-1]);
        }
    }

    private void RungeKutt() {
        for (int i = 1; i <= n; i++) {
            double k1 = h * Function.func(x[i-1], y[i-1]);
            double k2 = h * Function.func(x[i-1] + h / 4, y[i-1] + k1 / 4);
            double k3 = h * Function.func(x[i-1] + h / 2, y[i-1] + k2 / 2);
            double k4 = h * Function.func(x[i-1] + h, y[i-1] + k1 - 2 * k2 + 2 * k3);
            y[i] = y[i-1] + (k1 + 4 * k3 + k4) / 6;
        }
    }
}
