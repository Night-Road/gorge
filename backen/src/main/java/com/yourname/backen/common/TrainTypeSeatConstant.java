package com.yourname.backen.common;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author dell
 * @Date 10/30/2022 8:25 AM
 */
public class TrainTypeSeatConstant {

    private static Table<Integer, Integer, Pair<Integer, Integer>> crh2Table = HashBasedTable.create();

    private static Table<Integer, Integer, Integer> crh2SpecialTable = HashBasedTable.create();

    //crh2的每一节车厢有几排
    private static List<Integer> crh2CarriageRowTotal = Lists.newArrayList(0, 11, 20, 17, 20, 11, 20, 13, 13, 11, 20, 17,
            20, 11, 20, 13, 13);

    private static Table<Integer, Integer, Pair<Integer, Integer>> crh5Table = HashBasedTable.create();

    private static Table<Integer, Integer, Integer> crh5SpecialTable = HashBasedTable.create();

    //crh5的每一节车厢有几排
    private static List<Integer> crh5CarriageRowTotal = Lists.newArrayList(0, 15, 19, 19, 19, 19, 9, 16, 15, 15, 19, 19,
            19, 19, 9, 16, 15);

    private static Map<TrainType, Table<Integer, Integer, Pair<Integer, Integer>>> carriageMap = Maps.newConcurrentMap();

    //车厢的座位等级
    private static Table<TrainType, Integer, TrainSeatLevel> seatCarriageLevelTable = HashBasedTable.create();

    static {
        for (int row = 1; row <= 12; row++) {
            crh2SpecialTable.put(7, row, 1);
            crh2SpecialTable.put(15, row, 4);
        }
        crh2SpecialTable.put(15, 13, 3);
        crh2SpecialTable.put(7, 13, 3);
        crh2SpecialTable.put(8, 1, 4);
        crh2SpecialTable.put(16, 1, 4);

        for (int carriage = 1; carriage < crh2CarriageRowTotal.size(); carriage++) {
            //遍历每一节车厢
            int order = 0;
            for (int row = 1; row <= crh2CarriageRowTotal.get(carriage); row++) {
                int count = 5;
                if (crh2SpecialTable.contains(carriage, row)) {
                    count = crh2SpecialTable.get(carriage, row);
                }


                crh2Table.put(carriage, row, Pair.of(order + 1, order + count));
                order += count;
            }
        }

        for (int row = 1; row <= 12; row++) {
            crh5SpecialTable.put(8, row, 4);
            crh5SpecialTable.put(16, row, 4);
        }
        crh5SpecialTable.put(1, 1, 4);
        crh5SpecialTable.put(2, 1, 3);
        crh5SpecialTable.put(3, 1, 3);
        crh5SpecialTable.put(4, 1, 3);
        crh5SpecialTable.put(5, 1, 3);
        crh5SpecialTable.put(6, 1, 3);
        crh5SpecialTable.put(6, 9, 4);
        crh5SpecialTable.put(7, 1, 3);
        crh5SpecialTable.put(7, 16, 1);

        crh5SpecialTable.put(9, 1, 4);
        crh5SpecialTable.put(10, 1, 3);
        crh5SpecialTable.put(11, 1, 3);
        crh5SpecialTable.put(12, 1, 3);
        crh5SpecialTable.put(13, 1, 3);
        crh5SpecialTable.put(14, 1, 3);
        crh5SpecialTable.put(14, 9, 4);
        crh5SpecialTable.put(15, 1, 3);
        crh5SpecialTable.put(15, 16, 1);

        for (int carriage = 1; carriage < crh5CarriageRowTotal.size(); carriage++) {
            //遍历每一节车厢
            int order = 0;
            for (int row = 1; row <= crh5CarriageRowTotal.get(carriage); row++) {
                int count = 5;
                if (crh5SpecialTable.contains(carriage, row)) {
                    count = crh5SpecialTable.get(carriage, row);
                }

                //Pair.of()返回不可变的pair对象
                crh5Table.put(carriage, row, Pair.of(order + 1, order + count));
                order += count;
            }
        }

        carriageMap.put(TrainType.CRH2, crh2Table);
        carriageMap.put(TrainType.CRH5, crh5Table);

        seatCarriageLevelTable.put(TrainType.CRH2, 1, TrainSeatLevel.TOP_GRADE);
        seatCarriageLevelTable.put(TrainType.CRH2, 2, TrainSeatLevel.GRADE_1);

        seatCarriageLevelTable.put(TrainType.CRH5, crh5CarriageRowTotal.size() - 1, TrainSeatLevel.TOP_GRADE);
        seatCarriageLevelTable.put(TrainType.CRH5, crh5CarriageRowTotal.size() - 1, TrainSeatLevel.GRADE_1);
    }

    public static Table<Integer, Integer, Pair<Integer, Integer>> getTable(TrainType trainType) {
        return carriageMap.get(trainType);
    }

    public static TrainSeatLevel getLevel(TrainType trainType, Integer carriage) {
        if (seatCarriageLevelTable.contains(trainType, carriage)) {
            return seatCarriageLevelTable.get(trainType, carriage);
        }
        return TrainSeatLevel.GRADE_2;
    }
}
