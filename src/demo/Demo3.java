package demo;

import model.test.Benchmarker;

public class Demo3 {
    static void main(String[] args) {
        Benchmarker bm = Benchmarker.of(Benchmarker.test1, 10000);
        System.out.println(bm);
    }
}
