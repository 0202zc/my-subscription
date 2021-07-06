import demjson
import os
import re
import requests
import sys
import time
import urllib.parse

from bs4 import BeautifulSoup
from mail_assist import mail_send_with_user
from mail_assist import send_mail_with_time
from apscheduler.schedulers.blocking import BlockingScheduler
from datetime import datetime

url = "https://www.zhihu.com/topsearch"

file_path = 'C:/Users/Administrator/Desktop/runnable/beta/customize/zhihu_spider.py'

header = {
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
    'Accept-Encoding': 'gzip, deflate, br',
    'Accept-Language': 'zh-CN,zh;q=0.9',
    'Cache-Control': 'max-age=0',
    'sec-ch-ua': '" Not A;Brand";v="99", "Chromium";v="90", "Google Chrome";v="90"',
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
    data = prettify()

    local_datetime = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    local_time = datetime.now().strftime("%H:%M:%S")
    print(local_datetime)

    send_mail_with_time("知乎热搜 " + local_time, data, send_time=(str(local_time)[:2] + ':00:00'),
                        service_name='知乎热搜榜')


def prettify():
    soup = get_soup(url).findAll("script", id="js-initialData")
    script_text = str(soup[0])
    rule = r'"topsearch":(.*?),"requestHashId"'
    result = re.findall(rule, script_text)
    temp = result[0].replace("false", '"False"').replace("true", '"True"') + "}"
    data_dict = demjson.decode(temp)['data']

    content = """
                    <table border="1" cellspacing="0">
                        <tr>
                            <th align="center">排行</th>
                            <th align="center">标题</th>
                        </tr>
                    """
    rank = 1

    for data in data_dict:
        content += '<tr><td align="center">' + str(rank) + "</td>"
        content += '<td><a href="https://www.zhihu.com/search?type=content&q=' + urllib.parse.quote(
            data['realQuery']) + '" target="_blank">' + data['queryDisplay'] + "</a><br>" + data[
                       'queryDescription'] + "</td></tr>"

        rank += 1

    content += "</table>"

    return content


if __name__ == '__main__':
    print("Running...")
    content = prettify()
