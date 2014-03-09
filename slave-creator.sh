#! bin/bash

gcutil --service_version="v1" --project="plated-reducer-359" adddisk "$1" --zone="us-central1-a" --source_snapshot="slave-march-8"
gcutil --service_version="v1" --project="plated-reducer-359" addinstance "$1" --zone="us-central1-a" --machine_type="g1-small" --network="default" --external_ip_address="ephemeral" --service_account_scopes="https://www.googleapis.com/auth/userinfo.email,https://www.googleapis.com/auth/compute,https://www.googleapis.com/auth/devstorage.full_control" --disk="$1,deviceName=$1,mode=READ_WRITE,boot" --metadata=startup-script:'#! /bin/bash
git clone https://github.com/dibyom/BuildingOptimizer.git
cd BuildingOptimizer
ant slave -Dargs="-file params/building.slave.params" >> output.log
EOF'