import os
import requests
import sys
import time

from bs4 import BeautifulSoup
from mail_assist import mail_send_with_user
from mail_assist import send_mail_with_time
from apscheduler.schedulers.blocking import BlockingScheduler
from datetime import datetime

url = "https://s.weibo.com/top/summary"

file_path = 'C:/Users/Administrator/Desktop/runnable/weibo_spider.py'

header = {
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
    'Accept-Encoding': 'gzip, deflate, br',
    'Accept-Language': 'zh-CN,zh;q=0.9',
    'Cache-Control': 'max-age=0',
    'Host': 's.weibo.com',
    'sec-ch-ua': '" Not;A Brand";v="99", "Google Chrome";v="91", "Chromium";v="91"',
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.106 Safari/537.36'
}


def get_soup(url):
    print("Getting soup...")
    html = get_HTML_text(url)
    soup = BeautifulSoup(html, "html.parser")
    return soup


def get_HTML_text(url):
    try:
        r = requests.get(url, headers=header)
        while r.status_code != 200:
            time.sleep(600)
            r = requests.get(url, headers=header)
            print(r.status_code)
        r.raise_for_status()
        r.encoding = r.apparent_encoding
        return r.text
    except Exception as e:
        print(e)
        return e


# 单独发送
def process():
    soup = get_soup(url)
    data = soup.find(name="div", attrs={"id": "pl_top_realtimehot"})
    content = str(data.prettify()).replace("/weibo?q=", "https://s.weibo.com/weibo?q=")

    local_datetime = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    local_time = datetime.now().strftime("%H:%M:%S")
    print(local_datetime)

    send_mail_with_time("微博热搜榜 " + local_time, content, send_time=(str(local_time)[:2] + ':00:00'),
                        service_name='微博热搜榜')


def prettify():
    soup = get_soup(url)
    data = soup.find(name="div", attrs={"id": "pl_top_realtimehot"})
    content = str(data.prettify()).replace("/weibo?q=", "https://s.weibo.com/weibo?q=").replace("<table>",
                                                                                                '<table border="1" cellspacing="0">')
    return content


def main():
    print("Running...")
    try:
        process()
    except Exception as e:
        print(e)
        mail_send_with_user(e, "An Exception from 'weibo_spider.py'", "发件人", "收件人")
        print("程序暂停3秒后重新执行")
        time.sleep(3)
        os.system("cls")
        os.system('python "' + file_path + '"')


if __name__ == '__main__':
    print("Running...")
    prettify()