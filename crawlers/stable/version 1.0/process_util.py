import os, sys

from apscheduler.schedulers.blocking import BlockingScheduler
from datetime import datetime
import covid19_spider as covid19
import weibo_spider as weibo
import zhihu_spider as zhihu
import spider_hot as hot


# 任务
def job_weibo():
    print("<job_weibo> - " + str(datetime.now().strftime("%Y-%m-%d %H:%M:%S")))
    weibo.main()

def job_zhihu():
    print("<job_zhihu> - " + str(datetime.now().strftime("%Y-%m-%d %H:%M:%S")))
    zhihu.main()

def job_hot():
    print("<job_hot> - " + str(datetime.now().strftime("%Y-%m-%d %H:%M:%S")))
    hot.process()

def job_covid19():
    print("<job_covid19> - " + str(datetime.now().strftime("%Y-%m-%d %H:%M:%S")))
    covid19.process()

def my_job(text):
    print(text)


if __name__ == '__main__':
    # BlockingScheduler
    scheduler = BlockingScheduler()
    scheduler.add_job(func=job_weibo, trigger='cron', day_of_week='0-6', hour='8,13,18,20', id='job_weibo', misfire_grace_time=60)
    scheduler.add_job(func=job_zhihu, trigger='cron', day_of_week='0-6', hour='8,13,20', minute='14', id='job_zhihu', misfire_grace_time=60)
    scheduler.add_job(func=job_covid19, trigger='cron', day_of_week='0-6', hour='21', minute='30', id='job_covid19', misfire_grace_time=60)

    jobs = scheduler.get_jobs()
    print(jobs)

    scheduler.start()
