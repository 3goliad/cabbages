FROM nginx

WORKDIR /usr/share/nginx/html

COPY index.html .
COPY src ./src
COPY assets ./assets
