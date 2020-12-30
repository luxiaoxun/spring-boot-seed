package com.luxx.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BatchUtil {

    private static final int DEFAULT_BATCH_SIZE = 1000;

    public static <T> void batchConsumer(List<T> dataList, Consumer<List<T>> consumer) {
        batchConsumer(dataList, DEFAULT_BATCH_SIZE, consumer);
    }

    public static <T> void batchConsumer(List<T> dataList, int batchSize, Consumer<List<T>> consumer) {
        int start = 0;
        int size = dataList.size();
        while (start < size) {
            int end = start + batchSize;
            if (end > size) {
                end = size;
            }
            List<T> subList = dataList.subList(start, end);
            if (subList.isEmpty()) {
                break;
            }
            consumer.accept(subList);
            start = end;
        }
    }

    public static <T, R> List<R> batchFunction(List<T> dataList, int batchSize, Function<List<T>, R> function) {
        List<R> resultList = new ArrayList<>();
        batchConsumer(dataList, batchSize, e -> resultList.add(function.apply(e)));
        return resultList;
    }
}
