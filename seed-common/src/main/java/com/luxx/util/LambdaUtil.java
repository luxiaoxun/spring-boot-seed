package com.luxx.util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaUtil {
    public static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

    public static <T> void batchExecute(List<T> dataList, int maxNumber, Consumer<List<T>> consumer) {
        int limit = (dataList.size() + maxNumber - 1) / maxNumber;
        Stream.iterate(0, n -> n + 1)
                .limit(limit)
                .map(a -> dataList.stream().skip(a * maxNumber).limit(maxNumber).collect(Collectors.toList()))
                .forEach(consumer::accept);
    }
}
