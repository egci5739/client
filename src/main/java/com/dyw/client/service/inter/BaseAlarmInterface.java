package com.dyw.client.service.inter;

import com.dyw.client.entity.AlarmEntity;

public interface BaseAlarmInterface {
    void saveOtherEvent(AlarmEntity alarmEntity);

    void addAlarmInfo(AlarmEntity alarmEntity);
}
