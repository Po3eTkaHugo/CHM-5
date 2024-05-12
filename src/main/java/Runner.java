public class Runner {
    public static void main(String[] args) {
        //Task1 ans Task2
        System.out.printf("%-9s \t %-15s \t %-15s \n", "eps", "Euler", "Runge-Kutt");
        double eps = 0.01;
        for (int i = 1; i < 6; i++) {
            System.out.printf("%-5f \t %-15d \t %-15d \n",
                    eps,
                    accuracy(eps, Method.Euler, 2),
                    accuracy(eps, Method.RungeKutt, 2));
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
}
