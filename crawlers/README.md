# Introduction

该文件夹用于保存 Python 爬虫文件，`/stable` 为稳定版本，`/beta` 为测试版本。

# Crawlers‘ Problems & Updating

### Version 1.0 - 2021/06/25

- 多个服务分别发送邮件，未将爬取的内容融合在一个邮件里

### Version 1.1 - 2021/06/30

- 改进了1.0版本的爬虫内容合并问题
- 爬虫程序的添加非动态，未能扫描用户自定义添加的爬虫

### Version 1.2 (Beta) - 2021/07/01

- 在v1.1的基础上新增了用户自定义爬虫扫描功能，文件夹为`/beta/customize`
- 读取数据库进行扫描，扫描算法未优化

