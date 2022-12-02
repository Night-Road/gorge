package com.yourname.sync.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.yourname.sync.service.TrainSeatServeice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.List;


/**
 * @Description TODO
 * @Author dell
 * @Date 11/5/2022 10:31 AM
 */

@Component
@Slf4j
public class CanalSubscribe implements ApplicationListener<ContextRefreshedEvent> {
    @Resource
    TrainSeatServeice trainSeatServeice;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        canalSubscribe();
    }

    private void canalSubscribe() {
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("127.0.0.1",
                11111), "example", "", "");
        int batchSize = 1000;

        new Thread(() -> {
            try {
                connector.connect();
                connector.subscribe(".*\\..*");
                connector.rollback();
                log.info("canal启动成功");
                while (true) {
                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                        safeSleep(100);
                        continue;
                    }
                    try {
                        log.info("mew message,bitchId:{},size:{}", batchId, size);
                        printEntry(message.getEntries());
                        connector.ack(batchId); // 提交确认
                    } catch (Exception e2) {
                        log.info("mew message,bitchId:{},size:{}", batchId, size);
                        connector.rollback(batchId); // 处理失败, 回滚数据
                    }
                }
            } catch (Exception e3) {
                log.error("canal subsribe error", e3);
                //项目启动失败重试
                safeSleep(2000);
                canalSubscribe();
            } finally {
                connector.disconnect();
            }
        }).start();

    }

    private void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }
            RowChange rowchange = null;
            try {
                rowchange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("rowChang.eparser error , data:" + entry.toString(), e);
            }

            String schemaName = entry.getHeader().getSchemaName();
            String tableName = entry.getHeader().getTableName();
            EventType eventType = rowchange.getEventType();
            log.info("name:[{},{}],eventType:{}", schemaName, tableName, eventType);


            for (RowData rowData : rowchange.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    handleColumn(rowData.getBeforeColumnsList(), eventType, schemaName, tableName);
                } else {
                    handleColumn(rowData.getAfterColumnsList(), eventType, schemaName, tableName);
                }
            }
        }
    }

    private void handleColumn(List<Column> columns, EventType eventType, String schemeName, String tableName) {
        if (schemeName.contains("train_seat")) {
            //处理座位变更
            trainSeatServeice.handle(columns, eventType);
        } else if (tableName.equals("train_number_detail")) {
            //处理作为详情

        } else {
            log.info("你这该死的温柔");
        }
    }

    private void safeSleep(int m) {
        try {
            Thread.sleep(m);
        } catch (Exception e) {

        }
    }
}
