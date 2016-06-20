#!/usr/bin/env bash
# /etc/init.d/yadel

touch /var/lock/yadel

case "$1" in
  start)
    bash /usr/sbin/yadel_server
    ;;
  stop)
    bash /usr/sbin/yadel_stop
    ;;
  *)
    echo "Usage: /etc/init.d/yadel {start|stop}"
    exit 1
    ;;
esac

exit 0