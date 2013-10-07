Tunign Linux
============
See also `Xitrum guide <http://ngocdaothanh.github.io/xitrum/guide/deploy.html#tune-linux-for-many-connections>`_.

Increase open file limit
------------------------
``/etc/security/limits.conf``

::

  *  soft  nofile  1024000
  *  hard  nofile  1024000

To confirm

    ulimit -n.

Tune kernel
-----------
``/etc/sysctl.conf``

::

  # General gigabit tuning
  net.core.rmem_max = 16777216
  net.core.wmem_max = 16777216
  net.ipv4.tcp_rmem = 4096 87380 16777216
  net.ipv4.tcp_wmem = 4096 65536 16777216

  # This gives the kernel more memory for TCP
  # which you need with many (100k+) open socket connections
  net.ipv4.tcp_mem = 50576 64768 98152

  # Backlog
  net.core.netdev_max_backlog = 2048
  net.core.somaxconn = 1024
  net.ipv4.tcp_max_syn_backlog = 2048
  net.ipv4.tcp_syncookies = 1

To apply

    sudo sysctl -p

Set up port forwarding
----------------------
``/etc/sysconfig/iptables``

    sudo su - root

    chmod 700 /etc/sysconfig/iptables

    iptables-restore < /etc/sysconfig/iptables

    iptables -A PREROUTING -t nat -i eth0 -p tcp --dport 80 -j REDIRECT --to-port 8000

    iptables -A PREROUTING -t nat -i eth0 -p tcp --dport 443 -j REDIRECT --to-port 4430

    iptables -t nat -I OUTPUT -p tcp -d 127.0.0.1 --dport 80 -j REDIRECT --to-ports 8000

    iptables -t nat -I OUTPUT -p tcp -d 127.0.0.1 --dport 443 -j REDIRECT --to-ports 4430

    iptables-save -c > /etc/sysconfig/iptables

    chmod 644 /etc/sysconfig/iptables



Stop Appach
-----------

    sudo /etc/init.d/httpd stop

    sudo chkconfig httpd off


Set up supervisord
------------------
``/etc/supervisord.conf``

::

  [program:mobilus-isucon3]
  directory = /home/isucon/mobilus-isucon3
  command = /home/isucon/mobilus-isucon3/target/xitrum/bin/runner.sh
  autostart =true
  autorestart =true
  startsecs = 3
  user = isucon
  redirect_stderr =true
  stdout_logfile=/var/log/xitrum.log
  stdout_logfile_maxbytes=10MB
  stdout_logfile_backups=7
  stdout_capture_maxbytes=1MB
  stdout_events_enabled=false
  environment=PATH=/usr/local/bin:/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/sbin:/opt/aws/bin:/home/oshida/bin