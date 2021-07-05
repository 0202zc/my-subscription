import weibo_spider as weibo
import zhihu_spider as zhihu
import covid19_spider as covid19

from mail_assist import mail_send_with_user
from mail_assist import send_mail_with_time
from datetime import datetime
from database_util import query_subscriptions_by_user_id
from database_util import query_users


def hot(time):
    table_weibo = weibo.prettify()
    table_zhihu = zhihu.prettify()
    table_covid19 = covid19.covid19_II()

    dict = {1: table_weibo, 2: table_zhihu, 3: table_covid19}

    user_list = query_users()
    for user in user_list:
        subs_list = query_subscriptions_by_user_id(user[0], time)
        content = ''
        subject = ''
        for subs in subs_list:
            print('service_id = %s,' % subs, 'email = %s' % user[1])

            content += '<h2>'
            if subs == 1:
                content += "微博热搜"
                subject += '微博 '
            elif subs == 2:
                content += "知乎热搜"
                subject += '知乎 '
            elif subs == 3:
                content += "COVID-19 今日新增"
                subject += '新冠疫情新增数据 '

            content += '</h2>'
            content += dict[subs]
            content += '<br>'

        if content.__len__() is not 0 or content is not '':
            local_time = datetime.now().strftime("%H:%M")
            mail_send_with_user(content, '订阅邮件：' + subject + local_time, '发件人', [user[1]])


def process():
    table_weibo = weibo.prettify()
    table_zhihu = zhihu.prettify()

    content = "<div>微博热搜</div>" + table_weibo + '<br><div>知乎热搜</div>' + table_zhihu

    local_datetime = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    local_time = datetime.now().strftime("%H:%M:%S")
    print(local_datetime)

    send_mail_with_time("微博知乎热搜 " + local_time, content, send_time=(str(local_time)[:2] + ':00:00'),
                        service_name='微博知乎热搜')


if __name__ == '__main__':
    hot('12:00:00') # 测试
