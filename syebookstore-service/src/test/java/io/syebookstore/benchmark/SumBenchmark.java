package io.syebookstore.benchmark;

import io.syebookstore.api.account.AuthUtils;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
@Threads(1)
@Warmup(time = 1)
@Measurement(time = 1)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SumBenchmark {

  private static final String SECRET = "test12345";

  private String token;

  @Setup
  public void setup() {
    token = AuthUtils.createToken(SECRET, Long.MAX_VALUE);
  }

  @Benchmark
  public void createToken(Blackhole blackhole) {
    blackhole.consume(AuthUtils.createToken(SECRET, Long.MAX_VALUE));
  }

  @Benchmark
  public void verifyToken(Blackhole blackhole) {
    blackhole.consume(AuthUtils.verify(SECRET, token));
  }
}
