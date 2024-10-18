package net.vrakin.medsalary.generator;

public interface Generator<S, R>{
    R generate(S source) throws Exception;
}
