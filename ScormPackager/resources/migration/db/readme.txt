1. Do a backup of the existing DB (for example mysql):
   >>mysqldump -u root -p sahara > /path/backup.sql

2. Run the migration file (in this case migrate_mysql.sql):
   >>mysql -u root -p sahara < migrate_mysql.sql
