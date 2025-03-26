package io.syebookstore.benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner {
  public static void main(String[] args) throws Exception {
    new Runner(new OptionsBuilder().include("AuthUtilsBenchmark").build()).run();
  }
}
