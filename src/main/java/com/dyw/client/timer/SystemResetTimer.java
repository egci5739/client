package com.dyw.client.timer;

import com.dyw.client.controller.Egci;
import com.dyw.client.task.SystemResetTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class SystemResetTimer {
    private final Logger logger = LoggerFactory.getLogger(SystemResetTimer.class);
    //时间间隔(一天)
    private final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    public void open() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Egci.configEntity.getSynchronizationHour() - 1);//比同步时间提前1个小时
        calendar.set(Calendar.MINUTE, Egci.configEntity.getSynchronizationMinute());
        calendar.set(Calendar.SECOND, Egci.configEntity.getSynchronizationSecond());
        Date date = calendar.getTime(); //第一次执行定时任务的时间
        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = addDay(date, 1);
        }
        Timer timer = new Timer();
        logger.info("系统重置时间：" + calendar.getTime());
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        SystemResetTaskService systemResetTaskService = new SystemResetTaskService();
        timer.schedule(systemResetTaskService, date, PERIOD_DAY);
    }

    // 增加或减少天数
    private Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}