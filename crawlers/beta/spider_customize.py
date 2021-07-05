# 用户自定义爬虫检测文件
import importlib
import os
import sys

from database_util import *
from datetime import datetime
from mail_assist import mail_send_with_user


class Customize:
    file_dict = {}
    content_dict = {}

    def __init__(self):
        self.file_dict = query_crawls_path()
        # 将内容插入到数据库中
        self.insert_content()
        # 获取存储的内容
        self.get_content()

    def insert_content(self):
        for service_id in self.file_dict:
            crawler_id = self.file_dict[service_id][0]
            filename = self.file_dict[service_id][1][0:-3]
            service_name = self.file_dict[service_id][2]
            path = self.file_dict[service_id][3]

            try:
                module_name = importlib.import_module('customize.' + filename)  # 需要修改
                content = module_name.process()
                insert_crawls_content([content], service_id)
            except ModuleNotFoundError as mne:
                print(mne)
            except Exception as e:
                print(e)
                # mail_send_with_user(e, 'sprider_customize.py 出现异常', '1115810371@qq.com', ['lzc980202@163.com'])

    def get_content(self):
        service_list = query_active_subs_service()
        for service_id in service_list:
            self.content_dict[service_id] = query_content_by_service_id(service_id)

    def push_message(self, time):
        user_list = query_users()
        service_dict = query_service_name()
        for user in user_list:
            subs_list = query_subscriptions_by_user_id(user[0], time)

            if subs_list is None:
                continue

            content = ''
            subject = ''
            for subs in subs_list:
                if self.content_dict[subs] is None:
                    continue
                print('service_id = %s,' % subs, 'email = %s' % user[1])
                subject += service_dict[subs] + ' '
                content += '<h2>'
                content += service_dict[subs]
                content += '</h2>'
                content += self.content_dict[subs][0]
                content += '<br>'

            if content.__len__() is not 0 or content is not '':
                local_time = datetime.now().strftime("%m-%d %H:%M")
                mail_send_with_user(content, subject + '(' + local_time + ')', '发件人', [user[1]])


def process(time):
    custom = Customize()
    custom.push_message(time)


if __name__ == '__main__':
    process('12:00:00') # 测试
