package org.cc.demo.schedule;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.cc.demo.common.constant.Constant;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * quartz 的简单使用
 * 
 * @author dixingxing
 * @date Feb 14, 2012
 */
public final class QuartzScheduler {
	private static final Logger LOG = LoggerFactory.getLogger(QuartzScheduler.class);
	
	private QuartzScheduler() {}
	
	/**
	 * 创建job
	 * 
	 * @param name
	 * @param group
	 * @return
	 */
	private static JobDetail getJob(String name, String group) {
		return newJob(SimpleJob.class).withIdentity(name, group).build();
	}

	/**
	 * 创建trigger
	 * 
	 * @param name
	 * @param group
	 * @param cron
	 * @return
	 */
	private static Trigger getTrigger(String name, String group, String cron) {
		return newTrigger().withIdentity(name, group).withSchedule(
				cronSchedule(cron)).build();
	}

	public static void run() throws SchedulerException, InterruptedException {

		String jobGroup  = "group1";
		
		Scheduler sched = new StdSchedulerFactory().getScheduler();

		// 定义任务1
		JobDetail job = getJob("job1", jobGroup);
		Trigger trigger = getTrigger("trigger1", jobGroup, "0/5 * * * * ?");

		Date ft = sched.scheduleJob(job, trigger);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		LOG.info(job.getKey() + " 已经被加入，下次执行时间为: " + sdf.format(ft));

		// 定义任务2
		job = getJob("job2", jobGroup);
		trigger = getTrigger("trigger2", jobGroup, "0/3 * * * * ?");

		ft = sched.scheduleJob(job, trigger);
		LOG.info(job.getKey() + " 已经被加入，下次执行时间为: " + sdf.format(ft));

		// start之后job才会执行
		sched.start();

		Thread.sleep(Constant.TIME_MINUTE);

		sched.shutdown(true);

		LOG.info("共执行" + sched.getMetaData().getNumberOfJobsExecuted()
				+ " 个job");

	}

	public static void main(String[] args) {
		try {
			QuartzScheduler.run();
		} catch (SchedulerException e) {
			LOG.error("",e);
		} catch (InterruptedException e) {
			LOG.error("",e);
		}
	}

}