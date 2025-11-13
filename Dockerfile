# 构建阶段：使用Maven编译打包
FROM docker-cdv5ju.swr-pro.myhuaweicloud.com/global/eclipse-temurin:8-jdk AS builder
# 安装Maven构建工具
RUN apt update && apt install -y maven && \
    apt clean && \
    rm -rf /var/lib/apt/lists/*
WORKDIR /build
# 复制项目文件
COPY pom.xml .
COPY src ./src
# 执行Maven打包，跳过测试
RUN mvn clean package -DskipTests

# 运行阶段：构建最终镜像
FROM docker-cdv5ju.swr-pro.myhuaweicloud.com/global/eclipse-temurin:8-jdk
ENV TZ Asia/Shanghai
# 安装tini进程管理器
RUN apt update && apt install -y tini && \
    apt clean && \
    rm -rf /var/lib/apt/lists/*
# 从构建阶段复制编译好的jar包
COPY --from=builder /build/target/*.jar /app/app.jar
COPY app.sh /app/
WORKDIR /app
ENTRYPOINT ["/usr/bin/tini", "--", "/bin/bash", "app.sh"]