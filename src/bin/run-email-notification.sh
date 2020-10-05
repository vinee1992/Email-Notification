echo "Email-Notification Processing started at `date` "

JVM="java"

java -Xms1256m -Xmx2512m -jar NotificationHandler-0.0.1-SNAPSHOT.jar &

echo "Email-Notification Processing Finished at `date` "


