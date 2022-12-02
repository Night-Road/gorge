package com.yourname.backen.common;

import lombok.Getter;

/**
 * @Description TODO
 * @Author dell
 * @Date 10/22/2022 11:32 PM
 */
@Getter
public enum TrainType {


    CRH2(1220),
    CRH5(1224);

    int count;

    TrainType(int count) {
        this.count = count;
    }
}
