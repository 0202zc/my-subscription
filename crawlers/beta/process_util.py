import os
import sys
import logging
from datetime import datetime

import covid19_spider as covid19
import spider_customize as customize
import spider_hot as hot
import weibo_spider as weibo
import zhihu_spider as zhihu

from apscheduler.schedulers.background import BackgroundScheduler
from apscheduler.schedulers.blocking import BlockingScheduler
from apscheduler.events import EVENT_JOB_EXECUTED, EVENT_JOB_ERROR

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S',
                    filename='log' + str(datetime.now().strftime("%Y%m%d%H%M%S")) + '.txt',
                    filemode='a')


def my_listener(event):
    if event.exception:
        print('任务出错了！！！！！！')
    else:
        print('任务照常运行...')


# 任务
def job_weibo():
    print("<job_weibo> - " + str(datetime.now().strftime("%Y-%m-%d %H:%M:%S")))
    weibo.main()


def job_zhihu():
    print("<job_zhihu> - " + str(datetime.now().strftime("%Y-%m-%d %H:%M:%S")))
    zhihu.main()


def job_hot():
    print("<job_hot> - " + str(datetime.now().strftime("%Y-%m-%d %H:%M:%S")))
    local_time = datetime.now().strftime("%H:%M")
    hot.hot(str(local_time)[:2] + ':00:00')


def job_covid19():
    print("<job_covid19> - " + str(datetime.now().strftime("%Y-%m-%d %H:%M:%S")))
    covid19.process()


def job_customize():
    print("<job_customize> - " + str(datetime.now().strftime("%Y-%m-%d %H:%M:%S")))
    local_time = datetime.now().strftime("%H:%M")
    customize.process(str(local_time)[:2] + ':00:00')


def my_job(text):
    print(text)


if __name__ == '__main__':
    # 允许两个job同时执行
    job_defaults = {'max_instances': 2}

    # BlockingScheduler 会阻塞进程，不能执行start()后面的程序
    scheduler = BlockingScheduler()

    # 不会阻塞进程，可以执行start()后面的程序
    # scheduler = BackgroundScheduler()

    scheduler.add_job(func=job_hot, trigger='cron', day_of_week='0-6', hour='8,12,20', minute='00', id='job_hot',
                      misfire_grace_time=60)
    scheduler.add_job(func=job_customize, trigger='cron', day_of_week='0-6', hour='8,12,20', id='job_customize',
                      misfire_grace_time=60)
    scheduler.add_listener(my_listener, EVENT_JOB_EXECUTED | EVENT_JOB_ERROR)

    jobs = scheduler.get_jobs()
    print(jobs)

    scheduler.start()
