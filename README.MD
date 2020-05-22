# SpringBoot Monitor

Monitors any SpringBoot applications with configured actuator/health end-point
It dedicates thread per URL so max number of monitoring host is limited by number of threads supported by OS per process
At the moment it supports email notification only using "smarthost" and configured account to submit email.

# Build

mvn clean install

# Run

It requires external application.properties file with next parameters:

## Mandatory configuration

```
# Single or list of comma delimited URLs for monitoring, for ex. http://localhost:8080/actuator/health
agent.urls=

# List of alert's recipients
alert.to=

# Mail server access credentials
email.username=
email.password=

# From email account
email.mail-from=

# Mail server host:port
email.mail-server=
email.mail-port=
```

## Optional configuration

```
# Polling time in seconds, default is 60 sec
agent.poll-time=

# Delay to send DOWN alert, default is 300 sec
agent.down-delay=
```
