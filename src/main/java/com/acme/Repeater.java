package com.acme;

import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.function.BiFunction;

public class Repeater implements BiFunction<Flux<String>, Flux<Integer>, Flux<?>[]> {

    @Override
    public Flux<?>[] apply(Flux<String> stringFlux, Flux<Integer> integerFlux) {

        Flux<Integer> sharedIntFlux = integerFlux.publish().autoConnect(2);

        Flux<String> repeated = stringFlux.zipWith(sharedIntFlux)
                .doOnNext(System.out::println)
                .flatMap(t -> Flux.fromIterable(Collections.nCopies(t.getT2(), t.getT1())));

        Flux<Integer> sum = sharedIntFlux.buffer(3, 1).map(l -> l.stream().mapToInt(Integer::intValue).sum()).take(10);
        //Flux<Integer> sum = Flux.interval(Duration.ofMillis(500)).map(Long::intValue);

        return new Flux[]{repeated, sum};
    }

}
