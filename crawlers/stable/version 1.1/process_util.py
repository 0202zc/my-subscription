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
    local_time = datetime.now().strftime("%H:%M")
    hot.hot(str(local_time)[:2] + ':00:00')


def job_covid19():
    print("<job_covid19> - " + str(datetime.now().strftime("%Y-%m-%d %H:%M:%S")))
    covid19.process()


def my_job(text):
    print(text)


if __name__ == '__main__':
    # BlockingScheduler
    scheduler = BlockingScheduler()
    scheduler.add_job(func=job_hot, trigger='cron', day_of_week='0-6', hour='8,12,20', id='job_hot', misfire_grace_time=60)

    jobs = scheduler.get_jobs()
    print(jobs)

    scheduler.start()
