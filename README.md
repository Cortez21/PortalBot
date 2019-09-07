# PortalBot
Monitoring condition of employee's CRM account. The employee
registrates in bot system and set his login and password from "Portal" CRM.
The program logins in CRM and parse data from web interface for following
of change's dynamic of current tasks statuses, work route refreshing and
following of work inventory transporting. All observed changes sends in
private chat of Telegram for faster reaction on accidents and employee
work time saving. 

For starting use: "mvn exec:java -Dexec.mainClass="com.portalbot.main.Bot" -Dexec.classpathScope=runtime"
