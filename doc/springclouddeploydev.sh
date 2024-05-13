#!/bin/bash
#端口号
PORT=
#应用名
APPLICATION_NAME=
#基本路径
DEFAULT_PACKAGE_PATH="/home/application"
#启动超时时间
APP_START_TIMEOUT=180
#覆盖基本路径
TARGET_PACKAGE_PATH=
#应用部署路径
APP_HOME=
#应用部署路径
HEALTH_CHECK_URL=""
#挂载配置
VOLUMN=""

init_arg(){
	while getopts ':n:p:t:H:v:' opt
	do
	case $opt in
	n)
        APPLICATION_NAME=$OPTARG
        echo 应用名${OPTARG}
        ;;
	p)
		PORT=$OPTARG
		echo 端口号${OPTARG}
		;;
	t)
		TARGET_PACKAGE_PATH=$OPTARG
        echo 应用路径${OPTARG}
        ;;
    H)
        HEALTH_CHECK_URL=$OPTARG
        echo 健康检查url:${OPTARG}
        ;;
    v)
        VOLUMN=$OPTARG
        echo 挂载配置:${OPTARG}
	esac
	done
	check_arg;
}

check_arg(){
	if [ ! -n $PORT ];then
	echo "-p [端口号]必须指定";
	exit 1;
	fi
	if [ ! -n $APPLICATION_NAME ];then
	echo "-n [应用名]必须指定";
	exit 1;
	fi
}

#启动服务
start_application(){
    if [ -n "${TARGET_PACKAGE_PATH}" ];then
        APP_HOME=${TARGET_PACKAGE_PATH}/${APPLICATION_NAME};
    else
        APP_HOME=${DEFAULT_PACKAGE_PATH}/${APPLICATION_NAME};
    fi
    echo "部署路径:${APP_HOME}";
    IMAGE_NAME="${APPLICATION_NAME}-image-dev";
    SERVER_NAME="${APPLICATION_NAME}-server-dev";
    mkdir -p ${APP_HOME}
    mkdir -p ${APP_HOME}/logs
    mv -f ./* ${APP_HOME};
    cd ${APP_HOME};
    # 停止Docker容器
    docker stop ${SERVER_NAME};
    # 删除Docker容器
    docker rm -f ${SERVER_NAME};
    # 删除Docker镜像
    docker rmi ${IMAGE_NAME} -f;
    # 构建Docker镜像
    docker build -t ${IMAGE_NAME} .;
    # 启动Docker容器
    docker run -itd -u root --network=host \
    -e nacos.addr=172.19.209.188:8848 \
    -e nacos.discovery.group=DEFAULT_GROUP \
    -e nacos.config.group=DEFAULT_GROUP \
    -e nacos.namespace=0bc4d8c0-a515-4592-ab39-2064114a1bc8 \
    -e JAVA_OPTS="$JAVA_OPTS -Dnacos.addr=172.19.209.188:8848 -Dnacos.discovery.group=DEFAULT_GROUP -Dnacos.config.group=DEFAULT_GROUP -Dnacos.namespace=0bc4d8c0-a515-4592-ab39-2064114a1bc8" \
    --name ${SERVER_NAME} -v /home/catcxlog:/logs -p ${PORT}:${PORT} ${DEFAULT_VOLUMN} ${IMAGE_NAME};
}

#服务下线
service_shutdown(){
  SERVICE_URL=http://localhost:${PORT}/actuator/stopService;
  result=$(curl -X POST ${SERVICE_URL})
  echo "下线服务结果：$result"
  if [ "$result" = 'true' ]; then
    sleep 30
  fi
}

#监控检测
health_check() {
    exptime=0
    # 应用健康检查URL
    if [ ! -n "${HEALTH_CHECK_URL}" ];then
        HEALTH_CHECK_URL=http://localhost:${PORT}/actuator/health;
    fi
    echo "checking ${HEALTH_CHECK_URL}";
    while true
    do
        status_code=`/usr/bin/curl -L -o /dev/null --connect-timeout 5 -s -w %{http_code}  ${HEALTH_CHECK_URL}`
        if [ x$status_code != x200 ];then
            sleep 1
            ((exptime++))
            echo -n -e "\rWait app to pass health check: $exptime..."
        else
            break
        fi
        if [ $exptime -gt ${APP_START_TIMEOUT} ]; then
            echo
            echo 'app start failed'
            exit -1
        fi
    done
    echo "check ${HEALTH_CHECK_URL} success"
}

Usage(){
cat <<EOF
Usage:
	-n 应用名
	-p 端口号
	-t 目标路径 如果不指定则为默认路径${DEFAULT_PACKAGE_PATH}
    -H 心跳检测路径 如果不指定则为默认路径${HEALTH_CHECK_URL}
    -v 挂载配置
	-h 查看帮助文档
EOF
    exit 1
}

[ "$1" = "-h" ] && Usage
init_arg "$@"
service_shutdown
start_application
health_check
