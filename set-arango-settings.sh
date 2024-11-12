#!/bin/bash
sudo bash -c "echo madvise > /sys/kernel/mm/transparent_hugepage/enabled"
sudo bash -c "echo madvise > /sys/kernel/mm/transparent_hugepage/defrag"
#sudo sysctl -w "vm.max_map_count=512000"
sudo sysctl -w "vm.max_map_count=1024000"


