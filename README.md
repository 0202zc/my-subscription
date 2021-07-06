# my-subscription 邮件订阅软件
- 业务流程：用户访问页面，提交需要订阅的内容和时间（可自行定义服务），程序调度爬虫适时进行邮件推送。
- 本项目采用前后端分离架构。后端业务处理基于 SpringBoot + MyBatis，爬虫基于Python编写；前端使用 Ace Admin 作为模板
- 使用 Redis 作为缓存
- 采用 Nginx 搭建服务器，托管前端代码

## 面向人群
- 不能及时或不想自己手动获取信息
- 网络上信息获取需求不能满足（例如没有现成的需求信息的推送服务）

## 前端页面功能介绍

### Forms > 服务订阅：
- 用户添加订阅（选择服务、推送时间），可选择系统自带服务，也可自定义服务（暂未实现不同服务选择各自的推送时间）；
- 已经添加过订阅服务的用户，输入信息时会自动填充以往提交的内容，方便取消订阅；
- 该页面的“接收订阅”一栏中，可切换推送状态（打开/关闭）。另外，在选择推送时间“第二步 Alert”里面，也可以关闭推送服务。

### Forms > 文件上传：
- 用户自定义服务功能。用户上传.py的爬虫文件，且一个爬虫文件只能对应一个服务；若有多个爬虫相关联，则选择其中一个文件用于返回格式化后的内容，格式如下：
     ```python
     def process():
        content = "(返回的内容)"  # 此为发送到邮件的内容，推荐使用html格式
        ...
        return str(content) # 将其转化为字符串并返回给调度文件customize.py
     ```
     由于邮件内容采用html格式，因此，内容推荐使用html构造，以方便接收者查看。最后转为字符串返回。
- 首先上传.py的爬虫文件（不能与服务器已有文件重名），上传成功后选择**一个**对应的.py文件，然后输入自定义服务信息进行提交。
- 添加成功后可选择是否继续添加服务：
  - 若是，则留在本页继续添加，注意不要刷新页面，因为后文件的显示会丢失；
  - 否则，回到服务订阅页面进行订阅。

### 数据管理 > 服务管理：
- 查看服务信息：编号、名称、订阅量、类型、爬虫文件、发布者、备注、创建时间和状态等
- 修改状态：启用/禁用
- 删除服务（暂未实现修改服务信息）
- 导出服务列表到.CSV文件
- 打印服务列表

### 数据管理 > 用户管理：
- 查看用户信息： 编号、用户名、邮箱、权限、用户类型和注册时间等
- 修改用户：使用ajax动态修改数据并显示，可修改的内容仅有用户名、邮箱、权限和用户类型
- 删除用户：每一行的“操作”为单独修改方式，表格下方的删除和编辑为批量修改方式
- 添加用户：左下方的“加号”按钮，仅能添加邮箱、权限和用户类型，用户名需要点击编辑才能修改(bug)

# Installation
## Requirement
- `Java 1.8`
- `MySQL 5.7`
- `Nginx: latest stable version`
- `Python 3.6`
- `Redis: latest stable version`

## Step
1. git clone 项目 `my-subscription` 到本地
2. 新建数据库 `db_mail_send`，选择 `utf8mb4` 编码，运行 `config/database/` 中的.sql文件进行导入
3. 配置Nginx，参考 `config/nginx/conf` 中的文件，将 `html/ace-master` 文件夹以移动到本地nginx的html目录下
4. 配置Redis，配置应与application.yml设置一致（自定义）
5. 修改后端代码（IDEA项目文件夹：MySubcription）
     - 根据个人情况修改applicaiton.yml中的内容
     - 用户自定义上传的文件路径位于 `com/lzc/util/FileUtils.java` 的常量 `FILEPATH`，根据情况修改
     - 运行服务器，启动项目
6. 启动Python调度：
     - 利用 `cd` 命令进入`process_uitl.py`文件夹后，控制台执行命令：`python process_util.py`
     - 所有Python文件位于crawlers/**：
          - stable 文件夹为爬虫的稳定版本，beta为测试版本，beta/customize 为用户自定义文件存放位置
          - database_util.py：数据库工具文件，注意修改登录配置信息
          - mail_assist.py：邮件发送工具文件，根据内容进行信息配置，需要开通smtp服务
          - spider_hot.py：管理员编写的爬虫文件，整合了`weibo_spider.py`（微博热搜）、`zhihu_spider.py`（知乎热搜）、`covid19_spider.py`（国内新冠疫情每日新增信息）
          - spider_customize.py：用户自定义爬虫调度工具文件，扫描文件的路径根据实际情况修改
          - process_util.py：总调度文件，在每日的8点、12点、20点启动程序
> 注：html/ace-master文件夹中，修改过的文件以"\*-copy.html"结尾
