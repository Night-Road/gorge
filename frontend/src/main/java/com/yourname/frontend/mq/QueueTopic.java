package com.yourname.frontend.mq;

public interface QueueTopic {
    int TEST = 0;
    int SEAT_PLACE_ROLLBACK = 1;
    int ORDER_CREATE=2;
    int ORDER_PAY_DELAY_CHECK=3;
    int ORDER_CANCEL=4;
}
