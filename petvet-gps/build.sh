# Build the Java package
mvn clean package

# Build the Docker image
docker build . -t petvet-gps:latest

# Leave the swarm if already part of one
docker swarm leave --force

# Initialize Docker Swarm
docker swarm init

# Remove the existing macvlan network if it exists
docker network rm macvlan_net

# Create the macvlan network in Swarm mode
docker network create -d macvlan \
    --scope swarm \
    --subnet=192.168.0.0/24 \
    --gateway=192.168.0.1 \
    -o parent=wlo1 \
    macvlan_net

# Deploy the stack using Docker Compose
docker stack deploy -c docker-compose.yml my-java-stack
