/* 
 * Copyright 2005 - 2009 Terracotta, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

package org.cc.demo.schedule;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
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
public class QuartzScheduler {

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

	public static void run() throws Exception {
		Logger log = LoggerFactory.getLogger(QuartzScheduler.class);

		Scheduler sched = new StdSchedulerFactory().getScheduler();

		// job1
		JobDetail job = getJob("job1", "group1");
		Trigger trigger = getTrigger("trigger1", "group1", "0/5 * * * * ?");

		Date ft = sched.scheduleJob(job, trigger);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		log.info(job.getKey() + " 已经被加入，下次执行时间为: " + sdf.format(ft));

		// job2
		job = getJob("job2", "group1");
		trigger = getTrigger("trigger2", "group1", "0/3 * * * * ?");

		ft = sched.scheduleJob(job, trigger);
		log.info(job.getKey() + " 已经被加入，下次执行时间为: " + sdf.format(ft));

		// start之后job才会执行
		sched.start();

		Thread.sleep(30L * 1000L);

		sched.shutdown(true);

		log.info("共执行" + sched.getMetaData().getNumberOfJobsExecuted()
				+ " 个job");

	}

	public static void main(String[] args) throws Exception {
		QuartzScheduler.run();
	}

}
