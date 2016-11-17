call mvn package
FOR /F "tokens=5 delims= " %%P IN ('netstat -a -n -o ^| findstr :25565') DO @ECHO TaskKill.exe /PID %%P /F
FOR /F "tokens=5 delims= " %%P IN ('netstat -a -n -o ^| findstr :25565') DO TaskKill.exe /PID %%P /F
echo copy
copy /b/v/y "target\nukkit-1.0-SNAPSHOT.jar" "C:\Users\Jesse\Desktop\OTHER\mcpe\nukkit-1.0-SNAPSHOT.jar"
echo cd
cd "C:\Users\Jesse\Desktop\OTHER\mcpe"
echo start
#start.bat