public class ODUSystemSolving {
    double a;
    double b;
    double u10;
    double u20;
    int n;
    double[] x;
    double[] u1;
    double[] u2;
    double h;
    Method method;

    public ODUSystemSolving() {}

    public ODUSystemSolving(double a, double b, double u10, double u20, int n, Method method) {
        this.a = a;
        this.b = b;
        this.u10 = u10;
        this.u20 = u20;
        this.n = n;
        this.method = method;
        this.h = (b - a) / n;

        x = new double[n + 1];
        x[0] = a;
        for (int i = 1; i <= n; i++)
            x[i] = a + (h * i);

        u1 = new double[n + 1];
        u2 = new double[n + 1];
        u1[0] = u10;
        u2[0] = u20;

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
            u1[i] = u1[i-1] + h * Function.f1(x[i-1], u1[i-1], u2[i-1]);
            u2[i] = u2[i-1] + h * Function.f2(x[i-1], u1[i-1], u2[i-1]);
        }
    }

    private void RungeKutt() {
        for (int i = 1; i <= n; i++) {
            double k11 = h * Function.f1(x[i-1], u1[i-1], u2[i-1]);
            double k21 = h * Function.f2(x[i-1], u1[i-1], u2[i-1]);

            double k12 = h * Function.f1(x[i-1] + h / 2, u1[i-1] + k11 / 2, u2[i-1]+ k11 / 2);
            double k22 = h * Function.f2(x[i-1] + h / 2, u1[i-1] + k21 / 2, u2[i-1]+ k21 / 2);

            double k13 = h * Function.f1(x[i-1] + h, u1[i-1] - k11 + 2 * k12, u2[i-1] - k11 + 2 * k12);
            double k23 = h * Function.f2(x[i-1] + h, u1[i-1] - k21 + 2 * k22, u2[i-1] - k21 + 2 * k22);

            u1[i] = u1[i-1] + (k11 + 4 * k12 + k13) / 6;
            u2[i] = u2[i-1] + (k21 + 4 * k22 + k23) / 6;
        }
    }
}
