package white.common;

public class test {
    public static void main(String[] args) {
        test test = new test();
        test.start();
    }

    private void start() {
        int i = 5;
        value v = new value();
        v.i = 25;
        change(v,i);
        System.out.println(v.i);
    }

    private void change(value v, int i) {
        i = 0;
        v.i = 20;

        value val = new value();
        v = val;
        System.out.println(v.i  + "  " +i);
    }

    class  value{
        public int i  = 15;
    }
}
