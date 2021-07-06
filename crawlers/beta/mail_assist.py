import smtplib
from email.mime.text import MIMEText
from email.utils import formataddr
from database_util import query_subscription_by_send_time

# 邮箱参数字典
# 说明：根据收件人地址自动选择发送邮箱类型，减小被异名服务器拦截邮件的概率。
mail_dict = {
    # QQ邮箱
    'mail_qq': {
        'sender': '发件人地址',
        'password': '申请QQ邮箱的smtp口令',
        'server': 'smtp.qq.com',
        'getter': []
    },
    # 网易邮箱
    'mail_163': {
        'sender': '发件人地址',
        'password': '申请网易邮箱的smtp口令',
        'server': 'smtp.163.com',
        'getter': []
    }
}


# 分类收件人邮箱类型
def divide_mail(getter):
    for user in getter:
        if user.__contains__('qq.com'):
            mail_dict['mail_qq']['getter'].append(user)
        elif user.__contains__('163.com'):
            mail_dict['mail_163']['getter'].append(user)


# 根据邮箱参数发送邮件
def send_mail(msg, email_dict):
    sender = email_dict['sender']
    getter = email_dict['getter']
    server_url = email_dict['server']
    password = email_dict['password']

    msg['From'] = formataddr([sender, sender])  # 括号里的对应发件人邮箱昵称、发件人邮箱账号
    server = smtplib.SMTP_SSL(server_url, 465)  # 发件人邮箱中的SMTP服务器，端口是465
    server.login(sender, password)  # 括号中对应的是发件人邮箱账号、邮箱密码

    for user in getter:
        msg['To'] = formataddr([user, user])  # 括号里的对应收件人邮箱昵称、收件人邮箱账号
        server.sendmail(sender, user, msg.as_string())  # 括号中对应的是发件人邮箱账号、收件人邮箱账号、发送邮件
    server.quit()

    # 打印结果信息
    if len(getter) is not 0:
        print('发件人 "' + sender + '" 发送成功')


# 仅根据用户列表发送邮件
def mail_send_with_user(content, subject, getters):
    divide_mail(getters)

    ret = True
    try:
        msg = MIMEText(str(content), "html", "utf-8")
        msg['Subject'] = subject  # 邮件的主题，也可以说是标题

        # qq
        send_mail(msg, mail_dict['mail_qq'])

        # 163
        send_mail(msg, mail_dict['mail_163'])

        print("发送程序执行完毕。")
    except Exception as e:  # 如果 try 中的语句没有执行，则会执行下面的 ret=False
        print(e)
        ret = False
    return ret


# 根据订阅时间发送邮件
def send_mail_with_time(subject, content, send_time, service_name):
    my_sender = ''  # 发件人邮箱账号
    my_pass = ''  # 发件人邮箱密码(当时申请smtp给的口令)
    my_user = ''  # 收件人邮箱账号

    try:
        send_list = query_subscription_by_send_time(service_name=service_name, send_time=send_time)
        if len(send_list) == 0:
            return False
        else:
            msg = MIMEText(str(content), "html", "utf-8")
            msg['From'] = formataddr([my_sender, my_sender])  # 括号里的对应发件人邮箱昵称、发件人邮箱账号
            msg['Subject'] = subject  # 邮件的主题，也可以说是标题

            server = smtplib.SMTP_SSL("smtp.qq.com", 465)  # 发件人邮箱中的SMTP服务器，端口是465
            server.login(my_sender, my_pass)  # 括号中对应的是发件人邮箱账号、邮箱密码

            for email in send_list:
                msg['To'] = formataddr([email, email])  # 括号里的对应收件人邮箱昵称、收件人邮箱账号
                server.sendmail(my_sender, email, msg.as_string())  # 括号中对应的是发件人邮箱账号、收件人邮箱账号、发送邮件
            print("已发送给以下账户：")
            for email in send_list:
                print(email)
            server.quit()  # 关闭连接
            print("发送成功")
    except Exception as e:
        print(e)
        return False
    return True
