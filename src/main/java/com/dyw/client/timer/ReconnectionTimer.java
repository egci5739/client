package com.dyw.client.timer;

import com.dyw.client.controller.Egci;
import com.dyw.client.form.RegisterForm;
import com.dyw.client.task.ReconnectionTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class ReconnectionTimer {
    private Logger logger = LoggerFactory.getLogger(ReconnectionTimer.class);
    private RegisterForm registerForm;
    //时间间隔(一天)
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    public ReconnectionTimer(RegisterForm registerForm) {
        this.registerForm = registerForm;
    }

    public void open1() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Egci.configEntity.getExitTimeHour1());
        calendar.set(Calendar.MINUTE, Egci.configEntity.getExitTimeMinute1());
        calendar.set(Calendar.SECOND, Egci.configEntity.getExitTimeSecond1());
        Date date = calendar.getTime(); //第一次执行定时任务的时间
        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = addDay(date, 1);
        }
        Timer timer = new Timer();
        ReconnectionTask reconnectionTask = new ReconnectionTask(registerForm);
        timer.schedule(reconnectionTask, date, PERIOD_DAY);
        logger.info("自动重连启动成功");
    }

    public void open2() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Egci.configEntity.getExitTimeHour2());
        calendar.set(Calendar.MINUTE, Egci.configEntity.getExitTimeMinute2());
        calendar.set(Calendar.SECOND, Egci.configEntity.getExitTimeSecond2());
        Date date = calendar.getTime(); //第一次执行定时任务的时间
        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = addDay(date, 1);
        }
        Timer timer = new Timer();
        ReconnectionTask reconnectionTask = new ReconnectionTask(registerForm);
        timer.schedule(reconnectionTask, date, PERIOD_DAY);
        logger.info("自动重连启动成功");
    }

    // 增加或减少天数
    private static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}
