package com.acme;

import reactor.core.publisher.Flux;
import reactor.math.MathFlux;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Repeater implements BiFunction<Flux<String>, Flux<Integer>, Flux<?>[]> {

    @Override
    public Flux<?>[] apply(Flux<String> stringFlux, Flux<Integer> integerFlux) {


        Flux<String> repeated = stringFlux.zipWith(integerFlux)
                .doOnNext(System.out::println)
                .flatMap(t -> Flux.fromIterable(Collections.nCopies(t.getT2(), t.getT1()))
                );

        Flux<Integer> sum = integerFlux.buffer(3, 1).map(l -> l.stream().mapToInt(Integer::intValue).sum());
        //Flux<Integer> sum = Flux.interval(Duration.ofMillis(500)).map(Long::intValue);

        return new Flux[]{repeated, sum};
    }

}
