oldpid=`cat pid.txt`
echo killing Previous pid: ${oldpid}
kill ${oldpid}
echo sleep 10s Release port...
sleep 10s
echo runing app
nohup java -jar agent_server.jar >/dev/null &
echo "$!" > pid.txt
newpid=`cat pid.txt`
echo runing new app pid is : ${newpid}


