import smtplib
from email.mime.text import MIMEText
from email.utils import formataddr
from database_util import query_subscription_by_send_time

my_sender = ''  # 发件人邮箱账号
my_pass = ''  # 发件人邮箱密码(当时申请smtp给的口令)
my_user = ''  # 收件人邮箱账号


def mail_send_with_user(content, subject, sender, getter):
    ret = True
    try:
        msg = MIMEText(str(content), "html", "utf-8")
        msg['From'] = formataddr([sender, sender])  # 括号里的对应发件人邮箱昵称、发件人邮箱账号
        msg['Subject'] = subject  # 邮件的主题，也可以说是标题

        server = smtplib.SMTP_SSL("smtp.qq.com", 465)  # 发件人邮箱中的SMTP服务器，端口是465
        server.login(my_sender, my_pass)  # 括号中对应的是发件人邮箱账号、邮箱密码

        for user in getter:
            msg['To'] = formataddr([user, user])  # 括号里的对应收件人邮箱昵称、收件人邮箱账号
            server.sendmail(my_sender, user, msg.as_string())  # 括号中对应的是发件人邮箱账号、收件人邮箱账号、发送邮件
        server.quit()  # 关闭连接
        print("发送成功")
    except Exception as e:  # 如果 try 中的语句没有执行，则会执行下面的 ret=False
        print(e)
        ret = False
    return ret


def send_mail_with_time(subject, content, send_time, service_name):
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
