import pymysql

host = "localhost"
port = 3306
user = "root"
passwd = "root"
db = "db_mail_send"


def db_login(host, port, user, passwd, database):
    conn = pymysql.connect(host=host, port=port, user=user, passwd=passwd, db=database,
                           charset="utf8")
    return conn


def query_subscription_by_service_id(service_id):
    global host, port, user, passwd, db

    conn = db_login(host, port, user, passwd, db)
    cur = conn.cursor()
    sql = '''
        select u.email, s.send_time, s.allow_send
        from db_mail_send.subscription as s
        left join users u on s.user_id = u.id
        where s.allow_send = 1 and s.service_id = "%s";
    ''' % service_id
    send_list = []
    try:
        cur.execute(sql)
        results = cur.fetchall()
        for row in results:
            email = row[0]
            send_time = row[1]
            allow_send = row[2]
            print("email=%s, send_time=%s, authority=%s" % (email, send_time, allow_send))
            send_list.append(email)
    except Exception as e:
        print(e)
    finally:
        conn.close()
        return send_list


# 查找该时间下的该服务是否有被订阅
def query_subs_service_count(service_id, send_time):
    global host, port, user, passwd, db

    conn = db_login(host, port, user, passwd, db)
    cur = conn.cursor()
    sql = "select count(*) from subscription where allow_send = 1 and service_id = %s and send_time = '%s';" % (
        service_id, send_time)
    try:
        cur.execute(sql)
        result = cur.fetchone()
        return True if result > 0 else False
    except Exception as e:
        print(e)
        return False
    finally:
        conn.close()


def query_crawls_path():
    global host, port, user, passwd, db

    conn = db_login(host, port, user, passwd, db)
    cur = conn.cursor()
    sql = '''
    select sc.id, s.note, c.file_path
    from crawlers c, service_crawler sc, services s
    where c.id = sc.crawler_id and s.id = sc.service_id and s.enabled = 1;
    '''
    try:
        dict = {}
        cur.execute(sql)
        results = cur.fetchall()
        # 不采用一个服务对应多个爬虫的模式，因为可能导致执行顺序混乱。多个爬虫可以合并进一个.py文件
        for row in results:
            service_name = row[1]
            crawl_path = row[2]
            dict[service_name] = crawl_path
        return dict
    except Exception as e:
        print(e)
        return None
    finally:
        conn.close()


def query_service_id_by_name(name):
    global host, port, user, passwd, db

    conn = db_login(host, port, user, passwd, db)
    cur = conn.cursor()
    sql = '''
            SELECT id
            FROM services
            WHERE note = "%s";
        ''' % (name)
    try:
        cur.execute(sql)
        result = cur.fetchone()
        return result
    except Exception as e:
        print(e)
    finally:
        conn.close()
        return None


def query_subscription_by_send_time(service_name, send_time):
    global host, port, user, passwd, db

    conn = db_login(host, port, user, passwd, db)
    cur = conn.cursor()
    sql = '''
        select u.email, s2.note, s.send_time, s.allow_send
        from db_mail_send.subscription as s
        left join users u on s.user_id = u.id
        left join services s2 on s.service_id = s2.id
        where s.allow_send = 1 and u.is_allowed = 1 and s2.note = "%s" and s.send_time = "%s"; 
    ''' % (service_name, send_time)
    send_list = []
    try:
        cur.execute(sql)
        results = cur.fetchall()
        for row in results:
            email = row[0]
            service_name = row[1]
            send_time = row[2]
            allow_send = row[3]
            print(
                "email=%s, service_name=%s, send_time=%s, authority=%s" % (email, service_name, send_time, allow_send))
            send_list.append(email)
    except Exception as e:
        print(e)
    finally:
        conn.close()
        return send_list


def query_users():
    global host, port, user, passwd, db

    conn = db_login(host, port, user, passwd, db)
    cur = conn.cursor()
    sql = 'select id, email from users where is_allowed = 1;'

    user_list = []
    try:
        cur.execute(sql)
        results = cur.fetchall()
        for row in results:
            user_list.append([row[0], row[1]])
    except Exception as e:
        print(e)
    finally:
        conn.close()
        return user_list


def query_subscriptions_by_user_id(user_id, send_time):
    global host, port, user, passwd, db

    conn = db_login(host, port, user, passwd, db)
    cur = conn.cursor()
    sql = '''
        select service_id
        from subscription
        where user_id = %s and send_time = '%s' and allow_send = 1
    ''' % (user_id, send_time)

    subs_list = []
    try:
        cur.execute(sql)
        results = cur.fetchall()
        for row in results:
            subs_list.append(row[0])
    except Exception as e:
        print(e)
    finally:
        conn.close()
        return subs_list


def db_insert(sql):
    global host, port, user, passwd, db
    flag = False

    conn = db_login(host, port, user, passwd, db)
    cursor = conn.cursor()

    try:
        cursor.execute(sql)
        conn.commit()
        flag = True
    except Exception as e:
        print(e)
    finally:
        conn.close()
        return flag


def db_delete(sql):
    global host, port, user, passwd, db
    flag = False

    conn = db_login(host, port, user, passwd, db)
    cursor = conn.cursor()

    try:
        cursor.execute(sql)
        conn.commit()
        flag = True
    except Exception as e:
        print(e)
    finally:
        conn.close()
        return flag


if __name__ == '__main__':
    pass
