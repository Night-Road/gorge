package com.yourname.sync.db;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @Description TODO
 * @Author dell
 * @Date 11/1/2022 9:54 AM
 */
public class TrainSeatTableShardingAlgorithm implements PreciseShardingAlgorithm<Integer> {
    private final static String PREFI = "train_seat_";

    private String determineTable(int val) {
        int table = val % 10;
        if (table == 0) {
            table = 10;
        }
        return PREFI + table;
    }

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> preciseShardingValue) {

        String actualTableName = determineTable(preciseShardingValue.getValue());
        if (collection.contains(actualTableName)){
            return actualTableName;
        }
        throw new IllegalArgumentException();
    }
}
