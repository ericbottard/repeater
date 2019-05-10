package com.acme;

import java.io.IOException;
import java.time.Duration;
import java.util.function.BiFunction;

import reactor.core.publisher.Flux;

public class Repeater implements BiFunction<Flux<String>, Flux<Integer>, Flux<String>[]> {

    private static final String[] numbers = new String[]{"zero", "one", "two", "three", "four", "five"};

    @Override
    public Flux<String>[] apply(Flux<String> stringFlux, Flux<Integer> integerFlux) {
        System.out.println("Invoked");
        return new Flux[]{stringFlux.zipWith(integerFlux)
            .doOnNext(System.out::println)
            .flatMap(t -> {
            if (t.getT2().intValue() == 0) {
                return Flux.empty();
            }
            return Flux.just(t.getT1()).repeat(t.getT2() - 1);
        })};
    }



}
