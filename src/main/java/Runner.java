public class Runner {
    public static void main(String[] args) {
        //Task1 ans Task2
        System.out.println("Task1 and Task2");
        System.out.printf("%-9s \t %-15s \t %-15s \n", "eps", "Euler", "Runge-Kutt");
        double eps = 0.01;
        for (int i = 0; i < 5; i++) {
            System.out.printf("%-5f \t %-15d \t %-15d \n",
                    eps,
                    accuracy(eps, Method.Euler, 2),
                    accuracy(eps, Method.RungeKutt, 2));
            eps *= 0.1;
        }

        //Task3
        System.out.println("\n\nTask3");
        System.out.printf("%-9s \t %-15s \t %-15s \n", "eps", "Euler", "Runge-Kutt");
        eps = 0.1;
        for (int i = 0; i < 4; i++) {
            System.out.printf("%-5f \t %-15d \t %-15d \n",
                    eps,
                    systemAccuracy(eps, Method.Euler),
                    systemAccuracy(eps, Method.RungeKutt));
            eps *= 0.1;
        }
    }

    public static int accuracy(double eps, Method method, int task) {
        int n = 9;
        double delta = 1.0;

        do {
            n++;
            ODUSolving oduSolving = new ODUSolving(0.2, 1, 1.1, n, method);

            switch (task){
                case 1: {
                    delta = Math.abs(Function.funcExact(1) - oduSolving.y[oduSolving.n]) / (Math.pow(2, 4) - 1);
                    break;
                }
                case 2: {
                    ODUSolving oduSolving2n = new ODUSolving(0.2, 1, 1.1, 2 * n, method);
                    delta = Math.abs(oduSolving.y[oduSolving.n] - oduSolving2n.y[oduSolving2n.n]) / (Math.pow(2, 4) - 1);
                    break;
                }
            }
        } while (delta > eps);

        return n;
    }

    public static int systemAccuracy(double eps, Method method) {
        int n = 9;
        double delta = 1.0;

        do {
            n++;
            ODUSystemSolving oduSystemSolving = new ODUSystemSolving(0, 2, 119.0/900.0, 211.0/900.0, n, method);
            ODUSystemSolving oduSystemSolving2n = new ODUSystemSolving(0, 2, 119.0/900.0, 211.0/900.0, 2 * n, method);
            delta = Math.max(Math.abs(oduSystemSolving.u1[oduSystemSolving.n] - oduSystemSolving2n.u1[oduSystemSolving2n.n]) / (Math.pow(2, 3) - 1),
                             Math.abs(oduSystemSolving.u2[oduSystemSolving.n] - oduSystemSolving2n.u2[oduSystemSolving2n.n]) / (Math.pow(2, 3) - 1));

        } while (delta > eps);

        return n;
    }
}
