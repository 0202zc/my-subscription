import requests
import json
from mail_assist import mail_send_with_user
from mail_assist import send_mail_with_time
from datetime import datetime

file_path = 'C:/Users/Administrator/Desktop/runnable/covid19_spider.py'


def covid19():
    url = 'https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5'
    json_text = requests.get(url).json()
    data = json.loads(json_text['data'])
    last_update_time = data['lastUpdateTime']
    all_counties = data['areaTree']
    c_list = []
    content = """
            <table border="1" cellspacing="0">
                <tr>
                    <td>新增省份</td>
                    <td>新增城市</td>
                    <td>新增人数</td>
                    <td>总确诊人数</td>
                </tr>
                    """
    for country_data in all_counties:
        if country_data['name'] == '中国':
            all_provinces = country_data['children']
            for province_data in all_provinces:
                province_name = province_data['name']
                all_cities = province_data['children']
                for city_data in all_cities:
                    city_today = city_data['today']
                    city_add = city_today['confirm']
                    city_name = city_data['name']
                    city_total = city_data['total']
                    city_totaladd = city_total['nowConfirm']
                    if city_add > 0:
                        content += """
                            <tr>
                                <td>""" + province_name + """</td>
                                <td>""" + city_name + """</td>
                                <td>""" + str(city_add) + """</td>
                                <td>""" + str(city_totaladd) + """</td>
                            </tr>
                        """
                        c = '新增省份: ' + province_name + '----新增城市: ' + city_name + ', 新增人数：' + str(
                            city_add) + ',  截至目前一共确诊人数:' + str(city_totaladd)
                        print(c)
                        c_list.append(c)
    content += "</table>"

    info = '今日四川无新增，不必担心'
    for c in c_list:
        if c.__contains__('四川'):
            info = '今日四川有新增，提高警惕，注意防护'
            break

    content += ("<br>" + info)
    res = []
    res.append(content)
    res.append(last_update_time)

    return res


def process():
    try:
        res = covid19()
        # mail_send_with_user(res[0], "COVID-19新增数据 " + res[1], "1115810371@qq.com", ["lzc980202@163.com"])\
        local_time = datetime.now().strftime("%H:%M:%S")
        print(local_time)
        send_mail_with_time("COVID-19 新增数据 " + res[1], res[0], send_time=(str(local_time)[:2] + ':00:00'),
                            service_name='COVID-19 新增数据')
    except Exception as e:
        print(e)


def main():
    print("Running...")
    try:
        process()
    except Exception as e:
        print(e)
        mail_send_with_user(e, "An Exception from 'covid19_spider.py'", "发件人", "收件人")
        print("程序暂停3秒后重新执行")
        time.sleep(3)
        os.system("cls")
        os.system('python "' + file_path + '"')


if __name__ == '__main__':
    process()
