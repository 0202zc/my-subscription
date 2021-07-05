import weibo_spider as weibo
import zhihu_spider as zhihu
import covid19_spider as covid19

from mail_assist import mail_send_with_user
from mail_assist import send_mail_with_time
from datetime import datetime
from database_util import query_subscriptions_by_user_id


def hot():
    table_weibo = weibo.prettify()
    table_zhihu = zhihu.prettify()
    table_covid19 = covid19.covid19()

    dict = {}
    dict['weibo'] = table_weibo
    dict['zhihu'] = table_zhihu
    dict['covid19'] = table_covid19





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
    hot()
