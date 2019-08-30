package com.dyw.client.service.inter;

import com.dyw.client.entity.AlarmEntity;
import com.dyw.client.entity.PassRecordEntity;

public interface BaseMonitorInterface {
    void addPassInfo(PassRecordEntity passRecordEntity);

    void stressAlarmSearchHistory(AlarmEntity alarmEntity);
}
