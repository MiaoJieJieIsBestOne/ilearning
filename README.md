# ilearning
ilearning学堂

# 项目启动步骤
1、各类中间件，如mysql、redis等均部署在腾讯云上
2、启动nginx
    2.1、cd D:\Java\MiddleWare\nginx-1.23.1
    2.2、双击nginx.exe
2、启用minIO
    账号：minioadmin   密码：minioadmin
    2.1、cd D:\Java\MiddleWare\minIO
    2.2、minio.exe server D:\Java\MiddleWare\minIO\data1 D:\Java\MiddleWare\minIO\data2 D:\Java\MiddleWare\minIO\data3 D:\Java\MiddleWare\minIO\data4
3、启动xxl-job-2.3.1工程，访问[http://127.0.0.1:8088/xxl-job-admin](http://127.0.0.1:8088/xxl-job-admin/)地址
4、启动前端工程ilearning_vue
5、执行启动类
